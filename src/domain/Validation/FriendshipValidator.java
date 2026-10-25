package domain.Validation;

import domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if (entity.getFirstFriend() == null || entity.getSecondFriend() == null) {
            throw new ValidationException("Friends cannot be null!");
        }
        if (entity.getFirstFriend().getID().equals(entity.getSecondFriend().getID())) {
            throw new ValidationException("A user cannot add themselves as a friend!");
        }
    }
}
