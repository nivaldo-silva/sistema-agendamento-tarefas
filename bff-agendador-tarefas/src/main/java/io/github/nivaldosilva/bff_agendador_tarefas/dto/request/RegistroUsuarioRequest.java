package io.github.nivaldosilva.bff_agendador_tarefas.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record RegistroUsuarioRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
        String senha, 

        @Valid
        @NotEmpty(message = "Pelo menos um endereço é obrigatório")
        List<EnderecoRequest> enderecos,

        @Valid
        @NotEmpty(message = "Pelo menos um telefone é obrigatório")
        List<TelefoneRequest> telefones

) {}
