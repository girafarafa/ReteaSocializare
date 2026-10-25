package Service;

import Repository.Repository;
import domain.Exceptions.ServiceException;
import domain.Friendship;
import domain.User;

public class FriendshipService {
    private final Repository<Long, User> userRepository;
    private final Repository<Long, Friendship> friendshipRepository;

    public FriendshipService(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    private Long getNextID() {
        Long maxID=0L;
        for (Friendship f : friendshipRepository.findAll()) {
            if (f.getID() > maxID) {
                maxID = f.getID();
            }
        }
        return maxID + 1;
    }

    public void addFriendship(Long idUser1, Long idUser2) {
        User user1=userRepository.find(idUser1);
        User user2=userRepository.find(idUser2);

        if (user1==null) {
            throw new ServiceException("User with ID" + idUser1 + "not found");
        }
        if (user2==null) {
            throw new ServiceException("User with ID" + idUser2 + "not found");
        }

        Friendship newfriendship=new Friendship(user1,user2);

        //verific daca prietenia exista deja
        for (Friendship f:friendshipRepository.findAll()) {
            if(f.equals(newfriendship)) {
                throw new ServiceException("Friendship already exists");
            }
        }

        newfriendship.setID(getNextID());
        friendshipRepository.save(newfriendship);
    }

    public void removeFriendship(Long idUser1, Long idUser2) {
        Long friendshipID=null;
        for (Friendship f:friendshipRepository.findAll()) {
            if ((f.getFirstFriend().getID().equals(idUser1) && f.getSecondFriend().getID().equals(idUser2)) ||
                    (f.getFirstFriend().getID().equals(idUser2) && f.getSecondFriend().getID().equals(idUser1))) {
                friendshipID= f.getID();
                break;
            }
        }

        if (friendshipID==null) {
            throw new ServiceException("User with ID" + idUser1 + "not found");
        }
        friendshipRepository.delete(friendshipID);
    }

    public Iterable<Friendship> getAllFriendships() {
        return friendshipRepository.findAll();
    }

}
