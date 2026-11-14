package Repository;

import domain.User;
import domain.Validation.Validator;

import java.io.*;
import java.util.List;

public class UserFileRepository extends AbstractFileRepository<Long, User>{

    /**
     * Constructs a {@code UserFileRepository} with the specified validator and file path.
     *
     * @param validator the validator used to validate user entities
     * @param fileName  the path to the text file used for persistence
     */
    public UserFileRepository(Validator<User> validator, String fileName) {
        super(validator, fileName);
    }

    /**
     * Extracts a {@link User} entity from a list of string attributes parsed from a file line.
     *
     * @param attributes the list of string attributes separated by delimiter
     * @return the parsed {@link User} entity
     * @throws NumberFormatException     if the ID attribute cannot be parsed as {@link Long}
     * @throws IndexOutOfBoundsException if the attributes list contains fewer elements than expected
     */
    @Override
    protected User extractEntity(List<String> attributes){
        Long id=Long.parseLong(attributes.get(0));
        String firstName=attributes.get(1);
        String lastName=attributes.get(2);
        String email=attributes.get(3);
        String password=attributes.get(4);

        User user= new User(firstName,lastName,email,password);
        user.setID(id);
        return user;
    }

    /**
     * Converts a {@link User} entity into its semicolon-separated string representation for file storage.
     *
     * @param entity the user entity to be formatted
     * @return the formatted string representing the user
     */
    @Override
    protected String createEntityAsString(User entity) {
        return entity.getID() + ";" +
                entity.getFirstName() + ";" +
                entity.getLastName() + ";" +
                entity.getEmail() + ";" +
                entity.getPassword();
    }




}
