package Repository;

import domain.Entity;
import domain.Exceptions.RepositoryException;
import domain.User;
import domain.Validation.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDbRepository implements Repository<Long, User> {
    private final Validator<User> validator;
    private final String url;
    private final String username;
    private final String password;

    /**
     * @param validator the validator used to validate user entities
     * @param url       the JDBC database connection URL
     * @param username  the database username
     * @param password  the database password
     */
    public UserDbRepository(Validator<User> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /*
    * extract entity from the current row of the given
    * @param rs the result set positioned at a valid row
    * @return the constructed {@link User} entity with its database-generated ID populated
    * @throws SQLException if a database access error occurs or a column label is invalid
    * */
    private User extractUser(ResultSet rs) throws SQLException {
        Long id=rs.getLong("id");
        String firstName=rs.getString("first_name");
        String lastName=rs.getString("last_name");
        String email=rs.getString("email");
        String pass=rs.getString("password");

        User user=new User(firstName,lastName,email,pass);
        user.setID(id);
        return user;
    }

    /**
     * Finds a user entity by its identifier in the database.
     *
     * @param id the identifier of the user to find
     * @return an {@link Optional} containing the found user, or {@link Optional#empty()} if no user exists with the given ID
     * @throws IllegalArgumentException if the provided ID is {@code null}
     * @throws RepositoryException      if a database access error occurs during the query
     */
    @Override
    public Optional<User> find(Long id) {
        if(id==null) {
            throw new IllegalArgumentException("id is null");
        }
        String sql = "SELECT id, first_name, last_name, email, password FROM users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps= connection.prepareStatement(sql)){

            ps.setLong(1,id);
            try (ResultSet rs=ps.executeQuery()){
                if (rs.next()) {
                    return Optional.of(extractUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Database error while finding user: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Retrieves all user entities from the database.
     *
     * @return an {@link Iterable} containing all users
     * @throws RepositoryException if a database access error occurs during retrieval     */
    @Override
    public Iterable<User> findAll() {
        List<User> users=new ArrayList<>();
        String sql = "SELECT id, first_name, last_name, email, password FROM users";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps= connection.prepareStatement(sql);
             ResultSet rs=ps.executeQuery()){

                 while (rs.next()) {
                     users.add(extractUser(rs));
                 }
        } catch (SQLException e) {
            throw new RepositoryException("Database error while finding user: " + e.getMessage());
        }
        return users;
    }

    /**
     * Validates and saves a new user entity to the database, assigning the auto-generated ID to the entity.
     *
     * @param entity the user entity to save
     * @return {@code null} if the user was saved successfully, or the entity itself if the insertion failed
     * @throws IllegalArgumentException                 if the entity is {@code null}
     * @throws domain.Validation.ValidationException    if the entity fails domain validation rules
     * @throws RepositoryException                      if a database access error occurs during insertion
     */
    @Override
    public User save(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        validator.validate(entity);
        String sql = "INSERT INTO users (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setID(generatedKeys.getLong(1));
                    }
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Database error while saving user: " + e.getMessage());
        }
        return entity;
    }

    /**
     * Deletes a user entity from the database by its identifier.
     *
     @param id the identifier of the user to delete
     * @return the deleted {@link User} entity
     * @throws IllegalArgumentException if the provided ID is {@code null}
     * @throws RepositoryException      if no user exists with the given ID or if a database access error occurs
     */
    @Override
    public User delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }
        User userToDelete = find(id)
                .orElseThrow(() -> new RepositoryException("Entity with ID " + id + " does not exist!"));
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            return userToDelete;
        } catch (SQLException e) {
            throw new RepositoryException("Database error while deleting user: " + e.getMessage());
        }
    }

    /**
     * Validates and updates an existing user entity in the database.
     *
     * @param entity the user entity with updated fields
     * @return {@code null} if the user was updated successfully, or the entity itself if no user with the given ID exists
     * @throws IllegalArgumentException                 if the entity is {@code null}
     * @throws domain.Validation.ValidationException    if the entity fails domain validation rules
     * @throws RepositoryException                      if a database access error occurs during the update
     */
    @Override
    public User update(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        validator.validate(entity);

        if (entity.getID() == null) {
            return entity;
        }
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
            ps.setLong(5, entity.getID());

            int affectedRows = ps.executeUpdate();
            return (affectedRows > 0) ? null : entity;
        } catch (SQLException e) {
            throw new RepositoryException("Database error while updating user: " + e.getMessage());
        }
    }
}