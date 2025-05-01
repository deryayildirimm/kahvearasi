package com.dailyreader.daily_reader.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        return buildErrorResponse(
                "Beklenmeyen bir hata oluştu",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }



    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus httpStatus, WebRequest webRequest) {

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                httpStatus.value(),
                message,
                // false  -> client bilgisi dahil edilmesin
                webRequest.getDescription(false)
        );

        return new ResponseEntity<>(response, httpStatus);
    }


}
