package io.github.nivaldosilva.agendador_tarefas.enums;

import lombok.Getter;

@Getter
public enum Recorrencia {

    NENHUMA("Nenhuma", 0),
    DIARIA("Diária", 1),
    SEMANAL("Semanal", 7),
    QUINZENAL("Quinzenal", 15),
    MENSAL("Mensal", 30),
    TRIMESTRAL("Trimestral", 90),
    SEMESTRAL("Semestral", 180),
    ANUAL("Anual", 365);

    private final String nome;
    private final int diasIntervalo;

    Recorrencia(String nome, int diasIntervalo) {
        this.nome = nome;
        this.diasIntervalo = diasIntervalo;
    }
}
