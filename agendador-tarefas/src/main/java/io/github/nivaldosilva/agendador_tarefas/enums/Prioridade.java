package io.github.nivaldosilva.agendador_tarefas.enums;

import lombok.Getter;

@Getter
public enum Prioridade {

    BAIXA(1, "Baixa"),
    MEDIA(2, "Média"),
    ALTA(3, "Alta"),
    URGENTE(4, "Urgente");

    private final int nivel;
    private final String nome;


    Prioridade(int nivel, String nome) {
        this.nivel = nivel;
        this.nome = nome;

    }
}
