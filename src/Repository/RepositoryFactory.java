package Repository;
import domain.Friendship;
import domain.User;
import domain.Validation.Validator;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public class RepositoryFactory {
    private static RepositoryFactory instance;
    private RepositoryType repositoryType;
    private final Properties properties=new Properties();


    //declararea si initializarea campului
    private final Map<RepositoryType, Function<Validator<User>, Repository<Long, User>>> userRepoCreators = new EnumMap<>(RepositoryType.class);
    private final Map<RepositoryType, Function<Validator<Friendship>, Repository<Long, Friendship>>> friendshipRepoCreators = new EnumMap<>(RepositoryType.class);
    /**
     * Returns the global single instance
     * @return the single instance of {@link RepositoryFactory}
     */

    //aici se face constructorul
    private RepositoryFactory() {
        loadProperties();
        initCreators();
    }

    public static RepositoryFactory getInstance() {
        if (instance == null) {
            instance = new RepositoryFactory();
        }
        return instance;
    }

    private void initCreators() {
        userRepoCreators.put(RepositoryType.MEMORY, InMemoryRepository::new);
        userRepoCreators.put(RepositoryType.FILE,val ->
        {
            String userFile = properties.getProperty("file.users", "data/users.txt");
            return new UserFileRepository(val, userFile);
        });
        userRepoCreators.put(RepositoryType.DATABASE, val -> new UserDbRepository(
                val,
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        ));

        // --- Registru creatori Friendship Repository (folosește Long ca ID) ---
        friendshipRepoCreators.put(RepositoryType.MEMORY, InMemoryRepository::new);
        friendshipRepoCreators.put(RepositoryType.FILE, val -> {
            String friendshipFile = properties.getProperty("file.friendships", "data/friendships.txt");
            return new FriendshipFileRepository(val, friendshipFile);
        });
        friendshipRepoCreators.put(RepositoryType.DATABASE, val -> new FriendshipDbRepository(
                val,
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        ));

    }

    /**
     * @throws RuntimeException  if the configuration file cannot be found or read
     * @throws IllegalArgumentException  if {@code repository.type} is missing or unknown
     */
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Fisierul config.properties nu a fost gasit!");
            }
            properties.load(input);
            String typeStr = properties.getProperty("repository.type");
            if (typeStr == null) {
                throw new IllegalArgumentException("Proprietatea repository.type lipseste din config.properties!");
            }
            try {
                this.repositoryType = RepositoryType.valueOf(typeStr.toUpperCase()); //salveaza tipul de repository
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Tip de repository necunoscut: " + typeStr);
            }
        } catch (Exception e) {
            throw new RuntimeException("Eroare la incarcarea fisierului de configurare: " + e.getMessage(), e);
        }
    }

    /**
     * @param validator the validator instance for {@link User} entities
     * @return a concrete implementation of {@link Repository} for users
     * @throws IllegalArgumentException if the configured repository type is unknown
     */
    public Repository<Long, User> createUserRepository(Validator<User> validator) {
        Function<Validator<User>, Repository<Long, User>> creator = userRepoCreators.get(repositoryType);
        if (creator == null) {
            throw new IllegalArgumentException("Unknown repository type: " + repositoryType);
        }
        //aici se creeaza propriu zis repositoryul
        return creator.apply(validator);
    }

    /**
     * @param validator the validator instance for {@link Friendship} entities
     * @return a concrete implementation of {@link Repository} for friendships
     * @throws IllegalArgumentException if the configured repository type is unknown
     */
    public Repository<Long, Friendship> createFriendshipRepository(Validator<Friendship> validator) {
        Function<Validator<Friendship>, Repository<Long, Friendship>> creator = friendshipRepoCreators.get(repositoryType);
        if (creator == null) {
            throw new IllegalArgumentException("Unknown repository type: " + repositoryType);
        }
        return creator.apply(validator);
    }
}
