import domain.Friendship;
import domain.User;
import domain.Validation.FriendshipValidator;
import domain.Validation.UserValidator;
import domain.Validation.Validator;
import Repository.FriendshipFileRepository;
import Repository.InMemoryRepository;
import Repository.Repository;
import Repository.UserFileRepository;
import Repository.RepositoryFactory;
import Service.ComunityService;
import Service.FriendshipService;
import Service.UserService;
import ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();

        // Obtinem fabrica
        RepositoryFactory factory = RepositoryFactory.getInstance();

        // Main NU MAI CUNOAȘTE clasele concrete! (nu există new UserFileRepository)
        Repository<Long, User> userRepository = factory.createUserRepository(userValidator);
        Repository<Long, Friendship> friendshipRepository = factory.createFriendshipRepository(friendshipValidator);

        UserService userService = new UserService(userRepository, friendshipRepository);
        FriendshipService friendshipService = new FriendshipService(userRepository, friendshipRepository);
        ComunityService communityService = new ComunityService(userService, friendshipService);

        ConsoleUI console = new ConsoleUI(userService, friendshipService, communityService);
        console.start();
    }
}