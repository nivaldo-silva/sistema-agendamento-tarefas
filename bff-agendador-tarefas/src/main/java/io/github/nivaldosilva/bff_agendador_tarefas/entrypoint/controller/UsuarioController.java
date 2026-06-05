package io.github.nivaldosilva.bff_agendador_tarefas.entrypoint.controller;

import io.github.nivaldosilva.bff_agendador_tarefas.application.dto.request.LoginRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.application.dto.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.application.dto.response.LoginResponse;
import io.github.nivaldosilva.bff_agendador_tarefas.application.dto.response.UsuarioResponse;
import io.github.nivaldosilva.bff_agendador_tarefas.entrypoint.docs.UsuarioDocs;
import io.github.nivaldosilva.bff_agendador_tarefas.application.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UsuarioController implements UsuarioDocs {

    private final UsuarioService usuarioService;

    @Override
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return usuarioService.login(loginRequest);
    }

    @Override
    public ResponseEntity<UsuarioResponse> registro(@RequestBody RegistroUsuarioRequest request) {
        return usuarioService.registro(request);
    }

    @Override
    public ResponseEntity<UsuarioResponse> getPerfil(@RequestHeader("Authorization") String authorizationHeader) {
        return usuarioService.getPerfil(authorizationHeader);
    }
}
