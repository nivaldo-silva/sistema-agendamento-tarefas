package io.github.nivaldosilva.bff_agendador_tarefas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ProblemDetail> handleFeignException(FeignException ex) {
        log.error("FeignException capturada - Status: {}, URL: {}, Mensagem: {}", ex.status(), ex.getMessage(), ex.getMessage());

        try {
            if (ex.responseBody().isPresent()) {
                String responseBody = new String(ex.responseBody().get().array());
                log.error("Erro retornado pelo serviço downstream: {}", responseBody);
                ProblemDetail problemDetail = objectMapper.readValue(responseBody, ProblemDetail.class);
                return ResponseEntity.status(ex.status()).body(problemDetail);
            }
        } catch (IOException e) {
            log.error("Erro ao desserializar o corpo da resposta do Feign: {}", e.getMessage(), e);
            return createGenericFeignProblem(ex.status(), "Erro de comunicação com um serviço interno: " + ex.getMessage());
        }
        return createGenericFeignProblem(ex.status(), "Erro de comunicação com um serviço interno: " + ex.getMessage());
    }

    private ResponseEntity<ProblemDetail> createGenericFeignProblem(int status, String detail) {
        HttpStatus httpStatus = HttpStatus.resolve(status);
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(httpStatus, detail);
        problem.setTitle("Erro de Serviço");
        problem.setType(URI.create("/errors/service-communication-error"));
        return ResponseEntity.status(httpStatus).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados da requisição inválidos.");
        problem.setTitle("Erro de Validação");
        problem.setType(URI.create("/errors/validation-error"));
        problem.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problem.setTitle("Acesso Negado");
        problem.setType(URI.create("/errors/access-denied"));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado no servidor.");
        problem.setTitle("Erro Interno do Servidor");
        problem.setType(URI.create("/errors/internal-server-error"));
        problem.setProperty("causa", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }
}
