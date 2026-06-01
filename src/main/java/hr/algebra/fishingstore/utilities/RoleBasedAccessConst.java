package hr.algebra.fishingstore.utilities;

public final class RoleBasedAccessConst {
    private RoleBasedAccessConst() {
    }

    public static final String ADMIN_ONLY = "hasRole('ADMIN')";
    public static final String USER_ONLY = "hasRole('USER')";
    public static final String ADMIN_OR_RESOURCE_OWNER = "hasRole('ADMIN') or #id == #userId";
    public static final String AUTHENTICATED = "isAuthenticated()";
}