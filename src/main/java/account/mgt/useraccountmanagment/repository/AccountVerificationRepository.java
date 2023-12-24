package account.mgt.useraccountmanagment.repository;

import account.mgt.useraccountmanagment.model.AccountVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountVerificationRepository extends JpaRepository<AccountVerification,Long> {
    @Query("select a from AccountVerification a where a.id =:#{verification.id} and a.states=3 limit 1")
    Boolean isVerified(@Param("verification") AccountVerification verification);
}
