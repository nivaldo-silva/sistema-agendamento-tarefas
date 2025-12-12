package io.github.nivaldosilva.agendador_tarefas.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime dataEvento,
        
        @JsonProperty("prioridade")
        String prioridade
) {
}
