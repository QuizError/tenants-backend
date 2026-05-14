package tz.co.divinesolutions.tenants_backend.exceptions;

import tz.co.divinesolutions.tenants_backend.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object[] args;
    private final int status;
    
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.args = null;
        this.status = determineStatus(errorCode);
    }
    
    public CustomException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.args = null;
        this.status = determineStatus(errorCode);
    }
    
    public CustomException(ErrorCode errorCode, Object... args) {
        super(String.format(errorCode.getDefaultMessage(), args));
        this.errorCode = errorCode;
        this.args = args;
        this.status = determineStatus(errorCode);
    }
    
    private int determineStatus(ErrorCode errorCode) {
        String code = errorCode.getCode();
        if (code.startsWith("AUTH")) {
            if (code.equals("AUTH-1002") || code.equals("AUTH-1004")) {
                return 401; // Unauthorized
            }
            if (code.startsWith("AUTH-100")) {
                return 401;
            }
            return 403;
        }
        if (code.startsWith("AUTH-2")) return 403;
        if (code.startsWith("RES-3001")) return 404;
        if (code.startsWith("RES-3002")) return 409;
        if (code.startsWith("VAL-4")) return 400;
        if (code.startsWith("ENDP-7")) return 404;
        return 500;
    }
}