package com.example.spotify.exception;

import org.springframework.http.HttpStatus;

public record ExceptionDto(String message, HttpStatus status){
}
