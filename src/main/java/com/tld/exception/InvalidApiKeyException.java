package com.tld.exception;

import com.tld.dto.ErrorDTO;

public class InvalidApiKeyException extends RuntimeException {

    private ErrorDTO errorDTO;

    // Constructor sin mensaje ni causa
    public InvalidApiKeyException() {
        super("Invalid API key");
        this.errorDTO = new ErrorDTO("INVALID_API_KEY", "Invalid API key", "The provided API key is invalid.");
    }

    // Constructor con mensaje personalizado
    public InvalidApiKeyException(String message) {
        super(message);
        this.errorDTO = new ErrorDTO("INVALID_API_KEY", message, "La Api Key entregada no es valida.");
    }

    // Constructor con mensaje, causa y ErrorDTO
    public InvalidApiKeyException(String message, Throwable cause) {
        super(message, cause);
        this.errorDTO = new ErrorDTO("INVALID_API_KEY", message, "The provided API key is invalid.");
    }

    // Constructor con causa y ErrorDTO
    public InvalidApiKeyException(Throwable cause) {
        super(cause);
        this.errorDTO = new ErrorDTO("INVALID_API_KEY", "Invalid API key", cause.getMessage());
    }

    // Getter para el ErrorDTO
    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }
}
