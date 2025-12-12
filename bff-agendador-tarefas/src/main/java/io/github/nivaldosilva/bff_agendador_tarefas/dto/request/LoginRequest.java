package io.github.nivaldosilva.bff_agendador_tarefas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Dados de login do usuário")
public class LoginRequest {

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "Email inválido.")
    @Schema(description = "Email do usuário", example = "usuario@exemplo.com", required = true)
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Schema(description = "Senha do usuário", example = "senha123", required = true)
    private String senha;
}
