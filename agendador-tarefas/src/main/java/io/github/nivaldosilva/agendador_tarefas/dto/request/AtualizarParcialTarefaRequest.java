package io.github.nivaldosilva.agendador_tarefas.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nivaldosilva.agendador_tarefas.enums.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record AtualizarParcialTarefaRequest(

        @Size(min = 3, max = 150, message = "O título deve ter entre 3 e 150 caracteres")
        String titulo,

        @Size(min = 10, max = 1000, message = "A descrição deve ter entre 10 e 1000 caracteres")
        String descricao,

        @Size(max = 2000, message = "As observações devem ter no máximo 2000 caracteres")
        String observacoes,

        Recorrencia recorrencia,

        Prioridade prioridade,

        StatusTarefa status,

        @Future(message = "A data do evento deve ser no futuro") 
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        @JsonProperty("data_evento")
        LocalDateTime dataEvento,

        @Positive(message = "O tempo estimado deve ser positivo")
        @JsonProperty("tempo_estimado_minutos")
        Integer tempoEstimadoMinutos,

        @Future(message = "A data limite deve ser no futuro") 
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        @JsonProperty("data_limite")
        LocalDateTime dataLimite

) {}