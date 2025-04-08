package com.student.edsbackend.features.user.dal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    SUPER_ADMIN("super_admin:max"),
    ADMIN("admin:pro"),
    MANAGER("manager:plus"),
    USER("user:basic");

    @Getter
    private final String permission;

    @Override
    public String toString() {
        return permission;
    }
}
