package io.github.nivaldosilva.bff_agendador_tarefas.openapi;

import io.github.nivaldosilva.bff_agendador_tarefas.dto.request.LoginRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.response.LoginResponse;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Usuario", description = "Endpoints para autenticação e gerenciamento de usuarios")
public interface UsuarioAPI {

    @Operation(
            summary = "Realiza login do usuario",
            description = "Autentica um usuario e retorna um token JWT que deve ser usado nas requisições subsequentes. " +
                    "O token expira em 1 hora (3600 segundos)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(
            @Valid @org.springframework.web.bind.annotation.RequestBody LoginRequest loginRequest
    );

    @Operation(
            summary = "Registra um novo usuario",
            description = "Cria uma nova conta de usuario no sistema. Apenas o registro, use o endpoint de login para obter o token de autenticação."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro na requisição",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/registro")
    ResponseEntity<UsuarioResponse> registro(
            @Valid @org.springframework.web.bind.annotation.RequestBody RegistroUsuarioRequest request
    );

    @Operation(
            summary = "Busca o perfil do usuario autenticado",
            description = "Retorna os dados completos do perfil do usuario autenticado, incluindo endereços e telefones cadastrados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil do usuario obtido com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token invalido ou expirado",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/perfil")
    ResponseEntity<UsuarioResponse> getPerfil(
            @Parameter(description = "Token de autenticação (Bearer token)", required = true)
            @RequestHeader("Authorization") String authorizationHeader
    );

}
