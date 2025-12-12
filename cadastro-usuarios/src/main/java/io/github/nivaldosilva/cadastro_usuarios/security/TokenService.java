package io.github.nivaldosilva.cadastro_usuarios.security;

import io.github.nivaldosilva.cadastro_usuarios.dto.response.LoginResponse;
import io.github.nivaldosilva.cadastro_usuarios.entity.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration.seconds:3600}")
    private Long jwtExpirationSeconds;

    @Value("${jwt.issuer:api://cadastro-usuarios}")
    private String jwtIssuer;

    public LoginResponse generateTokens(Authentication authentication) {
        log.debug("Gerando Access Token para usuário: {}", authentication.getName());

        String accessToken = generateAccessTokenString(authentication);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .expiresIn(jwtExpirationSeconds)
                .build();
    }

    private String generateAccessTokenString(Authentication authentication) {
        var now = Instant.now();
        var expiresAt = now.plusSeconds(jwtExpirationSeconds);

        String scope = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.joining(" "));

        String userId = null;
        String nome = null;
        if (authentication.getPrincipal() instanceof Usuario usuario) {
            userId = usuario.getId().toString();
            nome = usuario.getNome();
        }

        var claimsBuilder = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(authentication.getName())
                .claim("scope", scope)
                .id(UUID.randomUUID().toString());

        if (userId != null) {
            claimsBuilder.claim("user_id", userId);
        }
        if (nome != null) {
            claimsBuilder.claim("nome", nome);
        }

        JwtClaimsSet claims = claimsBuilder.build();
        log.debug("Gerando access token para: {} com scopes: {}", authentication.getName(), scope);

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


}