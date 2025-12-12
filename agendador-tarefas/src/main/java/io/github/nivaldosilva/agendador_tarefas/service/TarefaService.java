package io.github.nivaldosilva.agendador_tarefas.service;

import io.github.nivaldosilva.agendador_tarefas.client.NotificacaoClient;
import io.github.nivaldosilva.agendador_tarefas.client.dto.NotificacaoRequest;
import io.github.nivaldosilva.agendador_tarefas.dto.request.AtualizarParcialTarefaRequest;
import io.github.nivaldosilva.agendador_tarefas.dto.request.AtualizarTarefaRequest;
import io.github.nivaldosilva.agendador_tarefas.dto.request.CriarTarefaRequest;
import io.github.nivaldosilva.agendador_tarefas.dto.response.TarefaResponse;
import io.github.nivaldosilva.agendador_tarefas.dto.response.UsuarioResponse;
import io.github.nivaldosilva.agendador_tarefas.entity.Tarefa;
import io.github.nivaldosilva.agendador_tarefas.enums.StatusNotificacao;
import io.github.nivaldosilva.agendador_tarefas.exception.ResourceNotFoundException;
import io.github.nivaldosilva.agendador_tarefas.mappers.TarefasMapper;
import io.github.nivaldosilva.agendador_tarefas.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final NotificacaoClient notificacaoClient;

    @Transactional
    public TarefaResponse criarNovaTarefa(@NonNull CriarTarefaRequest request) {
        var tarefa = TarefasMapper.toTarefa(request);

        String emailUsuario = getEmailUsuarioLogado();
        log.info("Criando tarefa para o usuário: {}", emailUsuario);

        tarefa.setEmailUsuario(emailUsuario);

        var tarefaSalva = tarefaRepository.save(tarefa);
        log.info("Tarefa salva com ID: {}, Email: {}", tarefaSalva.getId(), tarefaSalva.getEmailUsuario());


        if (tarefaSalva.getEmailUsuario() != null && !tarefaSalva.getEmailUsuario().isBlank()) {
            enviarNotificacaoAsync(tarefaSalva);
        } else {
            log.warn("Email do usuário está nulo ou vazio, notificação não enviada");
        }

        return TarefasMapper.toTarefaResponse(tarefaSalva);
    }

    public TarefaResponse buscarTarefaPorId(@NonNull String tarefaId) {
        return TarefasMapper.toTarefaResponse(findById(tarefaId));
    }

    public List<TarefaResponse> listarTarefas(String email, LocalDateTime de, LocalDateTime ate) {
        String emailUsuario = Optional.ofNullable(email).orElseGet(this::getEmailUsuarioLogado);

        List<Tarefa> tarefas = (de != null && ate != null)
                ? tarefaRepository.findAllByEmailUsuarioAndDataEventoBetween(emailUsuario, de, ate)
                : tarefaRepository.findAllByEmailUsuario(emailUsuario);

        return tarefas.stream()
                .map(TarefasMapper::toTarefaResponse)
                .toList();
    }

    @Transactional
    public TarefaResponse atualizarTarefa(@NonNull String tarefaId, @NonNull AtualizarTarefaRequest request) {
        Tarefa tarefa = findById(tarefaId);
        TarefasMapper.updateTarefaFromRequest(request, tarefa);
        return TarefasMapper.toTarefaResponse(tarefaRepository.save(tarefa));
    }

    @Transactional
    public TarefaResponse atualizarParcialmenteTarefa(
            @NonNull String tarefaId,
            @NonNull AtualizarParcialTarefaRequest request) {
        Tarefa tarefa = findById(tarefaId);
        TarefasMapper.updateTarefaFromPartialRequest(request, tarefa);
        return TarefasMapper.toTarefaResponse(tarefaRepository.save(tarefa));
    }

    @Transactional
    public void deletarTarefa(@NonNull String tarefaId) {
        if (!tarefaRepository.existsById(tarefaId)) {
            throw new ResourceNotFoundException("Tarefa com id " + tarefaId + " não encontrada.");
        }
        tarefaRepository.deleteById(tarefaId);
    }

    @Transactional
    public TarefaResponse concluirTarefa(@NonNull String tarefaId) {
        Tarefa tarefa = findById(tarefaId);
        log.info("Concluindo tarefa com ID: {}, status atual: {}", tarefaId, tarefa.getStatus());
        tarefa.concluir();
        Tarefa tarefaAtualizada = tarefaRepository.save(tarefa);
        log.info("Tarefa atualizada salva com status: {}, concluida: {}", tarefaAtualizada.getStatus(), tarefaAtualizada.isConcluida());
        TarefaResponse response = TarefasMapper.toTarefaResponse(tarefaAtualizada);
        log.info("Retornando response com ID: {}, status: {}, concluida: {}", response.id(), response.status(), response.concluida());
        return response;
    }

    @Transactional
    public void atualizarStatusNotificacao(@NonNull String tarefaId, @NonNull StatusNotificacao status) {
        Tarefa tarefa = findById(tarefaId);
        tarefa.setStatusNotificacao(status);
        tarefaRepository.save(tarefa);
    }

    public List<Tarefa> buscarTarefasParaNotificar(
            @NonNull LocalDateTime inicio,
            @NonNull LocalDateTime fim) {
        return tarefaRepository.findTarefasParaNotificar(inicio, fim, StatusNotificacao.PENDENTE);
    }

    public List<Tarefa> findTarefasProximasDoVencimento(LocalDateTime agora) {
        LocalDateTime futuro = agora.plusHours(24);
        log.info("Buscando tarefas para lembrete entre {} e {}", agora, futuro);
        return tarefaRepository.findTarefasParaLembrete(agora, futuro);
    }

    public UsuarioResponse buscarDetalhesDoUsuarioDaTarefa(@NonNull String tarefaId) {
        findById(tarefaId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new IllegalStateException("Tipo de autenticação inválido.");
        }

        String nome = jwtAuth.getToken().getClaimAsString("nome");
        String email = jwtAuth.getToken().getSubject();

        return new UsuarioResponse(nome, email);
    }

    public Tarefa findById(@NonNull String tarefaId) {
        return tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tarefa com id " + tarefaId + " não encontrada."));
    }

    private String getEmailUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Usuário não autenticado.");
        }
        return authentication.getName();
    }

    private void enviarNotificacaoAsync(Tarefa tarefa) {
        try {
            var request = new NotificacaoRequest(
                    tarefa.getEmailUsuario(),
                    tarefa.getTitulo(),
                    tarefa.getDescricao(),
                    tarefa.getDataEvento(),
                    tarefa.getPrioridade().toString());
            notificacaoClient.enviarNotificacao(request);
        } catch (Exception e) {
            log.error("Erro ao enviar notificação para tarefa {}: {}",
                    tarefa.getId(), e.getMessage());
        }
    }
}