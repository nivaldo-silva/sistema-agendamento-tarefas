package io.github.nivaldosilva.agendador_tarefas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.nivaldosilva.agendador_tarefas.enums.Prioridade;
import io.github.nivaldosilva.agendador_tarefas.enums.Recorrencia;
import io.github.nivaldosilva.agendador_tarefas.enums.StatusNotificacao;
import io.github.nivaldosilva.agendador_tarefas.enums.StatusTarefa;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;

@Document(collection = "tarefas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tarefa {

    @Id
    private String id;

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 150, message = "O título deve ter entre 3 e 150 caracteres")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 10, max = 1000, message = "A descrição deve ter entre 10 e 1000 caracteres")
    private String descricao;

    @Size(max = 2000, message = "As observações devem ter no máximo 2000 caracteres")
    private String observacoes;

    private Recorrencia recorrencia;

    @NotNull(message = "A prioridade é obrigatória")
    @Builder.Default
    @Indexed
    private Prioridade prioridade = Prioridade.MEDIA;

    @NotNull(message = "O status da tarefa é obrigatório")
    @Builder.Default
    @Indexed
    private StatusTarefa status = StatusTarefa.BACKLOG;

    @NotBlank(message = "O email do usuário é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
    @Indexed
    @Field("email_usuario")
    private String emailUsuario;

    @CreatedDate
    @Field("criado_em")
    private LocalDateTime criadoEm;

    @NotNull(message = "A data do evento é obrigatória")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Indexed
    @Field("data_evento")
    private LocalDateTime dataEvento;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Field("atualizado_em")
    private LocalDateTime atualizadoEm;

    @Positive(message = "O tempo estimado deve ser positivo")
    @Field("tempo_estimado_minutos")
    private Integer tempoEstimadoMinutos;

    @NotNull(message = "A data limite é obrigatória")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Indexed
    @Field("data_limite")
    private LocalDateTime dataLimite;

    @NotNull(message = "O status da notificação é obrigatório")
    @Builder.Default
    @Field("status_notificacao")
    private StatusNotificacao statusNotificacao = StatusNotificacao.PENDENTE;

    @Field("data_conclusao")
    private LocalDateTime dataConclusao;

    @AssertTrue(message = "A data limite deve ser posterior à data do evento")
    public boolean isDataLimiteValida() {
        if (dataEvento == null || dataLimite == null)
            return true;
        return dataLimite.isAfter(dataEvento) || dataLimite.isEqual(dataEvento);
    }

    public boolean isConcluida() {
        return status == StatusTarefa.CONCLUIDA;
    }

    public void concluir() {
        this.status = StatusTarefa.CONCLUIDA;
        this.dataConclusao = LocalDateTime.now();
    }
}