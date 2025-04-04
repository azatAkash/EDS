package com.student.edsbackend.dal.enums;

/**
 * Enum representing the possible statuses of a user declaration.
 */
public enum UserDeclarationStatus {
    /**
     * Declaration has been created but not yet submitted
     */
    CREATED,
    
    /**
     * Declaration has been sent for approval
     */
    SENT_FOR_APPROVAL,
    
    /**
     * Declaration contains an actual conflict of interest
     */
    ACTUAL_CONFILICT,
    
    /**
     * Declaration contains a perceived conflict of interest
     */
    PERCEIVED_CONFLICT,
    
    /**
     * Declaration contains no conflict of interest
     */
    NO_CONFLICT
}