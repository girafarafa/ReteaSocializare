package Repository;

import domain.Friendship;
import domain.Validation.Validator;

public class FriendshipDbRepository implements Repository<Long, Friendship> {
    private final Validator<Friendship> validator;
    private final String url;
    private final String username;
    private final String password;

    public FriendshipDbRepository(Validator<Friendship> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Friendship find(Long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Iterable<Friendship> findAll() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Friendship save(Friendship entity) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Friendship delete(Long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Friendship update(Friendship entity) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}