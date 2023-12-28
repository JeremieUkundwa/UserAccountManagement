package account.mgt.useraccountmanagment.validation;

import account.mgt.useraccountmanagment.model.User;
import account.mgt.useraccountmanagment.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidation implements Validator {
    private final UserServiceImpl userService;
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    @Autowired
    public UserValidation(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
        User theUser = (User) target;
        if(userService.checkIfUserExist(theUser))
            errors.rejectValue("phoneNumber","theUser.phoneNumber","phone number exists, log in!");
        if(!validatePassword(theUser.getPassword()))
            errors.rejectValue("password","theUser.password","Password must be at least 8 characters long and contain at least one letter and one number.");

    }

    public static boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
