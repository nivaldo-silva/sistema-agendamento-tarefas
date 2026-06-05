package io.github.nivaldosilva.bff_agendador_tarefas.entrypoint.controller;

import io.github.nivaldosilva.bff_agendador_tarefas.application.dto.request.CriarTarefaRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.application.dto.response.TarefaResponse;
import io.github.nivaldosilva.bff_agendador_tarefas.entrypoint.docs.TarefaDocs;
import io.github.nivaldosilva.bff_agendador_tarefas.application.service.TarefaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;
@Slf4j

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefaController implements TarefaDocs {

    private final TarefaService tarefaService;

    @Override
    public ResponseEntity<TarefaResponse> criarTarefa(
            @RequestHeader("Authorization") String authorizationHeader,
                @RequestBody CriarTarefaRequest request) {
        return tarefaService.criarTarefa(authorizationHeader, request);
    }

    @Override
    public ResponseEntity<List<TarefaResponse>> listarTarefas(
            @RequestHeader("Authorization") String authorizationHeader,
            LocalDateTime de,
            LocalDateTime ate) {
        return tarefaService.listarTarefas(authorizationHeader, de, ate);
    }

    @Override
    public ResponseEntity<TarefaResponse> buscarTarefa(
            @RequestHeader("Authorization") String authorizationHeader,
            String id) {
        return tarefaService.buscarTarefa(authorizationHeader, id);
    }

    @Override
    public ResponseEntity<Void> deletarTarefa(
            @RequestHeader("Authorization") String authorizationHeader,
            String id) {
        return tarefaService.deletarTarefa(authorizationHeader, id);
    }

    @Override
    public ResponseEntity<TarefaResponse> concluirTarefa(
            @RequestHeader("Authorization") String authorizationHeader,
            String id) {
        log.info("Recebida requisição para concluir tarefa com ID: {}", id);
        ResponseEntity<TarefaResponse> response = tarefaService.concluirTarefa(authorizationHeader, id);
        log.info("Resposta da requisição para concluir tarefa {}: {}", id, response.getStatusCode());
        return response;
    }
}
