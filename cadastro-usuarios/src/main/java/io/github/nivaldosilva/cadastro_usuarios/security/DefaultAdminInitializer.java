package io.github.nivaldosilva.cadastro_usuarios.security;

import io.github.nivaldosilva.cadastro_usuarios.entity.Endereco;
import io.github.nivaldosilva.cadastro_usuarios.entity.Telefone;
import io.github.nivaldosilva.cadastro_usuarios.entity.Usuario;
import io.github.nivaldosilva.cadastro_usuarios.enums.Role;
import io.github.nivaldosilva.cadastro_usuarios.repository.UsuarioRepository;
import io.github.nivaldosilva.cadastro_usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultAdminInitializer implements CommandLineRunner {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByEmail("admin@email.com").isEmpty()) {

            Usuario admin = Usuario.builder()
                    .nome("Admin")
                    .email("admin@email.com")
                    .senha("admin123")
                    .roles(Set.of(Role.ADMIN, Role.USUARIO))
                    .build();

            Endereco enderecoAdmin = Endereco.builder()
                    .rua("N/A")
                    .numero("N/A")
                    .complemento("N/A")
                    .bairro("N/A")
                    .cidade("N/A")
                    .estado("NA")
                    .cep("00000-000")
                    .usuario(admin)
                    .build();

            Telefone telefoneAdmin = Telefone.builder()
                    .ddd("000")
                    .numero("000000000")
                    .usuario(admin)
                    .build();

            admin.setEnderecos(List.of(enderecoAdmin));
            admin.setTelefones(List.of(telefoneAdmin));

            usuarioService.criarAdmin(admin);
            log.info("Usuario administrador padrao criado.");
        } else {
            log.info("O usuario administrador padrao ja existe..");
        }
    }
}
