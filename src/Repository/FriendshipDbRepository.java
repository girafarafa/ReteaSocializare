package Repository;

import domain.Exceptions.RepositoryException;
import domain.Friendship;
import domain.User;
import domain.Validation.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipDbRepository implements Repository<Long, Friendship> {
    private final Validator<Friendship> validator;
    private final String url;
    private final String username;
    private final String password;

    /**
     * Constructs with the specified validator and database connection details.
     *
     * @param validator the validator used to validate friendship entities
     * @param url       the JDBC database connection URL
     * @param username  the database username
     * @param password  the database password
     */
    public FriendshipDbRepository(Validator<Friendship> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }


    private Friendship extractFriendship(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        LocalDateTime friendsFrom = rs.getTimestamp("friends_from").toLocalDateTime();

        User u1 = new User(
                rs.getString("u1_first"),
                rs.getString("u1_last"),
                rs.getString("u1_email"),
                rs.getString("u1_pass")
        );
        u1.setID(rs.getLong("id1"));

        User u2 = new User(
                rs.getString("u2_first"),
                rs.getString("u2_last"),
                rs.getString("u2_email"),
                rs.getString("u2_pass")
        );
        u2.setID(rs.getLong("id2"));

        Friendship friendship = new Friendship(u1, u2, friendsFrom);
        friendship.setID(id);
        return friendship;
    }

    /**
     * Finds a friendship entity by its identifier in the database.
     *
     * @param id the identifier of the friendship to find
     * @return the friendship entity if found
     * @throws UnsupportedOperationException always thrown, as database persistence is not yet implemented
     */
    @Override
     public Optional<Friendship> find(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }
        String sql = """
                SELECT f.id, f.id1, f.id2, f.friends_from,
                       u1.first_name AS u1_first, u1.last_name AS u1_last, u1.email AS u1_email, u1.password AS u1_pass,
                       u2.first_name AS u2_first, u2.last_name AS u2_last, u2.email AS u2_email, u2.password AS u2_pass
                FROM friendships f
                JOIN users u1 ON f.id1 = u1.id
                JOIN users u2 ON f.id2 = u2.id
                WHERE f.id = ?
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractFriendship(rs));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Database error while finding friendship: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Retrieves all friendship entities from the database.
     *
     * @return an {@link Iterable} containing all friendships
     * @throws UnsupportedOperationException always thrown, as database persistence is not yet implemented
     */
    @Override
    public Iterable<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();
        String sql = """
                SELECT f.id, f.id1, f.id2, f.friends_from,
                       u1.first_name AS u1_first, u1.last_name AS u1_last, u1.email AS u1_email, u1.password AS u1_pass,
                       u2.first_name AS u2_first, u2.last_name AS u2_last, u2.email AS u2_email, u2.password AS u2_pass
                FROM friendships f
                JOIN users u1 ON f.id1 = u1.id
                JOIN users u2 ON f.id2 = u2.id
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                friendships.add(extractFriendship(rs));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Database error while retrieving friendships: " + e.getMessage());
        }
        return friendships;
    }

    /**
     * Saves a new friendship entity to the database.
     *
     * @param entity the friendship entity to save
     * @return {@code null} if saved successfully
     * @throws UnsupportedOperationException always thrown, as database persistence is not yet implemented
     */
    @Override
    public Friendship save(Friendship entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        validator.validate(entity);

        Long rawId1 = entity.getFirstFriend().getID();
        Long rawId2 = entity.getSecondFriend().getID();
        long minId = Math.min(rawId1, rawId2);
        long maxId = Math.max(rawId1, rawId2);

        String sql = "INSERT INTO friendships (id1, id2, friends_from) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, minId);
            ps.setLong(2, maxId);
            ps.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));

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
            throw new RepositoryException("Database error while saving friendship: " + e.getMessage());
        }
        return entity;
    }

    /**
     * Deletes a friendship entity from the database by its identifier.
     *
     * @param id the identifier of the friendship to delete
     * @return the deleted friendship entity
     * @throws UnsupportedOperationException always thrown, as database persistence is not yet implemented
     */
    @Override
    public Friendship delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }
        Friendship toDelete = find(id)
                .orElseThrow(() -> new RepositoryException("Friendship with ID " + id + " does not exist!"));

        String sql = "DELETE FROM friendships WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
            return toDelete;
        } catch (SQLException e) {
            throw new RepositoryException("Database error while deleting friendship: " + e.getMessage());
        }
    }

    /**
     * Updates an existing friendship entity in the database.
     *
     * @param entity the friendship entity to update
     * @return {@code null} if updated successfully
     * @throws UnsupportedOperationException always thrown, as database persistence is not yet implemented
     */
    @Override
    public Friendship update(Friendship entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        validator.validate(entity);

        if (entity.getID() == null) {
            return entity;
        }

        long minId = Math.min(entity.getFirstFriend().getID(), entity.getSecondFriend().getID());
        long maxId = Math.max(entity.getFirstFriend().getID(), entity.getSecondFriend().getID());

        String sql = "UPDATE friendships SET id1 = ?, id2 = ?, friends_from = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, minId);
            ps.setLong(2, maxId);
            ps.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));
            ps.setLong(4, entity.getID());

            int affectedRows = ps.executeUpdate();
            return (affectedRows > 0) ? null : entity;
        } catch (SQLException e) {
            throw new RepositoryException("Database error while updating friendship: " + e.getMessage());
        }
    }
}