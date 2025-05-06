package com.tld.exception;

import com.tld.dto.ErrorDTO;

public class InvalidJsonFormatException extends RuntimeException {

    private ErrorDTO errorDTO;

    // Constructor sin mensaje ni causa
    public InvalidJsonFormatException() {
        super("Invalid JSON format");
        this.errorDTO = new ErrorDTO("INVALID_JSON_FORMAT", "Invalid JSON format", "The provided JSON is not in a valid format.");
    }

    // Constructor con mensaje personalizado
    public InvalidJsonFormatException(String message) {
        super(message);
        this.errorDTO = new ErrorDTO("INVALID_JSON_FORMAT", message, "The provided JSON is not in a valid format.");
    }

    // Constructor con mensaje, causa y ErrorDTO
    public InvalidJsonFormatException(String message, Throwable cause) {
        super(message, cause);
        this.errorDTO = new ErrorDTO("INVALID_JSON_FORMAT", message, "The provided JSON is not in a valid format.");
    }

    // Constructor con causa y ErrorDTO
    public InvalidJsonFormatException(Throwable cause) {
        super(cause);
        this.errorDTO = new ErrorDTO("INVALID_JSON_FORMAT", "Invalid JSON format", cause.getMessage());
    }

    // Getter para el ErrorDTO
    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }
}
