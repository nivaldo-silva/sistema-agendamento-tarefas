package io.github.nivaldosilva.agendador_tarefas.repository;

import io.github.nivaldosilva.agendador_tarefas.entity.Tarefa;
import io.github.nivaldosilva.agendador_tarefas.enums.StatusNotificacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TarefaRepository extends MongoRepository<Tarefa, String> {

    List<Tarefa> findAllByEmailUsuario(String email);
    
    List<Tarefa> findAllByEmailUsuarioAndDataEventoBetween(
        String email, LocalDateTime inicio, LocalDateTime fim);
    
    @Query("{'data_evento': {$gte: ?0, $lte: ?1}, 'status_notificacao': ?2}")
    List<Tarefa> findTarefasParaNotificar(
        LocalDateTime inicio, LocalDateTime fim, StatusNotificacao status);

    @Query("{'data_limite': {$gte: ?0, $lte: ?1}, 'status': {$ne: 'CONCLUIDA'}, 'status_notificacao': 'PENDENTE'}")
    List<Tarefa> findTarefasParaLembrete(LocalDateTime inicio, LocalDateTime fim);
   
}
