package domain;
import java.util.Objects;
import java.time.LocalDateTime;
public class Friendship extends Entity<Long>{
    private  final User firstFriend;
    private final User secondFriend;
    private final LocalDateTime FriendsFrom;

    public Friendship(User firstFriend, User secondFriend, LocalDateTime FriendsFrom) {
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
        this.FriendsFrom = FriendsFrom;
    }

    public Friendship(User firstFriend, User secondFriend) {
        this(firstFriend, secondFriend, LocalDateTime.now());
    }

    public User getFirstFriend() {
        return firstFriend;
    }
    public User getSecondFriend() {return secondFriend;}
    public LocalDateTime getFriendsFrom() { return FriendsFrom; }

    //functie din clasa Object
    //compara ref de memorie, daca ambele var indica exact spre aceeasi loc
    //am suprascris ca egalitatea sa fie decisa de id, nu de adresa din ram
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return (Objects.equals(firstFriend, that.firstFriend) && Objects.equals(secondFriend, that.secondFriend)) ||
                (Objects.equals(firstFriend, that.secondFriend) && Objects.equals(secondFriend, that.firstFriend));
    }

    //oblg sa implementez si hashcode
    @Override
    public int hashCode() {
        return Objects.hash(firstFriend) + Objects.hash(secondFriend);
    }

    //functie din clasa Object
    //return o reprezentare text formata din numele clasei + adresa de memorie
    //o suprascriu pt a afisa informatiile utile in consola
    @Override
    public String toString() {
        return "Friendship ID: " + getID() +
                " | Friend 1: " + firstFriend.getFirstName() + " " + firstFriend.getLastName() + " (ID: " + firstFriend.getID() + ")" +
                " | Friend 2: " + secondFriend.getFirstName() + " " + secondFriend.getLastName() + " (ID: " + secondFriend.getID() + ")"
                + " | Friends From: " + FriendsFrom;
    }
}
