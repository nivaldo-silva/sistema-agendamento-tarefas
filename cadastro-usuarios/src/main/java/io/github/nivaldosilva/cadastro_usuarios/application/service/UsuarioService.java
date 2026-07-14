package io.github.nivaldosilva.cadastro_usuarios.application.service;

import io.github.nivaldosilva.cadastro_usuarios.application.dto.request.EnderecoRequest;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.request.TelefoneRequest;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.response.EnderecoResponse;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.response.TelefoneResponse;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.response.UsuarioResponse;
import io.github.nivaldosilva.cadastro_usuarios.database.entity.Endereco;
import io.github.nivaldosilva.cadastro_usuarios.database.entity.Telefone;
import io.github.nivaldosilva.cadastro_usuarios.database.entity.Usuario;
import io.github.nivaldosilva.cadastro_usuarios.database.enums.Role;
import io.github.nivaldosilva.cadastro_usuarios.application.exception.RegistroDuplicadoException;
import io.github.nivaldosilva.cadastro_usuarios.application.exception.RecursoNaoEncontradoException;
import io.github.nivaldosilva.cadastro_usuarios.application.mappers.EnderecoMapper;
import io.github.nivaldosilva.cadastro_usuarios.application.mappers.TelefoneMapper;
import io.github.nivaldosilva.cadastro_usuarios.application.mappers.UsuarioMapper;
import io.github.nivaldosilva.cadastro_usuarios.database.repository.EnderecoRepository;
import io.github.nivaldosilva.cadastro_usuarios.database.repository.TelefoneRepository;
import io.github.nivaldosilva.cadastro_usuarios.database.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final EnderecoRepository enderecoRepository;
	private final TelefoneRepository telefoneRepository;

	@Transactional
	public UsuarioResponse registrarUsuario(RegistroUsuarioRequest request) {
		validarEmailUnico(request.email());

		Usuario usuario = UsuarioMapper.toEntity(request);
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		usuario.setRoles(Set.of(Role.USUARIO));
		usuario.setAtivo(true);

		Usuario salvo = usuarioRepository.save(usuario);
		log.info("Usuário registrado: {}", salvo.getEmail());

		return UsuarioMapper.toResponse(salvo);
	}

	@Transactional
	public UsuarioResponse criarAdmin(RegistroUsuarioRequest request) {
		validarEmailUnico(request.email());

		Usuario usuario = UsuarioMapper.toEntity(request);
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		usuario.setRoles(Set.of(Role.ADMIN, Role.USUARIO));
		usuario.setAtivo(true);

		Usuario salvo = usuarioRepository.save(usuario);
		log.info("Administrador criado: {}", salvo.getEmail());

		return UsuarioMapper.toResponse(salvo);
	}

	@Transactional(readOnly = true)
	public List<UsuarioResponse> listarTodos() {
		return usuarioRepository.findAll().stream()
				.map(UsuarioMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public UsuarioResponse buscarPorEmail(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + email));
		return UsuarioMapper.toResponse(usuario);
	}

	@Transactional
	public UsuarioResponse atualizarPerfil(String emailAutenticado, RegistroUsuarioRequest request) {
		Usuario usuario = usuarioRepository.findByEmail(emailAutenticado)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

		if (!usuario.getEmail().equals(request.email())) {
			validarEmailUnico(request.email());
			usuario.setEmail(request.email());
		}

		usuario.setNome(request.nome());

		if (request.senha() != null && !request.senha().isBlank()) {
			usuario.setSenha(passwordEncoder.encode(request.senha()));
		}

		Usuario atualizado = usuarioRepository.save(usuario);
		return UsuarioMapper.toResponse(atualizado);
	}

	@Transactional
	public EnderecoResponse adicionarEndereco(String userEmail, EnderecoRequest request) {
		Usuario usuario = usuarioRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

		Endereco novoEndereco = EnderecoMapper.toEntity(request);
		novoEndereco.setUsuario(usuario);

		Endereco enderecoSalvo = enderecoRepository.save(novoEndereco);
		log.info("Novo endereço adicionado para o usuário {}: {}", userEmail, enderecoSalvo.getId());

		return EnderecoMapper.toResponse(enderecoSalvo);
	}

	@Transactional
	public TelefoneResponse adicionarTelefone(String userEmail, TelefoneRequest request) {
		Usuario usuario = usuarioRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

		Telefone novoTelefone = TelefoneMapper.toEntity(request);
		novoTelefone.setUsuario(usuario);

		Telefone telefoneSalvo = telefoneRepository.save(novoTelefone);
		log.info("Novo telefone adicionado para o usuário {}: {}", userEmail, telefoneSalvo.getId());

		return TelefoneMapper.toResponse(telefoneSalvo);
	}

	@Transactional
	public void deletarUsuario(UUID id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

		usuarioRepository.deleteById(id);
		log.info("Usuário deletado: {}", usuario.getEmail());
	}

	@Transactional
	public EnderecoResponse atualizarEndereco(String userEmail, UUID enderecoId, EnderecoRequest request) {
		Usuario usuario = usuarioRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

		Endereco endereco = enderecoRepository.findById(enderecoId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Endereço não encontrado"));

		if (!isAdmin(userEmail) && !endereco.getUsuario().getId().equals(usuario.getId())) {
			throw new RuntimeException("Você só pode atualizar seus próprios endereços");
		}

		endereco.setRua(request.rua());
		endereco.setNumero(request.numero());
		endereco.setComplemento(request.complemento());
		endereco.setCidade(request.cidade());
		endereco.setBairro(request.bairro());
		endereco.setEstado(request.estado());
		endereco.setCep(request.cep());


		Endereco atualizado = enderecoRepository.save(endereco);
		log.info("Endereço {} atualizado por {}", enderecoId, userEmail);

		return EnderecoMapper.toResponse(atualizado);
	}

	@Transactional
	public TelefoneResponse atualizarTelefone(String userEmail, UUID telefoneId, TelefoneRequest request) {
		Usuario usuario = usuarioRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

		Telefone telefone = telefoneRepository.findById(telefoneId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Telefone não encontrado"));

		if (!isAdmin(userEmail) && !telefone.getUsuario().getId().equals(usuario.getId())) {
			throw new RuntimeException("Você só pode atualizar seus próprios telefones");
		}

		telefone.setNumero(request.numero());
		telefone.setDdd(request.ddd());

		Telefone atualizado = telefoneRepository.save(telefone);
		log.info("Telefone {} atualizado por {}", telefoneId, userEmail);

		return TelefoneMapper.toResponse(atualizado);
	}

	private boolean isAdmin(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
		return usuario.getRoles().contains(Role.ADMIN);
	}

	private void validarEmailUnico(String email) {
		if (usuarioRepository.existsByEmail(email)) {
			throw new RegistroDuplicadoException("Email já cadastrado: " + email);
		}
	}
}