package account.mgt.useraccountmanagment.repository;

import account.mgt.useraccountmanagment.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query("select r from Role  r where r.roleName=:roleName")
    Role findByName(@Param("roleName") String roleName);
}
