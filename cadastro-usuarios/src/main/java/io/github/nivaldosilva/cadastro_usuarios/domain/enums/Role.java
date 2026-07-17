package io.github.nivaldosilva.cadastro_usuarios.database.enums;

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
