package domain.Validation;

import domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String errors="";

        if(entity.getFirstName()==null || entity.getFirstName().trim().isEmpty()){
            errors+="First Name is Required";
        }
        if(entity.getLastName()==null || entity.getLastName().trim().isEmpty()){
            errors+="Last Name is Required";
        }
        if(entity.getEmail()==null || entity.getEmail().trim().isEmpty()){
            errors+="Email is Required";
        }else if (!entity.getEmail().contains("@gmail") && !entity.getEmail().contains("@yahoo")) {
            errors += "Email must contain '@gmail' or '@yahoo'! ";
        }

        if (entity.getPassword() == null || entity.getPassword().trim().isEmpty()) {
            errors += "Password cannot be empty! ";
        } else if (entity.getPassword().length() < 6) {
            errors += "Password must be at least 6 characters long! ";
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
