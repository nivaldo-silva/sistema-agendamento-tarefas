package io.github.nivaldosilva.cadastro_usuarios.mappers;

import io.github.nivaldosilva.cadastro_usuarios.dto.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.cadastro_usuarios.dto.response.UsuarioResponse;
import io.github.nivaldosilva.cadastro_usuarios.entity.Endereco;
import io.github.nivaldosilva.cadastro_usuarios.entity.Telefone;
import io.github.nivaldosilva.cadastro_usuarios.entity.Usuario;
import io.github.nivaldosilva.cadastro_usuarios.enums.Role;
import lombok.experimental.UtilityClass;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class UsuarioMapper {

    public static Usuario toEntity(RegistroUsuarioRequest request) {

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(request.senha())
                .roles(new HashSet<>(Set.of(Role.USUARIO)))
                .ativo(true)
                .contaBloqueada(false)
                .credenciaisExpiradas(false)
                .build();

        if (request.enderecos() != null) {
            usuario.setEnderecos(request.enderecos().stream().map(enderecoRequest -> {
                Endereco endereco = EnderecoMapper.toEntity(enderecoRequest);
                endereco.setUsuario(usuario);
                return endereco;
            }).toList());
        }

        if (request.telefones() != null) {
            usuario.setTelefones(request.telefones().stream().map(telefoneRequest -> {
                Telefone telefone = TelefoneMapper.toEntity(telefoneRequest);
                telefone.setUsuario(usuario);
                return telefone;
            }).toList());
        }

        return usuario;
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .roles(usuario.getRoles())
                .ativo(usuario.getAtivo())
                .enderecos(
                        usuario.getEnderecos() != null ? EnderecoMapper.toResponseList(usuario.getEnderecos()) : null)
                .telefones(
                        usuario.getTelefones() != null ? TelefoneMapper.toResponseList(usuario.getTelefones()) : null)
                .build();
    }

    public static Usuario updateFromRequest(Usuario usuario, RegistroUsuarioRequest request) {
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());

        return usuario;
    }
}