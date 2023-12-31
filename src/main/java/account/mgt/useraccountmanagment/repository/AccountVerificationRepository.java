package account.mgt.useraccountmanagment.repository;

import account.mgt.useraccountmanagment.model.AccountVerification;
import account.mgt.useraccountmanagment.service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountVerificationRepository extends JpaRepository<AccountVerification,Long> {
    @Query("select a from AccountVerification a where a.id =:id and a.states=3")
    Boolean isVerified(@Param("id") Long id);
    @Query("select a from AccountVerification a where a.user.role.roleName='NORMAL'")
    List<AccountVerification> findAllNormalUser();
}
