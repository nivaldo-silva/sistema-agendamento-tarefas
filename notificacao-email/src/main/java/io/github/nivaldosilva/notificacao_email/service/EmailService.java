package io.github.nivaldosilva.notificacao_email.service;

import io.github.nivaldosilva.notificacao_email.dto.NotificacaoRequest;
import io.github.nivaldosilva.notificacao_email.exception.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private  final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${envio.email.remetente}")
    public String remetente;

    @Value("${envio.email.nomeRemetente}")
    public String nomeRemetente;

    public void enviarEmail(NotificacaoRequest request) {
        try {
            MimeMessage mensagem = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, StandardCharsets.UTF_8.name());

            helper.setFrom(new InternetAddress(remetente, nomeRemetente));
            helper.setTo(request.emailDestinatario());
            helper.setSubject("Nova Tarefa Agendada: " + request.tituloTarefa());

            org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
            context.setVariable("tituloTarefa", request.tituloTarefa());
            context.setVariable("descricaoTarefa", request.descricaoTarefa());
            context.setVariable("dataEvento", request.dataEvento());
            context.setVariable("prioridade", request.prioridade());

            String corpoEmail = templateEngine.process("notificacao", context);
            helper.setText(corpoEmail, true);

            javaMailSender.send(mensagem);
            log.info("Email enviado com sucesso para: {}", request.emailDestinatario());

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException("Erro ao enviar o email ", e.getCause());
        }
    }
}
