package io.github.nivaldosilva.bff_agendador_tarefas.client;

import io.github.nivaldosilva.bff_agendador_tarefas.dto.request.LoginRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.response.LoginResponse;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.response.UsuarioResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "usuario-client", url = "${microservices.cadastro-usuarios.url}")
public interface UsuarioClient {

    @PostMapping("/usuarios/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest);

    @PostMapping("/usuarios/registro")
    ResponseEntity<UsuarioResponse> registro(@RequestBody RegistroUsuarioRequest request);

    @GetMapping("/usuarios/perfil")
    ResponseEntity<UsuarioResponse> getPerfil(@RequestHeader("Authorization") String authorizationHeader);
}
