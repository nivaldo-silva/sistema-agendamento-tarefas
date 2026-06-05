package io.github.nivaldosilva.cadastro_usuarios.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RegistroDuplicadoException extends RuntimeException {

    public RegistroDuplicadoException(String message) {
		super(message);
    }

}
