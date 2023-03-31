package id.ac.ui.cs.advprog.auth.model.auth;

public enum ApplicationUserPermission {
    MAKANAN_READ("makanan:read"),
    MAKANAN_CREATE("makanan:create"),
    MAKANAN_UPDATE("makanan:update"),
    MAKANAN_DELETE("makanan:delete"),
    DATAHARIAN_READ_ALL("dataharian:read_all"),
    DATAHARIAN_READ_SELF("dataharian:read_self"),
    DATAHARIAN_CREATE("dataharian:create"),
    DATAHARIAN_UPDATE("dataharian:update"),
    DATAHARIAN_DELETE("dataharian:delete");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
