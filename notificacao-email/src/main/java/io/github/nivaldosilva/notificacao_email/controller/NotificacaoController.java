package io.github.nivaldosilva.notificacao_email.controller;

import io.github.nivaldosilva.notificacao_email.service.EmailService;
import io.github.nivaldosilva.notificacao_email.dto.NotificacaoRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class NotificacaoController {

    private final EmailService emailService;

    @PostMapping("/notificacoes")
    public ResponseEntity<Void> enviarNotificacao(@Valid @RequestBody NotificacaoRequest request) {
        emailService.enviarEmail(request);
        return ResponseEntity.ok().build();
    }

}
