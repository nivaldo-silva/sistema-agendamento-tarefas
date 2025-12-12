package io.github.nivaldosilva.cadastro_usuarios.security;

import io.github.nivaldosilva.cadastro_usuarios.dto.request.LoginRequest;
import io.github.nivaldosilva.cadastro_usuarios.dto.response.LoginResponse;
import io.github.nivaldosilva.cadastro_usuarios.entity.Usuario;
import io.github.nivaldosilva.cadastro_usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenGenerator;
    private final UsuarioRepository usuarioRepository;
    private final JwtDecoder jwtDecoder;

    public LoginResponse autenticarUsuario(LoginRequest loginRequest) {
        String email = loginRequest.getEmail().toLowerCase().trim();

        try {
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos"));
            if (!usuario.isEnabled()) {
                log.warn("Tentativa de login de conta desativada: {}", email);
                throw new DisabledException("Conta desativada");
            }
            if (!usuario.isAccountNonLocked()) {
                log.warn("Tentativa de login de conta bloqueada: {}", email);
                throw new LockedException("Conta bloqueada");
            }

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email,loginRequest.getSenha());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info("Login realizado com sucesso para usuário: {} com roles: {}",email, usuario.getRoles());

            return tokenGenerator.generateTokens(authentication);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            log.warn("Falha na autenticao para usuario: {} - {}", email, e.getMessage());
            throw new BadCredentialsException("Email ou senha inválidos");
        } catch (DisabledException | LockedException e) {
            log.warn("Conta com restrições tentou fazer login: {} - {}", email, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado na autenticao para usuario: {}", email, e);
            throw new BadCredentialsException("Erro interno. Tente novamente mais tarde.");
        }
    }


}
