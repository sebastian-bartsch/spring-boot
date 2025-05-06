package com.tld.exception;

import com.tld.dto.ErrorDTO;

public class InvalidCompanySensorException extends RuntimeException {
	
    private ErrorDTO errorDTO;

    // Constructor sin mensaje ni causa
    public InvalidCompanySensorException() {
        super("Invalid user");
        this.errorDTO = new ErrorDTO("INVALID_COMPANY_SENSOR_KEY", "Comapny y Sensor invalido", "Combinacion entre compania y sensor no existe.");
    }

    // Constructor con mensaje personalizado
    public InvalidCompanySensorException(String message) {
        super(message);
        this.errorDTO = new ErrorDTO("INVALID_COMPANY_SENSOR_KEY", message, "Combinacion entre compania y sensor no existe.");
    }

    // Constructor con mensaje, causa y ErrorDTO
    public InvalidCompanySensorException(String message, Throwable cause) {
        super(message, cause);
        this.errorDTO = new ErrorDTO("INVALID_COMPANY_SENSOR_KEY", message, "Combinacion entre compania y sensor no existe.");
    }

    // Constructor con causa y ErrorDTO
    public InvalidCompanySensorException(Throwable cause) {
        super(cause);
        this.errorDTO = new ErrorDTO("INVALID_COMPANY_SENSOR_KEY", "Invalid api key", cause.getMessage());
    }

    // Getter para el ErrorDTO
    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }

}
