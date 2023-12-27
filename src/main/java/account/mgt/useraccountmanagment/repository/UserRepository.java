package account.mgt.useraccountmanagment.repository;

import account.mgt.useraccountmanagment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("select u from User u where u.validated=false and u.phoneNumber=:username")
    User findNonValidateUser(@Param("username") String username);

    @Query("select u from User u where u.phoneNumber=:phone")
    User findByPhoneNumber(@Param("phone") String phone);

//    @Query("select u from User u where u")
//    User findUserByPhone(String phoneNumber);
}
