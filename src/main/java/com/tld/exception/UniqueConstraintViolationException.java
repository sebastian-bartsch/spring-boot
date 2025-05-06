package com.tld.exception;

import com.tld.dto.ErrorDTO;

public class UniqueConstraintViolationException extends RuntimeException {
    private final ErrorDTO errorDTO;

    public UniqueConstraintViolationException(String message) {
        super(message);
        this.errorDTO = new ErrorDTO("UNIQUE_CONSTRAINT_VIOLATION", "Valor duplicado", message);
    }

    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }
}
