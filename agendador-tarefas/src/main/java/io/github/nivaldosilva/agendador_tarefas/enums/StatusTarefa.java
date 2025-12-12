package io.github.nivaldosilva.agendador_tarefas.enums;

import lombok.Getter;

@Getter
public enum StatusTarefa {

    BACKLOG("Backlog", "Tarefa aguardando início"),
    EM_ANDAMENTO("Em Andamento", "Tarefa sendo executada"),
    PAUSADA("Pausada", "Tarefa temporariamente pausada"),
    AGUARDANDO_APROVACAO("Aguardando Aprovação", "Tarefa aguardando validação"),
    CONCLUIDA("Concluída", "Tarefa finalizada com sucesso"),
    CANCELADA("Cancelada", "Tarefa cancelada");

    private final String nome;
    private final String descricao;

    StatusTarefa(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
}
