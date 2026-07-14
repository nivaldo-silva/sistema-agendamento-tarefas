package io.github.nivaldosilva.cadastro_usuarios.database.repository;

import io.github.nivaldosilva.cadastro_usuarios.database.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TelefoneRepository  extends JpaRepository<Telefone, UUID> {
}
