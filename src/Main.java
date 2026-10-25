import domain.Friendship;
import domain.User;
import domain.Validation.FriendshipValidator;
import domain.Validation.UserValidator;
import domain.Validation.Validator;
import Repository.FriendshipFileRepository;
import Repository.InMemoryRepository;
import Repository.Repository;
import Repository.UserFileRepository;
import Service.ComunityService;
import Service.FriendshipService;
import Service.UserService;
import ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();

        Repository<Long, User> userRepository;
        Repository<Long, Friendship> friendshipRepository;

        String storageType = "memory";

        if (storageType.equals("file")) {
            userRepository = new UserFileRepository(userValidator, "users.txt");
            friendshipRepository = new FriendshipFileRepository(friendshipValidator, "friendships.txt");
        } else {
            userRepository = new InMemoryRepository<>(userValidator);
            friendshipRepository = new InMemoryRepository<>(friendshipValidator);
        }

        UserService userService = new UserService(userRepository, friendshipRepository);
        FriendshipService friendshipService = new FriendshipService(userRepository, friendshipRepository);
        ComunityService communityService = new ComunityService(userService, friendshipService);

        ConsoleUI console = new ConsoleUI(userService, friendshipService, communityService);
        console.start();
    }
}