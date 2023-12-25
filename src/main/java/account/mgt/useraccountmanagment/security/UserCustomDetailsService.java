package account.mgt.useraccountmanagment.security;

import account.mgt.useraccountmanagment.model.User;
import account.mgt.useraccountmanagment.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserCustomDetailsService implements UserDetailsService {
    @Autowired private UserRepository repo;
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user  = new User();
        if(phone != null){
            System.out.println("AM IN !!!!!!!!!>>>> Security");
            user = repo.findByPhoneNumber(phone);

        }
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
        return new UserCustomDetails(user);
    }
}
