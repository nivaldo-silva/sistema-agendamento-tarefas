package io.github.nivaldosilva.bff_agendador_tarefas.enums;

import lombok.Getter;

@Getter
public enum StatusNotificacao {

    PENDENTE("Pendente", "Notificação não enviada"),
    ENVIADA("Enviada", "Notificação enviada com sucesso"),
    ERRO("Erro", "Erro ao enviar notificação"),
    AGENDADA("Agendada", "Notificação programada");

    private final String nome;
    private final String descricao;

    StatusNotificacao(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
}
