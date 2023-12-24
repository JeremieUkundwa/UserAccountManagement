package account.mgt.useraccountmanagment.service;

import account.mgt.useraccountmanagment.model.User;

import java.util.List;

public interface UserService {
    User registerUser(User theUser);
    User updateUser(User theUser);
    User voidUser(User theUser);
    User searchById(User theUser);
    List<User> users();
}
