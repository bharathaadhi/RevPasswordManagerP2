package com.rev.revpasswordmanagerp2.exception;

import com.rev.revpasswordmanagerp2.dto.VaultResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PasswordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public VaultResponseDTO handlePasswordNotFound(PasswordNotFoundException ex) {

        return new VaultResponseDTO(
                "Error: " + ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public VaultResponseDTO handleGeneric(Exception ex) {

        return new VaultResponseDTO(
                "Something went wrong: " + ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public VaultResponseDTO handleBadRequest(BadRequestException ex) {

        return new VaultResponseDTO(
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public VaultResponseDTO handleDuplicate(
            DataIntegrityViolationException ex) {

        return new VaultResponseDTO(
                "Username or Email already exists",
                null
        );
    }
}
