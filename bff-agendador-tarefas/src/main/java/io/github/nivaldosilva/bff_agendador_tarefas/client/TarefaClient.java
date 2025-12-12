package io.github.nivaldosilva.bff_agendador_tarefas.client;

import io.github.nivaldosilva.bff_agendador_tarefas.dto.request.CriarTarefaRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.response.TarefaResponse;
import io.github.nivaldosilva.bff_agendador_tarefas.enums.StatusNotificacao;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "tarefa-client", url = "${microservices.agendador-tarefas.url}")
public interface TarefaClient {

    @PostMapping("/tarefas")
    ResponseEntity<TarefaResponse> criarTarefa(@RequestHeader("Authorization") String authorizationHeader, @RequestBody CriarTarefaRequest request);

    @GetMapping("/tarefas")
    ResponseEntity<List<TarefaResponse>> listarTarefas(@RequestHeader("Authorization") String authorizationHeader, 
                                                      @RequestParam(value = "de", required = false) LocalDateTime de,
                                                      @RequestParam(value = "ate", required = false) LocalDateTime ate);

    @GetMapping("/tarefas/{id}")
    ResponseEntity<TarefaResponse> buscarTarefa(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id);

    @DeleteMapping("/tarefas/{id}")
    ResponseEntity<Void> deletarTarefa(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id);
    
    @PatchMapping("/tarefas/{id}/concluir")
    ResponseEntity<TarefaResponse> concluirTarefa(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id);

    @PatchMapping("/tarefas/{id}/status-notificacao/{status}")
    ResponseEntity<Void> alteraStatusNotificacao(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id, @PathVariable("status") StatusNotificacao status);
}
