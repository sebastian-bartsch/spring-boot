package com.tld.exception;

import com.tld.dto.ErrorDTO;

public class DuplicateKeyException extends RuntimeException {

    private ErrorDTO errorDTO;

    // Constructor sin mensaje ni causa
    public DuplicateKeyException() {
        super("Duplicate key error");
        this.errorDTO = new ErrorDTO("DUPLICATE_KEY", "Duplicate key error", "A key already exists in the database.");
    }

    // Constructor con mensaje personalizado
    public DuplicateKeyException(String message) {
        super(message);
        this.errorDTO = new ErrorDTO("DUPLICATE_KEY", message, "A key already exists in the database.");
    }

    // Constructor con mensaje, causa y ErrorDTO
    public DuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
        this.errorDTO = new ErrorDTO("DUPLICATE_KEY", message, "A key already exists in the database.");
    }

    // Constructor con causa y ErrorDTO
    public DuplicateKeyException(Throwable cause) {
        super(cause);
        this.errorDTO = new ErrorDTO("DUPLICATE_KEY", "Duplicate key error", cause.getMessage());
    }

    // Getter para el ErrorDTO
    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }
}
