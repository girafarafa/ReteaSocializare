package Repository;
import domain.User;
import java.util.function.Predicate;

/**
 * Utility class providing static filtering criteria for User entities.
 * Each static method returns a Predicate<User>.
 */
public class UserCriteria {

    // Private constructor to prevent instantiation of utility class
    private UserCriteria() {}

    /**
     * Predicate filtering users by last name (case-insensitive).
     *
     * @param lastName the target last name
     * @return Predicate<User>
     */
    public static Predicate<User> withLastName(String lastName) {
        return user -> user != null && user.getLastName() != null &&
                user.getLastName().equalsIgnoreCase(lastName);
    }

    /**
     * Predicate filtering users whose first name starts with a given prefix (case-insensitive).
     *
     * @param prefix the target prefix
     * @return Predicate<User>
     */
    public static Predicate<User> firstNameStartsWith(String prefix) {
        return user -> user != null && user.getFirstName() != null &&
                user.getFirstName().toLowerCase().startsWith(prefix.toLowerCase());
    }

}