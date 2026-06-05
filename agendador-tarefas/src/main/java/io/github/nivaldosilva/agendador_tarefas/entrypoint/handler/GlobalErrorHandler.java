package io.github.nivaldosilva.agendador_tarefas.entrypoint.handler;

import io.github.nivaldosilva.agendador_tarefas.application.exception.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail handleResourceNotFoundException(RecursoNaoEncontradoException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        problemDetail.setTitle("Recurso não encontrado");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Verifique os campos da requisição");
        problemDetail.setTitle("Requisição inválida");
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                problemDetail.setProperty(fieldError.getField(), fieldError.getDefaultMessage()));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
