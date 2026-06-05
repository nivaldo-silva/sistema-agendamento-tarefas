package io.github.nivaldosilva.cadastro_usuarios.infrastructure.repository;

import io.github.nivaldosilva.cadastro_usuarios.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
