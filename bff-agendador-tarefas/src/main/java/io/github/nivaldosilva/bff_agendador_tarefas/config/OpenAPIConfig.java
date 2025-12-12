package io.github.nivaldosilva.bff_agendador_tarefas.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Autenticação JWT. Obtenha o token através do endpoint /login e inclua no header: Authorization: Bearer {token}"
)
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact()
                .email("nivaldosilva.contato@gmail.com")
                .name("Nivaldo Silva")
                .url("https://github.com/Nivaldo-Silva");

        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");

        Info info = new Info()
                .title("Sistema de Agendamento de Tarefas - BFF API")
                .version("v1.0.0")
                .description("""
                        API Gateway (Backend-for-Frontend) para o sistema de agendamento de tarefas.
                        
                        ## Autenticação
                        Esta API utiliza autenticação JWT. Para obter um token:
                        1. Faça login através do endpoint `/login`
                        2. Use o token retornado no header `Authorization: Bearer {token}`
                        
                        ## Funcionalidades
                        - **Autenticação**: Login e registro de usuários
                        - **Tarefas**: Criação, listagem, busca, atualização e exclusão de tarefas
                        - **Notificações**: Envio automático de emails para tarefas agendadas
                        
                        ## Arquitetura
                        Este BFF orquestra chamadas para os seguintes microserviços:
                        - **Cadastro de Usuários** (porta 8082): Gerenciamento de usuários e autenticação
                        - **Agendador de Tarefas** (porta 8083): Gerenciamento de tarefas
                        - **Notificação Email** (porta 8084): Envio de notificações por email
                        """)
                .contact(contact)
                .license(license);

        Server localServer = new Server()
                .url("http://localhost:8085")
                .description("Servidor de Desenvolvimento Local");

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}
