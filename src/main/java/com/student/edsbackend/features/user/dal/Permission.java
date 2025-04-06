package com.student.edsbackend.features.user.dal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    SUPER_ADMIN("super_admin:all"),
    ADMIN("admin:all"),
    MANAGER("manager:all"),
    USER("user:basic")

    ;

    @Getter
    private final String permission;
}
