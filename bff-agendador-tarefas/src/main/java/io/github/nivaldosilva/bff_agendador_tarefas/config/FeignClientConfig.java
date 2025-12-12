package io.github.nivaldosilva.bff_agendador_tarefas.config;

import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class FeignClientConfig {

    private static final Logger log = LoggerFactory.getLogger(FeignClientConfig.class);

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            log.debug("Processando requisição Feign para URL: {}", requestTemplate.url());


            if (requestTemplate.url().contains("/notificacoes")) {
                log.debug("Requisição interna detectada, pulando adição de JWT");
                requestTemplate.header("X-Internal-Request", "true");
                return;
            }


            if (!requestTemplate.headers().containsKey("Authorization")) {
                var authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
                    String tokenValue = jwtAuthenticationToken.getToken().getTokenValue();
                    requestTemplate.header("Authorization", "Bearer " + tokenValue);
                    log.debug("Cabeçalho de autorização adicionado");
                } else {
                    log.debug("Nenhuma autenticação JWT encontrada no contexto de segurança");
                }
            } else {
                log.debug("Cabeçalho de autorização já existente na requisição");
            }
        };
    }
}