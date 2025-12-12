package io.github.nivaldosilva.agendador_tarefas.controller;

import io.github.nivaldosilva.agendador_tarefas.dto.request.AtualizarParcialTarefaRequest;
import io.github.nivaldosilva.agendador_tarefas.dto.request.AtualizarTarefaRequest;
import io.github.nivaldosilva.agendador_tarefas.dto.request.CriarTarefaRequest;
import io.github.nivaldosilva.agendador_tarefas.dto.response.TarefaResponse;
import io.github.nivaldosilva.agendador_tarefas.dto.response.UsuarioResponse;
import io.github.nivaldosilva.agendador_tarefas.enums.StatusNotificacao;
import io.github.nivaldosilva.agendador_tarefas.service.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@Slf4j
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    public ResponseEntity<TarefaResponse> criarTarefa(@RequestBody @Valid CriarTarefaRequest request) {
        var response = tarefaService.criarNovaTarefa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    public ResponseEntity<List<TarefaResponse>> listarTarefas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime de,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ate) {
        var response = tarefaService.listarTarefas(null, de, ate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or @tarefaSecurity.isOwner(authentication, #id)")
    public ResponseEntity<TarefaResponse> buscarTarefa(@PathVariable String id) {
        var response = tarefaService.buscarTarefaPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/por-email/{email}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<TarefaResponse>> listarTarefasPorEmail(
            @PathVariable String email,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime de,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ate) {
        var response = tarefaService.listarTarefas(email, de, ate);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or @tarefaSecurity.isOwner(authentication, #id)")
    public ResponseEntity<TarefaResponse> atualizarTarefa(@PathVariable String id, @RequestBody @Valid AtualizarTarefaRequest request) {
        var response = tarefaService.atualizarTarefa(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or @tarefaSecurity.isOwner(authentication, #id)")
    public ResponseEntity<TarefaResponse> atualizarParcialmenteTarefa(@PathVariable String id, @RequestBody @Valid AtualizarParcialTarefaRequest request) {
        var response = tarefaService.atualizarParcialmenteTarefa(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or @tarefaSecurity.isOwner(authentication, #id)")
    public ResponseEntity<Void> deletarTarefa(@PathVariable String id) {
        tarefaService.deletarTarefa(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/concluir")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or @tarefaSecurity.isOwner(authentication, #id)")
    public ResponseEntity<TarefaResponse> concluirTarefa(@PathVariable String id) {
        var response = tarefaService.concluirTarefa(id);
        log.info("Retornando response do controller para tarefa ID {}: {}", id, response != null ? "não nulo" : "nulo");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status-notificacao/{status}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> atualizarStatusNotificacao(
            @PathVariable String id,
            @PathVariable StatusNotificacao status) {
        tarefaService.atualizarStatusNotificacao(id, status);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/usuario")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or @tarefaSecurity.isOwner(authentication, #id)")
    public ResponseEntity<UsuarioResponse> buscarUsuarioDaTarefa(@PathVariable String id) {
        var response = tarefaService.buscarDetalhesDoUsuarioDaTarefa(id);
        return ResponseEntity.ok(response);
    }
}
