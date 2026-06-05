package io.github.nivaldosilva.agendador_tarefas.infrastructure.client;

import io.github.nivaldosilva.agendador_tarefas.infrastructure.client.dto.request.NotificacaoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacao-email",
             url = "${microservices.notificacao-email.url}")
public interface NotificacaoClient {

    @PostMapping("/notificacoes")
    void enviarNotificacao(@RequestBody NotificacaoRequest request);
}
