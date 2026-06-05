package io.github.nivaldosilva.cadastro_usuarios.infrastructure.repository;

import io.github.nivaldosilva.cadastro_usuarios.domain.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EnderecoRepository  extends JpaRepository<Endereco, UUID> {
}
