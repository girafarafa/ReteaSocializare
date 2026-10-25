package Repository;

import domain.User;
import domain.Validation.Validator;

import java.io.*;
import java.util.List;

public class UserFileRepository extends AbstractFileRepository<Long, User>{

    public UserFileRepository(Validator<User> validator, String fileName) {
        super(validator, fileName);
    }

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

    @Override
    protected String createEntityAsString(User entity) {
        return entity.getID() + ";" +
                entity.getFirstName() + ";" +
                entity.getLastName() + ";" +
                entity.getEmail() + ";" +
                entity.getPassword();
    }




}
