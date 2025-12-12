package io.github.nivaldosilva.bff_agendador_tarefas.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nivaldosilva.bff_agendador_tarefas.enums.Prioridade;
import io.github.nivaldosilva.bff_agendador_tarefas.enums.Recorrencia;
import io.github.nivaldosilva.bff_agendador_tarefas.enums.StatusTarefa;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
@Schema(description = "Dados para criação de uma nova tarefa")
public record CriarTarefaRequest(

        @NotBlank(message = "O título é obrigatório")
        @Size(min = 3, max = 150, message = "O título deve ter entre 3 e 150 caracteres")
        @Schema(description = "Título da tarefa", example = "Reunião com equipe de desenvolvimento", required = true)
        String titulo,

        @NotBlank(message = "A descrição é obrigatória")
        @Size(min = 10, max = 1000, message = "A descrição deve ter entre 10 e 1000 caracteres")
        @Schema(description = "Descrição detalhada da tarefa", example = "Discutir as próximas funcionalidades do sistema e planejar o sprint", required = true)
        String descricao,

        @Size(max = 2000, message = "As observações devem ter no máximo 2000 caracteres")
        @Schema(description = "Observações adicionais sobre a tarefa", example = "Trazer notebook e material de apresentação")
        String observacoes,

        @Schema(description = "Tipo de recorrência da tarefa", example = "DIARIA")
        Recorrencia recorrencia,

        @NotNull(message = "A prioridade é obrigatória")
        @Schema(description = "Prioridade da tarefa", example = "ALTA", required = true)
        Prioridade prioridade,

        @NotNull(message = "O status da tarefa é obrigatório")
        @Schema(description = "Status inicial da tarefa", example = "PENDENTE", required = true)
        StatusTarefa status,

        @NotNull(message = "A data do evento é obrigatória")
        @FutureOrPresent(message = "A data do evento não pode ser no passado")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        @JsonProperty("data_evento")
        @Schema(description = "Data e hora do evento", example = "15/01/2024 14:30:00", required = true)
        LocalDateTime dataEvento,

        @Positive(message = "O tempo estimado deve ser positivo")
        @JsonProperty("tempo_estimado_minutos")
        @Schema(description = "Tempo estimado em minutos para conclusão", example = "60")
        Integer tempoEstimadoMinutos,

        @NotNull(message = "A data limite é obrigatória")
        @FutureOrPresent(message = "A data limite não pode ser no passado")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        @JsonProperty("data_limite")
        @Schema(description = "Data limite para conclusão da tarefa", example = "15/01/2024 16:00:00", required = true)
        LocalDateTime dataLimite

) {}
