package io.github.nivaldosilva.agendador_tarefas.validation;

import io.github.nivaldosilva.agendador_tarefas.dto.request.CriarTarefaRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DataLimiteValidator implements ConstraintValidator<DataLimiteValida, CriarTarefaRequest> {

    @Override
    public boolean isValid(CriarTarefaRequest request, ConstraintValidatorContext context) {
        if (request.dataEvento() == null || request.dataLimite() == null) {
            return true;
        }
        return request.dataLimite().isAfter(request.dataEvento()) 
            || request.dataLimite().isEqual(request.dataEvento());
    }
}
