package com.student.edsbackend.features.enums;

public enum Confirmation {
    CONFIRM("Confirm"),
    DO_NOT_CONFIRM("Do not confirm"),
    YES("Yes"),
    NO("No");

    public final String label;

    Confirmation(String label) {
        this.label = label;
    }
}
