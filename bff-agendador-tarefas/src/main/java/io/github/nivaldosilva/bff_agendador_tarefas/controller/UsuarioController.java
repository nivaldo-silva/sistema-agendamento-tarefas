package io.github.nivaldosilva.bff_agendador_tarefas.controller;

import io.github.nivaldosilva.bff_agendador_tarefas.dto.request.LoginRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.response.LoginResponse;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.response.UsuarioResponse;
import io.github.nivaldosilva.bff_agendador_tarefas.openapi.UsuarioAPI;
import io.github.nivaldosilva.bff_agendador_tarefas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UsuarioController implements UsuarioAPI {

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
