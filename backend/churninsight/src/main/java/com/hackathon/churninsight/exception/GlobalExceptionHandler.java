package com.hackathon.churninsight.exception;

import com.hackathon.churninsight.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> details = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                details.put(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        ex.getBindingResult().getGlobalErrors().forEach(error ->
                details.put(
                        error.getObjectName(),
                        error.getDefaultMessage()
                )
        );

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Error de Validación",
                details,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleMalformedJson(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        Map<String, String> details = new HashMap<>();
        details.put("error", "El JSON enviado está mal formado o incompleto");
        details.put(
                "detalle",
                "Verifica la sintaxis, tipos de datos y que no existan valores vacíos"
        );

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "JSON Mal Formado",
                details,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(
            Exception ex,
            HttpServletRequest request
    ) {
        Map<String, String> details = new HashMap<>();
        details.put("exception", ex.getClass().getSimpleName());
        details.put("message", "Ocurrió un error inesperado en el servidor");

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error Interno del Servidor",
                details,
                request.getRequestURI()
        );
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(
            HttpStatus status,
            String error,
            Map<String, ?> details,
            String path
    ) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                error,
                details,
                path
        );

        return ResponseEntity.status(status).body(response);
    }
}
