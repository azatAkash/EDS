package com.student.edsbackend.features.enums;

/**
 * Enum representing the types of questions that can be used in declarations.
 */
public enum QuestionType {
    /**
     * Open-ended question type where users can enter free-form responses
     */
    OPEN_ENDED,
    
    /**
     * Yes/No question type where users select either Yes or No
     */
    YES_NO,
    
    /**
     * Agreement question type where users certify they have read and agree
     */
    AGREE
}