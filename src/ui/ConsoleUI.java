package ui;

import Service.ComunityService;
import Service.FriendshipService;
import Service.UserService;
import domain.Exceptions.RepositoryException;
import domain.Exceptions.ServiceException;
import domain.Friendship;
import domain.User;
import domain.Validation.ValidationException;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final Scanner scanner = new Scanner(System.in);
    private final ComunityService comunityService;

    public ConsoleUI(UserService userService, FriendshipService friendshipService, ComunityService comunityService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.comunityService = comunityService;
    }

    public void start() {
        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Add User");
            System.out.println("2. Print All Users");
            System.out.println("3. Remove User");
            System.out.println("4. Add Friendship");
            System.out.println("5. Remove Friendship");
            System.out.println("6. View Friendships");
            System.out.println("7. Numbers of Comunities");
            System.out.println("8. Most Sociable Comunity");
            System.out.println("9. Find User by ID");
            System.out.println("10. View Friends of a User");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            String opt = scanner.nextLine();
            switch (opt) {
                case "1" -> addUser();
                case "2" -> displayAllUsers();
                case "3" -> removeUser();
                case "4" -> addFriend();
                case "5" -> removeFriend();
                case "6" -> displayAllFriendships();
                case "7" -> displayNumberOfComunities();
                case "8" -> displayMostSociableComunity();
                case "9" -> findUserById();
                case "10" -> displayFriendsOfUser();
                case "0" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void addUser() {
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        try {
            User savedUser = userService.addUser(firstName, lastName, email, password);
            System.out.println("User added successfully with ID: " + savedUser.getID());
        } catch (RepositoryException ex) {
            System.out.println("Repository error: " + ex.getMessage());
        } catch (ServiceException ex) {
            System.out.println("Service error: " + ex.getMessage());
        } catch (ValidationException ex) {
            System.out.println("Validation error: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
        }
    }

    private void displayAllUsers() {
        System.out.println("\n--- LIST OF USERS ---");
        try {
            Iterable<User> users = userService.getAllUsers();
            if (!users.iterator().hasNext()) {
                System.out.println("No users found.");
                return;
            }
            users.forEach(System.out::println);
        } catch (RepositoryException ex) {
            System.out.println("Repository error: " + ex.getMessage());
        }
    }

    private void removeUser() {
        System.out.print("Enter User ID to remove: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            User deletedUser = userService.deleteUser(id);
            System.out.println("User removed successfully: " + deletedUser);
        } catch (NumberFormatException ex) {
            System.out.println("UI error: ID must be a valid number!");
        } catch (ServiceException | RepositoryException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void addFriend() {
        try {
            System.out.print("Enter First User ID: ");
            Long id1 = Long.parseLong(scanner.nextLine());
            System.out.print("Enter Second User ID: ");
            Long id2 = Long.parseLong(scanner.nextLine());

            friendshipService.addFriendship(id1, id2);
            System.out.println("Friendship added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("UI error: IDs must be valid numbers!");
        } catch (ValidationException | RepositoryException | ServiceException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void removeFriend() {
        try {
            System.out.print("Enter First User ID: ");
            Long id1 = Long.parseLong(scanner.nextLine());
            System.out.print("Enter Second User ID: ");
            Long id2 = Long.parseLong(scanner.nextLine());

            friendshipService.removeFriendship(id1, id2);
            System.out.println("Friendship removed successfully!");
        } catch (NumberFormatException e) {
            System.out.println("UI error: IDs must be valid numbers!");
        } catch (RepositoryException | ServiceException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void displayAllFriendships() {
        System.out.println("\n--- LIST OF FRIENDSHIPS ---");
        try {
            Iterable<Friendship> friendships = friendshipService.getAllFriendships();
            if (!friendships.iterator().hasNext()) {
                System.out.println("No friendships found.");
                return;
            }
            friendships.forEach(System.out::println);
        } catch (RepositoryException ex) {
            System.out.println("Repository error: " + ex.getMessage());
        }
    }

    private void displayNumberOfComunities() {
        try {
            int count = comunityService.getNumberOfCommunities();
            System.out.println("Number of comunities: " + count);
        } catch (RepositoryException ex) {
            System.out.println("Repository error: " + ex.getMessage());
        }
    }

    private void displayMostSociableComunity() {
        try {
            List<Long> comunityUserIds = comunityService.getMostSociableCommunity();
            if (comunityUserIds.isEmpty()) {
                System.out.println("No comunities found.");
                return;
            }
            for (Long userId : comunityUserIds) {
                userService.findUser(userId).ifPresentOrElse(
                        user -> System.out.println("User ID: " + userId + " (" + user.getFirstName() + " " + user.getLastName() + ")"),
                        () -> System.out.println("User ID: " + userId)
                );
            }
        } catch (RepositoryException ex) {
            System.out.println("Repository error: " + ex.getMessage());
        }
    }

    private void findUserById() {
        System.out.print("Enter User ID to search: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            userService.findUser(id).ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("Utilizatorul nu a fost găsit!")
            );
        } catch (NumberFormatException e) {
            System.out.println("UI error: ID must be a valid number!");
        } catch (RepositoryException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void displayFriendsOfUser() {
        System.out.print("Enter User ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            List<User> friends = friendshipService.getFriendsOfUser(id);
            if (friends.isEmpty()) {
                System.out.println("This user has no friends.");
            } else {
                System.out.println("--- FRIENDS ---");
                friends.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("UI error: ID must be a valid number!");
        } catch (ServiceException | RepositoryException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}