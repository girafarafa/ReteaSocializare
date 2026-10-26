package Repository;

import domain.Entity;
import domain.User;
import domain.Validation.Validator;

public class UserDbRepository implements Repository<Long, User> {
    private final Validator<User> validator;
    private final String url;
    private final String username;
    private final String password;

    public UserDbRepository(Validator<User> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public User find(Long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Iterable<User> findAll() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public User save(User entity) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public User delete(Long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public User update(User entity) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}