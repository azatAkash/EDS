package com.student.edsbackend.features.user.dal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {

    USER(EnumSet.of(Permission.USER)),
    SUPER_ADMIN(EnumSet.allOf(Permission.class)),
    ADMIN(EnumSet.of(Permission.ADMIN, Permission.MANAGER, Permission.USER)),
    MANAGER(EnumSet.of(Permission.MANAGER, Permission.USER));

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority(this.name()));
        return authorities;
    }
}
