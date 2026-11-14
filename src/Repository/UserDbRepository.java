package Repository;

import domain.Entity;
import domain.User;
import domain.Validation.Validator;

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

    /**
     * Finds a user entity by its identifier in the database.
     *
     * @param id the identifier of the user to find
     * @return the user entity if found
     * @throws UnsupportedOperationException always thrown, as database persistence is not yet implemented
     */
    @Override
    public User find(Long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Retrieves all user entities from the database.
     *
     * @return an {@link Iterable} containing all users
     * @throws UnsupportedOperationException always thrown, as database persistence is not yet implemented
     */
    @Override
    public Iterable<User> findAll() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Saves a new user entity to the database.
     *
     * @param entity the user entity to save
     * @return {@code null} if saved successfully
     * @throws UnsupportedOperationException always thrown, as database persistence is not yet implemented
     */
    @Override
    public User save(User entity) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Deletes a user entity from the database by its identifier.
     *
     * @param id the identifier of the user to delete
     * @return the deleted user entity
     * @throws UnsupportedOperationException always thrown, as database persistence is not yet implemented
     */
    @Override
    public User delete(Long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Updates an existing user entity in the database.
     *
     * @param entity the user entity to update
     * @return {@code null} if updated successfully
     * @throws UnsupportedOperationException always thrown, as database persistence is not yet implemented
     */
    @Override
    public User update(User entity) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}