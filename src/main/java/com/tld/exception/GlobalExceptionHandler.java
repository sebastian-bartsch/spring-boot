package com.tld.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tld.dto.ErrorDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja las excepciones de tipo DuplicateKeyException
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorDTO> handleDuplicateKeyException(DuplicateKeyException ex) {
        return new ResponseEntity<>(ex.getErrorDTO(), HttpStatus.CONFLICT);
    }

    // Maneja las excepciones de tipo EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getErrorDTO(), HttpStatus.NOT_FOUND);
    }

    // Maneja las excepciones de tipo InvalidApiKeyException
    @ExceptionHandler(InvalidApiKeyException.class)
    public ResponseEntity<ErrorDTO> handleInvalidApiKeyException(InvalidApiKeyException ex) {
        return new ResponseEntity<>(ex.getErrorDTO(), HttpStatus.UNAUTHORIZED);
    }

    // Maneja las excepciones de tipo InvalidJsonFormatException
    @ExceptionHandler(InvalidJsonFormatException.class)
    public ResponseEntity<ErrorDTO> handleInvalidJsonFormatException(InvalidJsonFormatException ex) {
        return new ResponseEntity<>(ex.getErrorDTO(), HttpStatus.BAD_REQUEST);
    }

    // Maneja las excepciones de tipo InvalidTokenException
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorDTO> handleInvalidTokenException(InvalidTokenException ex) {
        return new ResponseEntity<>(ex.getErrorDTO(), HttpStatus.UNAUTHORIZED);
    }

    // Maneja las excepciones de tipo InvalidUserException
    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ErrorDTO> handleInvalidUserException(InvalidUserException ex) {
        return new ResponseEntity<>(ex.getErrorDTO(), HttpStatus.BAD_REQUEST);
    }

    // Maneja la excepción DataIntegrityViolationException
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorDTO errorDTO = new ErrorDTO("DATA_INTEGRITY_VIOLATION", "Data integrity violation", ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    // Maneja excepciones generales (Throwable)
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDTO> handleThrowable(Throwable ex) {
    	ex.printStackTrace();
        ErrorDTO errorDTO = new ErrorDTO("GENERAL_ERROR", "An unexpected error occurred", ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(InvalidCompanySensorException.class)
    public ResponseEntity<ErrorDTO> handleInvalidCompanySensor(InvalidCompanySensorException ex) {
        return new ResponseEntity<>(ex.getErrorDTO(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity<ErrorDTO> handleMissingParameterException(MissingParameterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorDTO());
    }
    
    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleUniqueConstraintViolationException(UniqueConstraintViolationException ex) {
        return new ResponseEntity<>(ex.getErrorDTO(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomDatabaseException.class)
    public ResponseEntity<ErrorDTO> handleCustomDatabaseException(CustomDatabaseException ex) {
        return new ResponseEntity<>(ex.getErrorDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
