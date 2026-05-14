package tz.co.divinesolutions.tenants_backend.enums;

public enum ErrorCode {
    // Authentication Errors (1000-1999)
    TOKEN_MISSING("AUTH-1001", "No token found on request sent"),
    TOKEN_EXPIRED("AUTH-1002", "Token time expired please login again"),
    TOKEN_INVALID("AUTH-1003", "Token is changes or invalid"),
    TOKEN_BLACKLISTED("AUTH-1004", "Token blacklisted please login again"),
    INVALID_CREDENTIALS("AUTH-1005", "Invalid user name or password"),
    USER_NOT_FOUND("AUTH-1006", "User not found"),
    ACCOUNT_DISABLED("AUTH-1007", "Account disabled kindly contact system admin"),
    ACCOUNT_LOCKED("AUTH-1008", "Account locked please contact system admin"),
    UNAUTHORIZED("AUTH-1009", "Unauthorized request please login again"),
    
    // Authorization Errors (2000-2999)
    ACCESS_DENIED("AUTH-2001", "Access denied for this request"),
    INSUFFICIENT_PERMISSIONS("AUTH-2002", "You are not authorized to perform this action: %s"),
    ROLE_NOT_FOUND("AUTH-2003", "Role given is not available in the system"),
    PERMISSION_NOT_FOUND("AUTH-2004", "Permission not found"),
    
    // Resource Errors (3000-3999)
    RESOURCE_NOT_FOUND("RES-3001", "Resource not found"),
    RESOURCE_ALREADY_EXISTS("RES-3002", "Resource already exist"),
    
    // Validation Errors (4000-4999)
    VALIDATION_FAILED("VAL-4001", "Data sent could not be validated"),
    INVALID_FORMAT("VAL-4002", "Invalid format"),
    MISSING_FIELD("VAL-4003", "Invalid request data"),
    
    // System Errors (5000-5999)
    INTERNAL_SERVER_ERROR("SYS-5001", "System error kindly try again later"),
    DATABASE_ERROR("SYS-5002", "Database error kindly contact support"),
    
    // Business Logic Errors (6000-6999)
    BUSINESS_RULE_VIOLATION("BIZ-6001", "This request is prohibited for business operations"),
    
    // Endpoint Errors (7000-7999)
    ENDPOINT_NOT_FOUND("ENDP-7001", "End point could not be found");
    
    private final String code;
    private final String defaultMessage;
    
    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDefaultMessage() {
        return defaultMessage;
    }
}