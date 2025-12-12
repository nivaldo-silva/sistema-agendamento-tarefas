package io.github.nivaldosilva.agendador_tarefas.mappers;

import io.github.nivaldosilva.agendador_tarefas.dto.request.*;
import io.github.nivaldosilva.agendador_tarefas.dto.response.TarefaResponse;
import io.github.nivaldosilva.agendador_tarefas.entity.Tarefa;
import lombok.experimental.UtilityClass;
import java.util.Optional;

@UtilityClass
public class TarefasMapper {

    public static TarefaResponse toTarefaResponse(Tarefa tarefa) {
        return TarefaResponse.builder()
                .id(tarefa.getId())
                .titulo(tarefa.getTitulo())
                .descricao(tarefa.getDescricao())
                .observacoes(tarefa.getObservacoes())
                .recorrencia(tarefa.getRecorrencia())
                .prioridade(tarefa.getPrioridade())
                .status(tarefa.getStatus())
                .emailUsuario(tarefa.getEmailUsuario())
                .criadoEm(tarefa.getCriadoEm())
                .dataEvento(tarefa.getDataEvento())
                .atualizadoEm(tarefa.getAtualizadoEm())
                .tempoEstimadoMinutos(tarefa.getTempoEstimadoMinutos())
                .dataLimite(tarefa.getDataLimite())
                .statusNotificacao(tarefa.getStatusNotificacao())
                .dataConclusao(tarefa.getDataConclusao())
                .concluida(tarefa.isConcluida())  
                .build();
    }

    public static Tarefa toTarefa(CriarTarefaRequest request) {
        return Tarefa.builder()
                .titulo(request.titulo())
                .descricao(request.descricao())
                .observacoes(request.observacoes())
                .recorrencia(request.recorrencia())
                .prioridade(request.prioridade())
                .status(request.status())
                .dataEvento(request.dataEvento())
                .tempoEstimadoMinutos(request.tempoEstimadoMinutos())
                .dataLimite(request.dataLimite())
                .build();
    }

    public static void updateTarefaFromRequest(AtualizarTarefaRequest request, Tarefa tarefa) {
        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        tarefa.setObservacoes(request.observacoes());
        tarefa.setRecorrencia(request.recorrencia());
        tarefa.setPrioridade(request.prioridade());
        tarefa.setStatus(request.status());
        tarefa.setDataEvento(request.dataEvento());
        tarefa.setTempoEstimadoMinutos(request.tempoEstimadoMinutos());
        tarefa.setDataLimite(request.dataLimite());
        
    }

    public static void updateTarefaFromPartialRequest(AtualizarParcialTarefaRequest request, Tarefa tarefa) {
        Optional.ofNullable(request.titulo()).ifPresent(tarefa::setTitulo);
        Optional.ofNullable(request.descricao()).ifPresent(tarefa::setDescricao);
        Optional.ofNullable(request.observacoes()).ifPresent(tarefa::setObservacoes);
        Optional.ofNullable(request.recorrencia()).ifPresent(tarefa::setRecorrencia);
        Optional.ofNullable(request.prioridade()).ifPresent(tarefa::setPrioridade);
        Optional.ofNullable(request.status()).ifPresent(tarefa::setStatus);
        Optional.ofNullable(request.dataEvento()).ifPresent(tarefa::setDataEvento);
        Optional.ofNullable(request.tempoEstimadoMinutos()).ifPresent(tarefa::setTempoEstimadoMinutos);
        Optional.ofNullable(request.dataLimite()).ifPresent(tarefa::setDataLimite);
       
    }
}