package com.rev.revpasswordmanagerp2.exception;

import com.rev.revpasswordmanagerp2.dto.VaultResponseDTO;
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
}
