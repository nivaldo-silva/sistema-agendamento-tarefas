package io.github.nivaldosilva.bff_agendador_tarefas.infrastructure.enums;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN("Administrador"),
    USUARIO("Usuário");

    private final String descricao;

    Role(String descricao) {
        this.descricao = descricao;
    }

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
