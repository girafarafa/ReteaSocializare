package Repository;
import domain.Friendship;
import domain.User;
import domain.Validation.Validator;

import java.io.InputStream;
import java.util.Properties;

public class RepositoryFactory {
    private static RepositoryFactory instance;
    private RepositoryType repositoryType;
    private final Properties properties=new Properties();

    private RepositoryFactory() {loadProperties();}
    public static synchronized RepositoryFactory getInstance() {
        if (instance == null) {
            instance = new RepositoryFactory();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Fișierul config.properties nu a fost găsit!");
            }
            properties.load(input);

            String typeStr = properties.getProperty("repository.type");
            if (typeStr == null) {
                throw new IllegalArgumentException("Proprietatea repository.type lipsește din config.properties!");
            }

            try {
                this.repositoryType = RepositoryType.valueOf(typeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Tip de repository necunoscut: " + typeStr);
            }

        } catch (Exception e) {
            throw new RuntimeException("Eroare la încărcarea fișierului de configurare: " + e.getMessage(), e);
        }
    }

    public Repository<Long, User> createUserRepository(Validator<User> validator) {
        switch (repositoryType) {
            case MEMORY:
                return new InMemoryRepository<>(validator);
            case FILE:
                String userFile = properties.getProperty("file.users", "data/users.txt");
                return new UserFileRepository(validator, userFile);
            case DATABASE:
                return new UserDbRepository(
                        validator,
                        properties.getProperty("db.url"),
                        properties.getProperty("db.user"),
                        properties.getProperty("db.password")
                );
            default:
                throw new IllegalArgumentException("Tip necunoscut: " + repositoryType);
        }
    }

    public Repository<Long, Friendship> createFriendshipRepository(Validator<Friendship> validator) {
        switch (repositoryType) {
            case MEMORY:
                return new InMemoryRepository<>(validator);
            case FILE:
                String friendshipFile = properties.getProperty("file.friendships", "data/friendships.txt");
                return new FriendshipFileRepository(validator, friendshipFile);
            case DATABASE:
                return new FriendshipDbRepository(
                        validator,
                        properties.getProperty("db.url"),
                        properties.getProperty("db.user"),
                        properties.getProperty("db.password")
                );
            default:
                throw new IllegalArgumentException("Tip necunoscut: " + repositoryType);
        }
    }
}
