package com.example.spotify.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleException {

    @ExceptionHandler(SongNotFoundException.class)
    public ResponseEntity<ExceptionDto> songNotFound(SongNotFoundException e) {
        ExceptionDto exceptionDTO = new ExceptionDto(e.getMessage()+e.getCause().toString(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(exceptionDTO,exceptionDTO.status());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> userNotFound(UserNotFoundException e) {
        ExceptionDto exceptionDTO = new ExceptionDto(e.getMessage()+e.getCause().toString(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(exceptionDTO,exceptionDTO.status());
    }
}
