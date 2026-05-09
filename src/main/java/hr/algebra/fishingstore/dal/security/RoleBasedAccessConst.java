package hr.algebra.fishingstore.dal.security;

import hr.algebra.fishingstore.model.enums.Role;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class RoleBasedAccessConst {

    public static final String ADMIN_ONLY =
            "hasRole('" + Role.ADMIN_ROLE + "')";

    public static final String USER_ONLY =
            "hasRole('" + Role.USER_ROLE + "')";

    public static final String ADMIN_OR_RESOURCE_OWNER =
            "hasRole('" + Role.ADMIN_ROLE + "') or #id == #userId";
}