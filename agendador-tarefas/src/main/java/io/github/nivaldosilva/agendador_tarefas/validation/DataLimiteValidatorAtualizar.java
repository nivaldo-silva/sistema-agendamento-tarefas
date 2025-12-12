package io.github.nivaldosilva.agendador_tarefas.validation;

import io.github.nivaldosilva.agendador_tarefas.dto.request.AtualizarTarefaRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DataLimiteValidatorAtualizar implements ConstraintValidator<DataLimiteValida, AtualizarTarefaRequest> {

    @Override
    public boolean isValid(AtualizarTarefaRequest request, ConstraintValidatorContext context) {
        
        if (request == null || request.dataEvento() == null || request.dataLimite() == null) {
            return true;
        }
        return request.dataLimite().isAfter(request.dataEvento()) 
            || request.dataLimite().isEqual(request.dataEvento());
    }
}