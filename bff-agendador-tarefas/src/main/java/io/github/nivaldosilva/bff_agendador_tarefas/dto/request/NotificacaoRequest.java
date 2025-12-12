package io.github.nivaldosilva.bff_agendador_tarefas.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record NotificacaoRequest(

        @JsonProperty("email_destinatario")
        String emailDestinatario,

        @JsonProperty("titulo_tarefa")
        String tituloTarefa,

        @JsonProperty("descricao_tarefa")
        String descricaoTarefa,

        @JsonProperty("data_evento")
        String dataEvento,

        @JsonProperty("prioridade")
        String prioridade
) {}