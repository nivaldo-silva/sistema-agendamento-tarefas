package io.github.nivaldosilva.cadastro_usuarios.repository;

import io.github.nivaldosilva.cadastro_usuarios.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TelefoneRepository  extends JpaRepository<Telefone, UUID> {
}
