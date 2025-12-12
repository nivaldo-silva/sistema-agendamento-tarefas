package io.github.nivaldosilva.bff_agendador_tarefas.client;

import io.github.nivaldosilva.bff_agendador_tarefas.dto.request.NotificacaoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "notificacao-client", url = "${microservices.notificacao-email.url}")
public interface NotificacaoClient {

    @PostMapping("/notificacoes")
    ResponseEntity<Void> enviarNotificacao(
            @RequestHeader("X-Internal-Request") String internalHeader,
            @RequestBody NotificacaoRequest request
    );
}


