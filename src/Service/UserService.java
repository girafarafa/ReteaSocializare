package Service;
import domain.Friendship;
import domain.User;
import domain.Exceptions.ServiceException;
import Repository.Repository;


import java.util.ArrayList; // Import adăugat pentru ArrayList
import java.util.List;

public class UserService {
    private final Repository<Long, User> userRepository;
    private final Repository<Long, Friendship> friendshipRepository;

    public UserService(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }
    private Long getNextID(){
        Long maxId=0L;
        for(User user:userRepository.findAll()){
            if(user.getID()>maxId){
                maxId=user.getID();
            }
        }
        return maxId+1;
    }

    public void addUser(String firstName, String lastName, String email, String password) {
        User user = new User(firstName, lastName, email, password);
        user.setID(getNextID());
        userRepository.save(user);
    }

    public User deleteUser(long id){
        User deleteUser = userRepository.delete(id);
        if(deleteUser==null){
            throw new ServiceException("User not found");
        }
        List<Long> friendshipsToDelete = new ArrayList<>();
        for (Friendship f : friendshipRepository.findAll()) {
            if (f.getFirstFriend().getID().equals(id) || f.getSecondFriend().getID().equals(id)) {
                friendshipsToDelete.add(f.getID());
            }
        }

        for (Long friendshipId : friendshipsToDelete) {
            friendshipRepository.delete(friendshipId);
        }

        return deleteUser;
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(Long id, String firstName, String lastName, String email, String password) {
        User user=new User(firstName, lastName, email, password);
        user.setID(id);
        userRepository.update(user);
    }
}
