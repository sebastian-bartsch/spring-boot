package com.tld.exception;

import com.tld.dto.ErrorDTO;

public class InvalidUserException extends RuntimeException {

    private ErrorDTO errorDTO;

    // Constructor sin mensaje ni causa
    public InvalidUserException() {
        super("Invalid user");
        this.errorDTO = new ErrorDTO("INVALID_USER", "Invalid user", "The specified user is invalid or does not exist.");
    }

    // Constructor con mensaje personalizado
    public InvalidUserException(String message) {
        super(message);
        this.errorDTO = new ErrorDTO("INVALID_USER", message, "The specified user is invalid or does not exist.");
    }

    // Constructor con mensaje, causa y ErrorDTO
    public InvalidUserException(String message, Throwable cause) {
        super(message, cause);
        this.errorDTO = new ErrorDTO("INVALID_USER", message, "The specified user is invalid or does not exist.");
    }

    // Constructor con causa y ErrorDTO
    public InvalidUserException(Throwable cause) {
        super(cause);
        this.errorDTO = new ErrorDTO("INVALID_USER", "Invalid user", cause.getMessage());
    }

    // Getter para el ErrorDTO
    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }
}
