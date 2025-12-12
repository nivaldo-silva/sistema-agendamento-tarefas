package io.github.nivaldosilva.bff_agendador_tarefas.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TelefoneResponse(

        UUID id,
        String numero,
        String ddd
        
) {}
