package id.app.asnot.model.entity;

import java.util.Set;

public enum Role {
    ADMIN("ADMIN", Set.of("ADMIN_ACCESS", "READ_MENU",  "EDIT_MENU", "DASHBOARD_VIEW")),
    USER("USER", Set.of("READ_MENU", "DASHBOARD_VIEW")),
    GUEST("GUEST", Set.of("READ_MENU"));


    private final String name;
    private final Set<String> permissions;
    Role(String name, Set<String> permissions) {
        this.name = name;
        this.permissions = permissions;
    }
    public String getName() { return name; }
    public Set<String> getPermissions() { return permissions; }
    public boolean hasPermission(String permission) { return permissions.contains(permission); }
}