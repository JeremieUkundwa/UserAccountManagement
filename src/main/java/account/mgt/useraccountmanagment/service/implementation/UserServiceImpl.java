package account.mgt.useraccountmanagment.service.implementation;

import account.mgt.useraccountmanagment.model.User;
import account.mgt.useraccountmanagment.repository.UserRepository;
import account.mgt.useraccountmanagment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    @Autowired
    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public User registerUser(User theUser) {
        return repo.save(theUser);
    }

    @Override
    public User updateUser(User theUser) {
        User user = searchById(theUser);
        if(user!=null){
            user.setGender(theUser.getGender());
            user.setStatus(theUser.getStatus());
            user.setFirstName(theUser.getFirstName());
            user.setLastName(theUser.getLastName());
            user.setNationality(theUser.getNationality());
            user.setVerified(theUser.isVerified());
            return repo.save(theUser);
        }
        return null;
    }

    @Override
    public User voidUser(User theUser) {
        User user = searchById(theUser);
        if(user!=null){
            user.setActive(false);
            return repo.save(theUser);
        }
        return null;
    }

    @Override
    public User searchById(User theUser) {
        return repo.findById(theUser.getId()).get();
    }

    @Override
    public List<User> users() {
        return repo.findAll();
    }

    @Override
    public User changePassword(User theUser) {
        User user = searchById(theUser);
        if(user!=null){
            user.setPassword(theUser.getPassword());
            return repo.save(user);
        }
        return null;
    }

    @Override
    public User searchNonValidateUserByPhone(String username) {
        return repo.findNonValidateUser(username);
    }

    @Override
    public User validatedUser(User theUser) {
        User user = searchById(theUser);
        if(user!=null){
            user.setValidated(theUser.isValidated());
            return repo.save(user);
        }
        return null;
    }

    @Override
    public User searchUserByPhone(String phoneNumber) {
        return repo.findByPhoneNumber(phoneNumber);
    }

    @Override
    public User updateOtpUser(User theUser) {
        User user = searchById(theUser);
        if(user !=null){
            user.setOtp(theUser.getOtp());
            return repo.save(user);
        }
        return null;
    }
    @Override
    public boolean checkIfUserExist(User theUser) {
        User user = searchUserByPhone(theUser.getPhoneNumber());
        if(Objects.nonNull(user))
            return true;
        return false;
    }
}
