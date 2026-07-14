package io.github.nivaldosilva.bff_agendador_tarefas.infrastructure.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@io.swagger.v3.oas.annotations.security.SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT"
)
public class OpenAPIConfig {

	@Bean
	public OpenAPI customOpenAPI() {

		final String securitySchemeName = "bearerAuth";

		return new OpenAPI()

				.info(new Info()
						.title("API BFF - Sistema de Agendamento de Tarefas")
						.version("v1")
						.description("""
                                API responsável por centralizar e orquestrar a comunicação entre
                                as aplicações clientes e os microsserviços da plataforma,
                                disponibilizando um ponto único de acesso para autenticação,
                                gerenciamento de usuários, gerenciamento de tarefas e demais
                                funcionalidades do sistema.

                                A autenticação é realizada por meio de tokens JWT enviados no
                                cabeçalho Authorization utilizando o esquema Bearer Token.
                                """)
						.contact(new Contact()
								.name("Nivaldo Silva")
								.url("https://github.com/Nivaldo-Silva")))

				.servers(List.of(
						new Server()
								.url("http://localhost:8085")
								.description("Ambiente Local")
				))

				.addSecurityItem(new SecurityRequirement()
						.addList(securitySchemeName));
	}
}