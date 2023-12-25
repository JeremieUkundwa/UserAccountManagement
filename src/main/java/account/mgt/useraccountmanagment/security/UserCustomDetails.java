package account.mgt.useraccountmanagment.security;


import account.mgt.useraccountmanagment.model.Role;
import account.mgt.useraccountmanagment.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class UserCustomDetails implements UserDetails {
    private User theUser;

    public UserCustomDetails(User theUser) {
        this.theUser = theUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = theUser.getRole();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(role!=null){
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return theUser.getPassword();
    }

    @Override
    public String getUsername() {
        return theUser.getPhoneNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return theUser.isActive();
    }

    public String getNames(){
        return theUser.getFirstName()+" "+theUser.getLastName();
    }
    public Long getCustomer_id(){
        return theUser.getId();
    }

    public User getUser() {
        return theUser;
    }
    public boolean hasRole(String roleName) {
        return this.theUser.hasRole(roleName);
    }
}
