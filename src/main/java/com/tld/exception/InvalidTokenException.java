package com.tld.exception;

import com.tld.dto.ErrorDTO;

public class InvalidTokenException extends RuntimeException {

    private ErrorDTO errorDTO;

    // Constructor sin mensaje ni causa
    public InvalidTokenException() {
        super("Invalid token");
        this.errorDTO = new ErrorDTO("INVALID_TOKEN", "Invalid token", "The provided token is invalid or expired.");
    }

    // Constructor con mensaje personalizado
    public InvalidTokenException(String message) {
        super(message);
        this.errorDTO = new ErrorDTO("INVALID_TOKEN", message, "The provided token is invalid or expired.");
    }

    // Constructor con mensaje, causa y ErrorDTO
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
        this.errorDTO = new ErrorDTO("INVALID_TOKEN", message, "The provided token is invalid or expired.");
    }

    // Constructor con causa y ErrorDTO
    public InvalidTokenException(Throwable cause) {
        super(cause);
        this.errorDTO = new ErrorDTO("INVALID_TOKEN", "Invalid token", cause.getMessage());
    }

    // Getter para el ErrorDTO
    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }
}
