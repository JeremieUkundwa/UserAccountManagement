package account.mgt.useraccountmanagment.service.implementation;

import account.mgt.useraccountmanagment.model.Role;
import account.mgt.useraccountmanagment.repository.RoleRepository;
import account.mgt.useraccountmanagment.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repo;

    @Autowired
    public RoleServiceImpl(RoleRepository repo) {
        this.repo = repo;
    }

    @Override
    public Role searchRole(Role theRole) {
        return repo.findById(theRole.getId()).get();
    }

    @Override
    public Role searchRoleByName(String name) {
        return repo.findByName(name);
    }
}
