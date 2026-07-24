package com.example.demo.exeption;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ErrorCampoDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidacion(MethodArgumentNotValidException ex) {
        List<ErrorCampoDTO> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> ErrorCampoDTO.builder()
                        .campo(fe.getField())
                        .mensaje(fe.getDefaultMessage())
                        .build())
                .toList();

        return ResponseEntity.badRequest().body(
                ApiResponse.error("Error de validacion en los datos enviados", errores)
        );
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error(ex.getMessage())
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleCredencialesInvalidas(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.error("Credenciales invalidas")
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccesoDenegado(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ApiResponse.error("No tiene permisos suficientes para realizar esta accion")
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenerico(Exception ex, HttpServletRequest req) {
        log.error("Error no controlado en {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(
                ApiResponse.error("Ocurrio un error interno en el servidor")
        );
    }
}
