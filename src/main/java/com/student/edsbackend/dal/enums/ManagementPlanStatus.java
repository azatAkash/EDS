package com.student.edsbackend.dal.enums;

/**
 * Enum representing the possible statuses of a management plan.
 */
public enum ManagementPlanStatus {
    /**
     * Management plan has been created but not yet sent for confirmation
     */
    CREATED,
    
    /**
     * Management plan has been sent for confirmation
     */
    SENT_FOR_CONFIRMATION,
    
    /**
     * Management plan has been agreed upon by all parties
     */
    AGREED,
    
    /**
     * Management plan has been refused
     */
    REFUSED
}