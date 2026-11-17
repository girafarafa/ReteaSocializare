package Service;

import Repository.Repository;
import domain.Exceptions.ServiceException;
import domain.Friendship;
import domain.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipService {
    private final Repository<Long, User> userRepository;
    private final Repository<Long, Friendship> friendshipRepository;

    public FriendshipService(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    private Long getNextID() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(User::getID)
                .max(Long::compareTo)
                .orElse(0L)+1;

    }

    public void addFriendship(Long idUser1, Long idUser2) {
        User user1 = userRepository.find(idUser1)
                .orElseThrow(() -> new ServiceException("User with ID " + idUser1 + " not found"));

        User user2 = userRepository.find(idUser2)
                .orElseThrow(() -> new ServiceException("User with ID " + idUser2 + " not found"));

        Friendship newfriendship=new Friendship(user1,user2, LocalDateTime.now());

        //verific daca prietenia exista deja
        boolean exists=StreamSupport.stream(friendshipRepository.findAll().spliterator(),false)
                        .anyMatch(newfriendship::equals);

        if (exists) {
            throw new ServiceException("Friendship already exists");
        }

        newfriendship.setID(getNextID());
        friendshipRepository.save(newfriendship);
    }

    public void removeFriendship(Long idUser1, Long idUser2) {
        Long friendshipID = StreamSupport.stream(friendshipRepository.findAll().spliterator(), false)
                .filter(f -> (f.getFirstFriend().getID().equals(idUser1) && f.getSecondFriend().getID().equals(idUser2)) ||
                        (f.getFirstFriend().getID().equals(idUser2) && f.getSecondFriend().getID().equals(idUser1)))
                .map(Friendship::getID)
                .findFirst()
                .orElseThrow(() -> new ServiceException("Friendship between users " + idUser1 + " and " + idUser2 + " not found"));

        friendshipRepository.delete(friendshipID);
    }

    public List<User> getFriendsOfUser(Long userId) {
        User user = userRepository.find(userId)
                .orElseThrow(() -> new ServiceException("User with ID " + userId + " not found"));

        return StreamSupport.stream(friendshipRepository.findAll().spliterator(), false)
                .filter(f -> f.getFirstFriend().getID().equals(userId) || f.getSecondFriend().getID().equals(userId))
                .map(f -> f.getFirstFriend().getID().equals(userId) ? f.getSecondFriend() : f.getFirstFriend())
                .distinct()
                .sorted(Comparator.comparing(User::getLastName).thenComparing(User::getFirstName))
                .collect(Collectors.toList());
    }

    public Iterable<Friendship> getAllFriendships() {
        return friendshipRepository.findAll();
    }

}
