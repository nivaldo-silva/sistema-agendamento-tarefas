package io.github.nivaldosilva.bff_agendador_tarefas.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Resposta de autenticação contendo o token JWT")
public record LoginResponse(
        @Schema(description = "Token JWT para autenticação nas requisições", example = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,
        
        @JsonProperty("expires_in")
        @Schema(description = "Tempo de expiração do token em segundos", example = "3600")
        Long expiresIn,
        
        @Schema(description = "Tipo do token", example = "Bearer")
        String tokenType
) {
    @Builder
    public LoginResponse(String accessToken, Long expiresIn, String tokenType) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.tokenType = (tokenType != null) ? tokenType : "Bearer";
    }
}