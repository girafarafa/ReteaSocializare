package Service;
import domain.Friendship;
import domain.User;
import domain.Exceptions.ServiceException;
import Repository.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserService {
    private final Repository<Long, User> userRepository;
    private final Repository<Long, Friendship> friendshipRepository;

    public UserService(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }
    private Long getNextID(){
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(User::getID)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    public void addUser(String firstName, String lastName, String email, String password) {
        User user = new User(firstName, lastName, email, password);
        user.setID(getNextID());
        userRepository.save(user);
    }

    public User deleteUser(long id){
        User deletedUser = Optional.ofNullable(userRepository.delete(id))
                .orElseThrow(() -> new ServiceException("User with ID " + id + " not found"));

        StreamSupport.stream(friendshipRepository.findAll().spliterator(), false)
                .filter(f -> f.getFirstFriend().getID().equals(id) || f.getSecondFriend().getID().equals(id))
                .map(Friendship::getID)
                .collect(Collectors.toList())
                .forEach(friendshipRepository::delete);

        return deletedUser;
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUser(Long id) {
        return Optional.ofNullable(userRepository.find(id));
    }
}
