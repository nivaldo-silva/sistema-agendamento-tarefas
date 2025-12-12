package io.github.nivaldosilva.cadastro_usuarios.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({ "accessToken", "expires_in", "tokenType" })
public class LoginResponse {

  private String accessToken;

  @JsonProperty("expires_in")
  private Long expiresIn;

  @Builder.Default
  private String tokenType = "Bearer";

}