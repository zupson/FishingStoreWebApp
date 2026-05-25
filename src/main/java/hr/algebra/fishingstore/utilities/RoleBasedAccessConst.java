package hr.algebra.fishingstore.utilities;

import hr.algebra.fishingstore.model.enums.Role;


public final class RoleBasedAccessConst {
    private RoleBasedAccessConst() {}

    private static final String HAS_ROLE_OPEN = "hasRole('";
    private static final String CLOSE = "')";
    private static final String RESOURCE_OWNER = " or #id == #userId";


    public static final String ADMIN_ONLY =
            HAS_ROLE_OPEN + Role.ADMIN_ROLE + CLOSE;

    public static final String USER_ONLY =
            HAS_ROLE_OPEN + Role.USER_ROLE + CLOSE;

    public static final String ADMIN_OR_RESOURCE_OWNER =
            HAS_ROLE_OPEN + Role.ADMIN_ROLE + CLOSE + RESOURCE_OWNER;
}