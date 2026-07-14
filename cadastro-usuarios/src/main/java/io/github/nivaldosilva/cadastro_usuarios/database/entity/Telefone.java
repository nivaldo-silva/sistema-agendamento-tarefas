package io.github.nivaldosilva.cadastro_usuarios.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "telefones")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false)
    private UUID id;

    @Column(name = "numero", length = 15, nullable = false)
    private String numero;

    @Column(name = "ddd", length = 3, nullable = false)
    private String ddd;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dataCriacao;

}
