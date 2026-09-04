package com.schibilinski.projeto.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.schibilinski.projeto.dto.response.ErroResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroResponse> handleNotFound(
        EntityNotFoundException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(TransicaoStatusInvalidaException.class)
    public ResponseEntity<ErroResponse> handleConflict(
        TransicaoStatusInvalidaException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.CONFLICT, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler({RegraDeNegocioException.class, IllegalArgumentException.class})
    public ResponseEntity<ErroResponse> handleBadRequest(
        RuntimeException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidation(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        return buildResponse(
            HttpStatus.BAD_REQUEST,
            "Existem campos inválidos",
            request,
            campos
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponse> handleConstraintViolation(
        ConstraintViolationException exception,
        HttpServletRequest request
    ) {
        Map<String, String> campos = new LinkedHashMap<>();
        exception.getConstraintViolations().forEach(violation ->
            campos.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        return buildResponse(
            HttpStatus.BAD_REQUEST,
            "Existem parâmetros inválidos",
            request,
            campos
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> handleTypeMismatch(
        MethodArgumentTypeMismatchException exception,
        HttpServletRequest request
    ) {
        return buildResponse(
            HttpStatus.BAD_REQUEST,
            "Valor inválido para o parâmetro: " + exception.getName(),
            request,
            Map.of()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleUnreadableMessage(
        HttpMessageNotReadableException exception,
        HttpServletRequest request
    ) {
        return buildResponse(
            HttpStatus.BAD_REQUEST,
            "O corpo da requisição está ausente ou possui formato inválido",
            request,
            Map.of()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGeneric(
        Exception exception,
        HttpServletRequest request
    ) {
        log.error("Erro inesperado ao processar {}", request.getRequestURI(), exception);
        return buildResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro interno do servidor",
            request,
            Map.of()
        );
    }

    private ResponseEntity<ErroResponse> buildResponse(
        HttpStatus status,
        String mensagem,
        HttpServletRequest request,
        Map<String, String> campos
    ) {
        ErroResponse body = new ErroResponse(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            mensagem,
            request.getRequestURI(),
            campos
        );
        return ResponseEntity.status(status).body(body);
    }
}
