package hr.algebra.fishingstore.model.enums;

public enum Role {
    USER,
    ADMIN;

    public String toAuthority() {
        return "ROLE_" + this.name();
    }
}