package com.student.edsbackend.features.user.dal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    SUPER_ADMIN("SUPER_ADMIN"),
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    USER("USER");

    @Getter
    private final String permission;

    @Override
    public String toString() {
        return permission;
    }
}
