import domain.Friendship;
import domain.User;
import domain.Validation.FriendshipValidator;
import domain.Validation.UserValidator;
import domain.Validation.ValidationException;
import domain.Validation.Validator;
import Repository.Repository;
import Repository.RepositoryFactory;
import Repository.UserCriteria;
import Service.ComunityService;
import Service.FriendshipService;
import Service.UserService;
import ui.ConsoleUI;

import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {

        //face verificare in respository daca fabrica exista deja
        //daca nu o construieste si ii zice setarile
        RepositoryFactory factory = RepositoryFactory.getInstance();

        //Validatorul de baza
        Validator<User> baseUserValidator = new UserValidator();

        //Regula de validare ca numele sa inceapa cu majuscula
        Validator<User> capitalLetterValidator = user -> {
            if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
                if (!Character.isUpperCase(user.getFirstName().charAt(0))) {
                    throw new ValidationException("First name must start with an uppercase letter!");
                }
            }
        };

        /*Validator<User> startsWithAValidator = user -> {
            if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
                if (user.getFirstName().charAt(0) != 'A') {
                    throw new ValidationException("First name must start with the letter 'A'!");
                }
            }4
        };*/

        //am combinat cu andthen()
        Validator<User> combinedUserValidator = baseUserValidator.andThen(capitalLetterValidator);
        //Validator<User> combinedUserValidator = baseUserValidator.andThen(startsWithAValidator);
        //am trimis validatorul combinat mai departe catre repository
        Validator<Friendship> friendshipValidator = new FriendshipValidator();

        Repository<Long, User> userRepository = factory.createUserRepository(combinedUserValidator);

        System.out.println("--- Users starting with 'A' ---");
        // For Lab 4: Filter using static criteria
        userRepository.findAll(UserCriteria.firstNameStartsWith("A"))
                .forEach((User u) -> System.out.println(u));

        System.out.println("\n--- Users named Popescu but not starting with 'I' ---");
        // For Lab 4: Filter combining criteria with .and() and Predicate.not()
        userRepository.findAll(UserCriteria.withLastName("Popescu")
                        .and(Predicate.not(UserCriteria.firstNameStartsWith("I"))))
                .forEach((User u) -> System.out.println(u));

        Repository<Long, Friendship> friendshipRepository = factory.createFriendshipRepository(friendshipValidator);

        UserService userService = new UserService(userRepository, friendshipRepository);
        FriendshipService friendshipService = new FriendshipService(userRepository, friendshipRepository);
        ComunityService communityService = new ComunityService(userService, friendshipService);

        ConsoleUI console = new ConsoleUI(userService, friendshipService, communityService);
        console.start();
    }
}