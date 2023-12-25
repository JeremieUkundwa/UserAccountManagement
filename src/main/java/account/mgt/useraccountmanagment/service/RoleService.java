package account.mgt.useraccountmanagment.service;

import account.mgt.useraccountmanagment.model.Role;

public interface RoleService {
    Role searchRole(Role theRole);
    Role searchRoleByName(String name);
}
