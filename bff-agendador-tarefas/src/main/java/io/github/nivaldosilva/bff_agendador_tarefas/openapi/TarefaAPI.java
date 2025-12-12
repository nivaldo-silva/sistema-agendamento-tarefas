package io.github.nivaldosilva.bff_agendador_tarefas.openapi;

import io.github.nivaldosilva.bff_agendador_tarefas.dto.request.CriarTarefaRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.response.TarefaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Tarefas", description = "Endpoints para gerenciamento de tarefas")
public interface TarefaAPI {

    @Operation(
            summary = "Cria uma nova tarefa",
            description = "Cria uma nova tarefa no sistema. Após a criação, um email de notificação é enviado automaticamente para o usuário. " +
                    "A tarefa também será notificada automaticamente 1 hora antes do evento."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso",
                    content = @Content(schema = @Schema(implementation = TarefaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping
    ResponseEntity<TarefaResponse> criarTarefa(
            @Parameter(description = "Token de autenticação (Bearer token)", required = true)
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody CriarTarefaRequest request
    );

    @Operation(
            summary = "Lista todas as tarefas do usuário",
            description = "Retorna todas as tarefas do usuário autenticado. Opcionalmente, pode filtrar por período usando os parâmetros 'de' e 'ate'. " +
                    "Formato das datas: ISO 8601 (ex: 2024-01-15T10:30:00)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefas listadas com sucesso",
                    content = @Content(schema = @Schema(implementation = TarefaResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping
    ResponseEntity<List<TarefaResponse>> listarTarefas(
            @Parameter(description = "Token de autenticação (Bearer token)", required = true)
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "Data inicial do período")
            @RequestParam(required = false) LocalDateTime de,
            @Parameter(description = "Data final do período")
            @RequestParam(required = false) LocalDateTime ate
    );

    @Operation(
            summary = "Busca uma tarefa específica por ID",
            description = "Retorna os detalhes completos de uma tarefa específica. Apenas o dono da tarefa ou um administrador pode acessar."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada",
                    content = @Content(schema = @Schema(implementation = TarefaResponse.class))),

            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/{id}")
    ResponseEntity<TarefaResponse> buscarTarefa(
            @Parameter(description = "Token de autenticação (Bearer token)", required = true)
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "ID da tarefa", required = true)
            @PathVariable String id
    );

    @Operation(
            summary = "Deleta uma tarefa",
            description = "Remove permanentemente uma tarefa do sistema. Apenas o dono da tarefa ou um administrador pode deletar."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada",
                    content = @Content(schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "401", description = "Não autorizado",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarTarefa(
            @Parameter(description = "Token de autenticação (Bearer token)", required = true)
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "ID da tarefa", required = true)
            @PathVariable String id
    );

    @Operation(
            summary = "Marca uma tarefa como concluída",
            description = "Marca uma tarefa como concluída e registra a data de conclusão. Apenas o dono da tarefa ou um administrador pode concluir."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa concluída com sucesso",
                    content = @Content(schema = @Schema(implementation = TarefaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })

    @PutMapping("/{id}/concluir")
    ResponseEntity<TarefaResponse> concluirTarefa(
            @Parameter(description = "Token de autenticação (Bearer token)", required = true)
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "ID da tarefa", required = true)
            @PathVariable(value = "id") String id
    );
}