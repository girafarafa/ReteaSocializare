package Repository;

import domain.Friendship;
import domain.User;
import domain.Validation.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipFileRepository extends AbstractFileRepository<Long, Friendship>{
    /**
     * @param validator the validator used to validate friendship entities
     * @param fileName  the path to the text file used for persistence
     */
    public FriendshipFileRepository(Validator<Friendship> validator, String fileName) {
        super(validator, fileName);
    }

    /**
     * @param attributes the list of string attributes separated by delimiter
     * @return the parsed {@link Friendship} entity
     * @throws NumberFormatException        if the ID attributes cannot be parsed as {@link Long}
     * @throws java.time.format.DateTimeParseException if the timestamp attribute is not in valid ISO-8601 format
     * @throws IndexOutOfBoundsException    if the attributes list contains fewer elements than expected
     */
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

    /**
     * Converts a {@link Friendship} entity into its semicolon-separated string representation for file storage.
     *
     * @param entity the friendship entity to be formatted
     * @return the formatted string representing the friendship
     */
    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getID() + ";" +
                entity.getFirstFriend().getID() + ";" +
                entity.getSecondFriend().getID() + ";" +
                entity.getFriendsFrom().toString();
    }
}
