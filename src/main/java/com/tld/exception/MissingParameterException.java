package com.tld.exception;

import com.tld.dto.ErrorDTO;

public class MissingParameterException extends RuntimeException {

    private final ErrorDTO errorDTO;

    // Constructor sin parámetros, usa un mensaje genérico
    public MissingParameterException() {
        super("Faltan parámetros requeridos.");
        this.errorDTO = new ErrorDTO(
            "MISSING_PARAMETER",
            "Faltan parámetros requeridos.",
            "Uno o más parámetros necesarios no fueron proporcionados."
        );
    }

    // Constructor con mensaje personalizado
    public MissingParameterException(String message) {
        super(message);
        this.errorDTO = new ErrorDTO(
            "MISSING_PARAMETER",
            message,
            "Uno o más parámetros necesarios no fueron proporcionados."
        );
    }

    // Constructor con mensaje y causa
    public MissingParameterException(String message, Throwable cause) {
        super(message, cause);
        this.errorDTO = new ErrorDTO(
            "MISSING_PARAMETER",
            message,
            cause.getMessage()
        );
    }

    // Constructor con solo la causa
    public MissingParameterException(Throwable cause) {
        super(cause);
        this.errorDTO = new ErrorDTO(
            "MISSING_PARAMETER",
            "Faltan parámetros requeridos.",
            cause.getMessage()
        );
    }

    // Getter para el ErrorDTO
    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }
}
