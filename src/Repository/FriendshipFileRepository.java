package Repository;

import domain.Friendship;
import domain.User;
import domain.Validation.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipFileRepository extends AbstractFileRepository<Long, Friendship>{

    public FriendshipFileRepository(Validator<Friendship> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    protected Friendship extractEntity(List<String> attributes){
        Long id=Long.parseLong(attributes.get(0));
        Long user1Id=Long.parseLong(attributes.get(1));
        Long user2Id=Long.parseLong(attributes.get(2));
        LocalDateTime FriendsFrom=LocalDateTime.parse(attributes.get(3));

        User user1 = new User("", "", "","");
        user1.setID(user1Id);

        User user2 = new User("", "", "","");
        user2.setID(user2Id);

        Friendship friendship = new Friendship(user1, user2, FriendsFrom);
        friendship.setID(id);
        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getID() + ";" +
                entity.getFirstFriend().getID() + ";" +
                entity.getSecondFriend().getID() + ";" +
                entity.getFriendsFrom().toString();
    }
}
