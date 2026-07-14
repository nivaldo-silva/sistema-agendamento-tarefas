package io.github.nivaldosilva.cadastro_usuarios.infrastructure.startup;

import io.github.nivaldosilva.cadastro_usuarios.application.dto.request.EnderecoRequest;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.request.TelefoneRequest;
import io.github.nivaldosilva.cadastro_usuarios.database.repository.UsuarioRepository;
import io.github.nivaldosilva.cadastro_usuarios.application.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer implements CommandLineRunner {

	private final UsuarioService usuarioService;
	private final UsuarioRepository usuarioRepository;

	@Override
	public void run(String... args) throws Exception {
		if (usuarioRepository.findByEmail("admin@email.com").isEmpty()) {

			EnderecoRequest enderecoAdminRequest = new EnderecoRequest(
					"N/A",
					"N/A",
					"N/A",
					"N/A",
					"N/A",
					"NA",
					"00000-000"
			);

			TelefoneRequest telefoneAdminRequest = new TelefoneRequest(
					"000000000",
					"000"
			);

			RegistroUsuarioRequest adminRequest = new RegistroUsuarioRequest(
					"Admin",
					"admin@email.com",
					"admin123",
					List.of(enderecoAdminRequest),
					List.of(telefoneAdminRequest)
			);


			usuarioService.criarAdmin(adminRequest);
			log.info("Usuario administrador padrao criado.");
		} else {
			log.info("O usuario administrador padrao ja existe..");
		}
	}
}