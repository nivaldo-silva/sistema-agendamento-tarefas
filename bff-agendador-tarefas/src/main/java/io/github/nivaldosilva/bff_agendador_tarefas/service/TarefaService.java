package io.github.nivaldosilva.bff_agendador_tarefas.service;

import io.github.nivaldosilva.bff_agendador_tarefas.client.NotificacaoClient;
import io.github.nivaldosilva.bff_agendador_tarefas.client.TarefaClient;
import io.github.nivaldosilva.bff_agendador_tarefas.client.UsuarioClient;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.request.CriarTarefaRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.request.NotificacaoRequest;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.response.TarefaResponse;
import io.github.nivaldosilva.bff_agendador_tarefas.dto.response.UsuarioResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaClient tarefaClient;
    private final UsuarioClient usuarioClient;
    private final NotificacaoClient notificacaoClient;

    private static final DateTimeFormatter NOTIFICATION_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @CircuitBreaker(name = "tarefas", fallbackMethod = "fallbackCriarTarefa")
    public ResponseEntity<TarefaResponse> criarTarefa(String authorizationHeader, CriarTarefaRequest request) {
        ResponseEntity<TarefaResponse> response = tarefaClient.criarTarefa(authorizationHeader, request);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            enviarNotificacaoAsync(authorizationHeader, response.getBody());
        }
        return response;
    }

    public void enviarNotificacaoAsync(String authorizationHeader, TarefaResponse tarefa) {
        try {
            ResponseEntity<UsuarioResponse> usuarioResponse = usuarioClient.getPerfil(authorizationHeader);

            if (usuarioResponse.getBody() != null) {
                UsuarioResponse usuario = usuarioResponse.getBody();

                String dataEventoFormatada = tarefa.dataEvento() != null
                        ? tarefa.dataEvento().format(NOTIFICATION_DATE_FORMATTER)
                        : LocalDateTime.now().format(NOTIFICATION_DATE_FORMATTER);

                NotificacaoRequest notificacao = new NotificacaoRequest(
                        usuario != null && usuario.email() != null ? usuario.email() : "nivaldoluizadm@gmail.com",
                        tarefa.titulo(),
                        tarefa.descricao(),
                        dataEventoFormatada,
                        tarefa.prioridade() != null ? tarefa.prioridade().toString() : "MEDIA"
                );

                notificacaoClient.enviarNotificacao("true", notificacao);
                log.info("Notificação enviada com sucesso para: {}", usuario != null ? usuario.email() : "email padrão");
            }
        } catch (Exception e) {
            log.error("Erro ao enviar notificação: {}", e.getMessage(), e);
        }
    }

    @CircuitBreaker(name = "tarefas", fallbackMethod = "fallbackListarTarefas")
    public ResponseEntity<List<TarefaResponse>> listarTarefas(String authorizationHeader, LocalDateTime de, LocalDateTime ate) {
        return tarefaClient.listarTarefas(authorizationHeader, de, ate);
    }

    @CircuitBreaker(name = "tarefas", fallbackMethod = "fallbackBuscarTarefa")
    public ResponseEntity<TarefaResponse> buscarTarefa(String authorizationHeader, String id) {
        return tarefaClient.buscarTarefa(authorizationHeader, id);
    }

    @CircuitBreaker(name = "tarefas", fallbackMethod = "fallbackDeletarTarefa")
    public ResponseEntity<Void> deletarTarefa(String authorizationHeader, String id) {
        return tarefaClient.deletarTarefa(authorizationHeader, id);
    }


    @CircuitBreaker(name = "tarefas", fallbackMethod = "fallbackConcluirTarefa")
    public ResponseEntity<TarefaResponse> concluirTarefa(String authorizationHeader, String id) {
        log.info("Chamando client para concluir tarefa com ID: {}", id);
        ResponseEntity<TarefaResponse> response = null;
        try {
            response = tarefaClient.concluirTarefa(authorizationHeader, id);
            log.info("Cliente retornou resposta com status: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("Erro ao chamar client para concluir tarefa {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public ResponseEntity<TarefaResponse> fallbackCriarTarefa(String authorizationHeader, CriarTarefaRequest request, Throwable t) {
        log.error("Fallback acionado ao criar tarefa: {}", t.getMessage(), t);
        return ResponseEntity.status(503).body(null);
    }

    public ResponseEntity<List<TarefaResponse>> fallbackListarTarefas(String authorizationHeader, LocalDateTime de, LocalDateTime ate, Throwable t) {
        log.error("Fallback acionado ao listar tarefas: {}", t.getMessage(), t);
        return ResponseEntity.status(503).body(Collections.emptyList());
    }

    public ResponseEntity<TarefaResponse> fallbackBuscarTarefa(String authorizationHeader, String id, Throwable t) {
        log.error("Fallback acionado ao buscar tarefa: {}", t.getMessage(), t);
        return ResponseEntity.status(503).body(null);
    }

    public ResponseEntity<TarefaResponse> fallbackConcluirTarefa(String authorizationHeader, String id, Throwable t) {
        log.error("Fallback acionado ao concluir tarefa {}: {}", id, t.getMessage(), t);

        try {
            log.info("Tentando chamar diretamente após fallback para tarefa {}", id);
            ResponseEntity<TarefaResponse> response = tarefaClient.concluirTarefa(authorizationHeader, id);
            log.info("Chamada direta após fallback retornou status: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("Erro ao tentar chamar diretamente após fallback para tarefa {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(503).body(null);
        }
    }

    public ResponseEntity<Void> fallbackDeletarTarefa(String authorizationHeader, String id, Throwable t) {
        log.error("Fallback acionado ao deletar tarefa: {}", t.getMessage(), t);
        return ResponseEntity.status(503).build();
    }
}
