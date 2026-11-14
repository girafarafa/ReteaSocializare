package Repository;

import domain.Friendship;
import domain.Validation.Validator;

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

    /**
     * Finds a friendship entity by its identifier in the database.
     *
     * @param id the identifier of the friendship to find
     * @return the friendship entity if found
     * @throws UnsupportedOperationException always thrown, as database persistence is not yet implemented
     */
    @Override
    public Friendship find(Long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Retrieves all friendship entities from the database.
     *
     * @return an {@link Iterable} containing all friendships
     * @throws UnsupportedOperationException always thrown, as database persistence is not yet implemented
     */
    @Override
    public Iterable<Friendship> findAll() {
        throw new UnsupportedOperationException("Not implemented yet!");
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
        throw new UnsupportedOperationException("Not implemented yet!");
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
        throw new UnsupportedOperationException("Not implemented yet!");
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
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}