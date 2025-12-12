package io.github.nivaldosilva.bff_agendador_tarefas.service;

import io.github.nivaldosilva.bff_agendador_tarefas.client.UsuarioClient;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.request.LoginRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.response.LoginResponse;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.response.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioClient usuarioClient;

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        return usuarioClient.login(loginRequest);
    }

    public ResponseEntity<UsuarioResponse> registro(RegistroUsuarioRequest request) {
        return usuarioClient.registro(request);
    }

    public ResponseEntity<UsuarioResponse> getPerfil(String authorizationHeader) {
        return usuarioClient.getPerfil(authorizationHeader);
    }
}
