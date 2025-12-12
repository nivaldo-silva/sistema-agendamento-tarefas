package io.github.nivaldosilva.agendador_tarefas.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nivaldosilva.agendador_tarefas.enums.*;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TarefaResponse(

        String id,
        String titulo,
        String descricao,
        String observacoes,
        Recorrencia recorrencia,
        Prioridade prioridade,
        StatusTarefa status,

        @JsonProperty("email_usuario")
        String emailUsuario,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        @JsonProperty("criado_em")
        LocalDateTime criadoEm,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        @JsonProperty("data_evento")
        LocalDateTime dataEvento,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        @JsonProperty("atualizado_em")
        LocalDateTime atualizadoEm,

        @JsonProperty("tempo_estimado_minutos")
        Integer tempoEstimadoMinutos,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        @JsonProperty("data_limite")
        LocalDateTime dataLimite,

        @JsonProperty("status_notificacao")
        StatusNotificacao statusNotificacao,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        @JsonProperty("data_conclusao")
        LocalDateTime dataConclusao,

        Boolean concluida  

) {}