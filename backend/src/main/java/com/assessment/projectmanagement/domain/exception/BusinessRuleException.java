package com.assessment.projectmanagement.domain.exception;

/**
 * Exception thrown when business rules are violated
 */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
