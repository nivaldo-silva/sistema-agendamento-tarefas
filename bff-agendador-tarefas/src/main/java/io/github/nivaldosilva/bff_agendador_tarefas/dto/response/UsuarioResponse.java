package io.github.nivaldosilva.bff_agendador_tarefas.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.nivaldosilva.bff_agendador_tarefas.enums.Role;
import lombok.Builder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UsuarioResponse(

        UUID id,
        String nome,
        String email,
        Set<Role> roles, 
        Boolean ativo,
        List<EnderecoResponse> enderecos,
        List<TelefoneResponse> telefones

        
) {}
