package id.ac.ui.cs.advprog.auth.model.auth;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static id.ac.ui.cs.advprog.auth.model.auth.ApplicationUserPermission.*;


public enum ApplicationUserRole {

    ADMIN(Sets.newHashSet(MAKANAN_CREATE, MAKANAN_READ, MAKANAN_UPDATE, MAKANAN_DELETE, DATAHARIAN_READ_ALL, DATAHARIAN_DELETE)),
    USER(Sets.newHashSet(MAKANAN_READ, DATAHARIAN_CREATE, DATAHARIAN_READ_SELF, DATAHARIAN_UPDATE));


    private final Set<ApplicationUserPermission> permissions;


    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthority() {
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+ this.name()));
        return authorities;
    }
}
