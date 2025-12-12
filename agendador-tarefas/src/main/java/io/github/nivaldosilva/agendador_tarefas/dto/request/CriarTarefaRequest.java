package io.github.nivaldosilva.agendador_tarefas.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nivaldosilva.agendador_tarefas.enums.Prioridade;
import io.github.nivaldosilva.agendador_tarefas.enums.Recorrencia;
import io.github.nivaldosilva.agendador_tarefas.enums.StatusTarefa;
import io.github.nivaldosilva.agendador_tarefas.validation.DataLimiteValida;
import jakarta.validation.constraints.*;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
@DataLimiteValida
public record CriarTarefaRequest(

       @NotBlank(message = "O título é obrigatório")
        @Size(min = 3, max = 150, message = "O título deve ter entre 3 e 150 caracteres")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória")
        @Size(min = 10, max = 1000, message = "A descrição deve ter entre 10 e 1000 caracteres")
        String descricao,

        @Size(max = 2000, message = "As observações devem ter no máximo 2000 caracteres")
        String observacoes,

        Recorrencia recorrencia,

        @NotNull(message = "A prioridade é obrigatória")
        Prioridade prioridade,

        @NotNull(message = "O status da tarefa é obrigatório")
        StatusTarefa status,

        @NotNull(message = "A data do evento é obrigatória")
        @Future(message = "A data do evento deve ser no futuro")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        @JsonProperty("data_evento")
        LocalDateTime dataEvento,

        @Positive(message = "O tempo estimado deve ser positivo")
        @JsonProperty("tempo_estimado_minutos")
        Integer tempoEstimadoMinutos,

        @NotNull(message = "A data limite é obrigatória")
        @Future(message = "A data limite deve ser no futuro")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        @JsonProperty("data_limite")
        LocalDateTime dataLimite

) {}