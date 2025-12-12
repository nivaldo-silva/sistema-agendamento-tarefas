package io.github.nivaldosilva.agendador_tarefas.security;

import io.github.nivaldosilva.agendador_tarefas.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("tarefaSecurity")
@RequiredArgsConstructor
public class TarefaSecurity {

    private final TarefaRepository tarefaRepository;

    public boolean isOwner(Authentication authentication, @NonNull String taskId) {
        if (authentication == null || taskId == null || taskId.isBlank()) {
            return false;
        }
        
        String loggedInUserEmail = authentication.getName();
        if (loggedInUserEmail == null) {
            return false;
        }
        
        return tarefaRepository.findById(taskId)
                .map(tarefa -> tarefa.getEmailUsuario().equals(loggedInUserEmail))
                .orElse(false);
    }
}