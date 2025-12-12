package io.github.nivaldosilva.agendador_tarefas.tasks;

import io.github.nivaldosilva.agendador_tarefas.client.NotificacaoClient;
import io.github.nivaldosilva.agendador_tarefas.client.dto.NotificacaoRequest;
import io.github.nivaldosilva.agendador_tarefas.entity.Tarefa;
import io.github.nivaldosilva.agendador_tarefas.enums.StatusNotificacao;
import io.github.nivaldosilva.agendador_tarefas.service.TarefaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LembreteTarefaScheduler {

    private final TarefaService tarefaService;
    private final NotificacaoClient notificacaoClient;

    @Scheduled(cron = "${cron.horario:0 */5 * * * *}")
    public void notificarTarefasProximas() {
        log.info("========================================");
        log.info("Iniciando job de notificação de tarefas");
        log.info("========================================");

        try {
            LocalDateTime agora = LocalDateTime.now();
            LocalDateTime limite = agora.plusMinutes(5);

            log.info("Buscando tarefas entre {} e {}", agora, limite);

            List<Tarefa> tarefas = tarefaService.buscarTarefasParaNotificar(agora, limite);

            log.info("Encontradas {} tarefas para notificar", tarefas.size());

            if (tarefas.isEmpty()) {
                log.info("Nenhuma tarefa pendente de notificação");
            } else {
                tarefas.forEach(this::processarNotificacao);
            }

            log.info("Job de notificação finalizado com sucesso");

        } catch (Exception e) {
            log.error("ERRO durante execução do job", e);
        }
    }

    private void processarNotificacao(Tarefa tarefa) {
            try {
                log.info("Processando notificação para tarefa ID: {}", tarefa.getId());
                log.info("- Título: {}", tarefa.getTitulo());
                log.info("- Email: {}", tarefa.getEmailUsuario());
                log.info("- Data Evento: {}", tarefa.getDataEvento());

                var request = new NotificacaoRequest(
                        tarefa.getEmailUsuario(),
                        tarefa.getTitulo(),
                        tarefa.getDescricao(),
                        tarefa.getDataEvento(),
                        tarefa.getPrioridade().toString()
                );

                log.info("Enviando notificação via cliente Feign...");
                notificacaoClient.enviarNotificacao(request);

                tarefaService.atualizarStatusNotificacao(
                        tarefa.getId(),
                        StatusNotificacao.ENVIADA
                );

                log.info("âœ“ Notificação enviada com sucesso para tarefa {}", tarefa.getId());

            } catch (Exception e) {
                log.error(" Falha ao notificar tarefa {}: {}",
                        tarefa.getId(), e.getMessage(), e);

                try {
                    tarefaService.atualizarStatusNotificacao(
                            tarefa.getId(),
                            StatusNotificacao.ERRO
                    );
                } catch (Exception ex) {
                    log.error("Erro ao atualizar status de notificação: {}", ex.getMessage());
                }
            }
        }
    }
