package com.tld.exception;

import com.tld.dto.ErrorDTO;

public class CustomDatabaseException extends RuntimeException {
    private final ErrorDTO errorDTO;

    public CustomDatabaseException(String message, Throwable cause) {
        super(message, cause);
        this.errorDTO = new ErrorDTO("DATABASE_ERROR", "Database operation failed", message);
    }
    
    // Constructor sin mensaje ni causa
    public CustomDatabaseException(String message) {
        super("Registro inactivo");
        this.errorDTO = new ErrorDTO("REGISTRO_INACTIVO", "Registro inactivo ", message);
    }

    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }
}
