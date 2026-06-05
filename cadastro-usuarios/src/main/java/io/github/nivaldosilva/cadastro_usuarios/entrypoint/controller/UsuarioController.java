package io.github.nivaldosilva.cadastro_usuarios.entrypoint.controller;

import io.github.nivaldosilva.cadastro_usuarios.application.dto.request.EnderecoRequest;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.request.LoginRequest;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.request.TelefoneRequest;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.response.EnderecoResponse;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.response.LoginResponse;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.response.TelefoneResponse;
import io.github.nivaldosilva.cadastro_usuarios.application.dto.response.UsuarioResponse;
import io.github.nivaldosilva.cadastro_usuarios.application.service.AuthService;
import io.github.nivaldosilva.cadastro_usuarios.application.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService usuarioService;
	private final AuthService autenticacaoService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
		LoginResponse response = autenticacaoService.autenticarUsuario(loginRequest);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/registro")
	public ResponseEntity<UsuarioResponse> registro(@RequestBody @Valid RegistroUsuarioRequest request) {
		UsuarioResponse response = usuarioService.registrarUsuario(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<List<UsuarioResponse>> listar() {
		List<UsuarioResponse> response = usuarioService.listarTodos();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/email/{email}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<UsuarioResponse> buscarPorEmail(@PathVariable String email) {
		UsuarioResponse response = usuarioService.buscarPorEmail(email);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/admin")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<UsuarioResponse> criarAdmin(@RequestBody @Valid RegistroUsuarioRequest request) {
		UsuarioResponse response = usuarioService.criarAdmin(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<Void> deletarUsuario(@PathVariable UUID id) {
		usuarioService.deletarUsuario(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/perfil")
	@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
	public ResponseEntity<UsuarioResponse> verPerfil() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		UsuarioResponse response = usuarioService.buscarPorEmail(email);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/perfil")
	@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
	public ResponseEntity<UsuarioResponse> atualizarPerfil(@RequestBody @Valid RegistroUsuarioRequest request) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		UsuarioResponse response = usuarioService.atualizarPerfil(email, request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/endereco")
	@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
	public ResponseEntity<EnderecoResponse> adicionarEndereco(@RequestBody @Valid EnderecoRequest request) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		EnderecoResponse response = usuarioService.adicionarEndereco(email, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/telefone")
	@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
	public ResponseEntity<TelefoneResponse> adicionarTelefone(@RequestBody @Valid TelefoneRequest request) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		TelefoneResponse response = usuarioService.adicionarTelefone(email, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/endereco/{id}")
	@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
	public ResponseEntity<EnderecoResponse> atualizarEndereco(
			@PathVariable UUID id,
			@RequestBody @Valid EnderecoRequest request) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		EnderecoResponse response = usuarioService.atualizarEndereco(email, id, request);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/telefone/{id}")
	@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
	public ResponseEntity<TelefoneResponse> atualizarTelefone(
			@PathVariable UUID id,
			@RequestBody @Valid TelefoneRequest request) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		TelefoneResponse response = usuarioService.atualizarTelefone(email, id, request);
		return ResponseEntity.ok(response);
	}
}