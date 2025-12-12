package io.github.nivaldosilva.cadastro_usuarios.controller;

import io.github.nivaldosilva.cadastro_usuarios.dto.request.EnderecoRequest;
import io.github.nivaldosilva.cadastro_usuarios.dto.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.cadastro_usuarios.dto.request.TelefoneRequest;
import io.github.nivaldosilva.cadastro_usuarios.dto.response.EnderecoResponse;
import io.github.nivaldosilva.cadastro_usuarios.dto.response.TelefoneResponse;
import io.github.nivaldosilva.cadastro_usuarios.dto.response.UsuarioResponse;
import io.github.nivaldosilva.cadastro_usuarios.entity.Endereco;
import io.github.nivaldosilva.cadastro_usuarios.entity.Telefone;
import io.github.nivaldosilva.cadastro_usuarios.entity.Usuario;
import io.github.nivaldosilva.cadastro_usuarios.mappers.EnderecoMapper;
import io.github.nivaldosilva.cadastro_usuarios.mappers.TelefoneMapper;
import io.github.nivaldosilva.cadastro_usuarios.mappers.UsuarioMapper;
import io.github.nivaldosilva.cadastro_usuarios.dto.request.LoginRequest;
import io.github.nivaldosilva.cadastro_usuarios.dto.response.LoginResponse;
import io.github.nivaldosilva.cadastro_usuarios.security.AuthenticationService;
import io.github.nivaldosilva.cadastro_usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationService autenticacaoService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse response = autenticacaoService.autenticarUsuario(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registro(@RequestBody @Valid RegistroUsuarioRequest request) {
        Usuario usuario = UsuarioMapper.toEntity(request);
        Usuario novoUsuario = usuarioService.registrarUsuario(usuario);
        UsuarioResponse response = UsuarioMapper.toResponse(novoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        List<UsuarioResponse> response = usuarios.stream()
                .map(UsuarioMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UsuarioResponse> buscarPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(UsuarioMapper.toResponse(usuario));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UsuarioResponse> criarAdmin(@RequestBody @Valid RegistroUsuarioRequest request) {
        Usuario usuario = UsuarioMapper.toEntity(request);
        Usuario novoUsuario = usuarioService.criarAdmin(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toResponse(novoUsuario));
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
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(UsuarioMapper.toResponse(usuario));
    }

    @PutMapping("/perfil")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    public ResponseEntity<UsuarioResponse> atualizarPerfil(@RequestBody @Valid RegistroUsuarioRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = UsuarioMapper.toEntity(request);
        Usuario usuarioAtualizado = usuarioService.atualizarPerfil(email, usuario);
        return ResponseEntity.ok(UsuarioMapper.toResponse(usuarioAtualizado));
    }

    @PostMapping("/endereco")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    public ResponseEntity<EnderecoResponse> adicionarEndereco(@RequestBody @Valid EnderecoRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Endereco novoEndereco = EnderecoMapper.toEntity(request);
        Endereco enderecoSalvo = usuarioService.adicionarEndereco(email, novoEndereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(EnderecoMapper.toResponse(enderecoSalvo));
    }

    @PostMapping("/telefone")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    public ResponseEntity<TelefoneResponse> adicionarTelefone(@RequestBody @Valid TelefoneRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Telefone novoTelefone = TelefoneMapper.toEntity(request);
        Telefone telefoneSalvo = usuarioService.adicionarTelefone(email, novoTelefone);
        return ResponseEntity.status(HttpStatus.CREATED).body(TelefoneMapper.toResponse(telefoneSalvo));
    }

    @PutMapping("/endereco/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    public ResponseEntity<EnderecoResponse> atualizarEndereco(
            @PathVariable UUID id,
            @RequestBody @Valid EnderecoRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Endereco endereco = EnderecoMapper.toEntity(request);
        Endereco enderecoAtualizado = usuarioService.atualizarEndereco(email, id, endereco);
        return ResponseEntity.ok(EnderecoMapper.toResponse(enderecoAtualizado));
    }

    @PutMapping("/telefone/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    public ResponseEntity<TelefoneResponse> atualizarTelefone(
            @PathVariable UUID id,
            @RequestBody @Valid TelefoneRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Telefone telefone = TelefoneMapper.toEntity(request);
        Telefone telefoneAtualizado = usuarioService.atualizarTelefone(email, id, telefone);
        return ResponseEntity.ok(TelefoneMapper.toResponse(telefoneAtualizado));
    }


}
