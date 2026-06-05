package io.github.nivaldosilva.cadastro_usuarios.entrypoint.handler;

import io.github.nivaldosilva.cadastro_usuarios.application.exception.RecursoNaoEncontradoException;
import io.github.nivaldosilva.cadastro_usuarios.application.exception.RegistroDuplicadoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	public ProblemDetail handleRecursoNaoEncontradoException(
			RecursoNaoEncontradoException ex,
			WebRequest request) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
				HttpStatus.NOT_FOUND,
				ex.getMessage() != null ? ex.getMessage() : "O recurso solicitado não pôde ser encontrado."
		);

		problemDetail.setTitle("Recurso não encontrado");
		problemDetail.setType(URI.create("https://api.exemplo.com/erros/nao-encontrado"));
		problemDetail.setProperty("timestamp", Instant.now());
		problemDetail.setProperty("caminho", request.getDescription(false).replace("uri=", ""));

		return problemDetail;
	}

	@ExceptionHandler(RegistroDuplicadoException.class)
	public ProblemDetail handleConflitoException(
			RegistroDuplicadoException ex,
			WebRequest request) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
				HttpStatus.CONFLICT,
				ex.getMessage() != null ? ex.getMessage() : "Houve um conflito com um registro existente."
		);

		problemDetail.setTitle("Conflito de dados");
		problemDetail.setType(URI.create("https://api.exemplo.com/erros/conflito"));
		problemDetail.setProperty("timestamp", Instant.now());
		problemDetail.setProperty("caminho", request.getDescription(false).replace("uri=", ""));

		return problemDetail;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidacaoException(
			MethodArgumentNotValidException ex,
			WebRequest request) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
				HttpStatus.BAD_REQUEST,
				"A validação falhou em um ou mais campos."
		);

		problemDetail.setTitle("Erro de validação");
		problemDetail.setType(URI.create("https://api.exemplo.com/erros/validacao"));
		problemDetail.setProperty("timestamp", Instant.now());
		problemDetail.setProperty("caminho", request.getDescription(false).replace("uri=", ""));

		Map<String, String> errosCampos = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String nomeCampo = ((FieldError) error).getField();
			String mensagemErro = error.getDefaultMessage();
			errosCampos.put(nomeCampo, mensagemErro);
		});

		problemDetail.setProperty("errosCampos", errosCampos);

		return problemDetail;
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ProblemDetail handleAcessoNegadoException(
			AccessDeniedException ex,
			WebRequest request) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
				HttpStatus.FORBIDDEN,
				"Acesso negado. Você não tem permissão para executar esta ação."
		);

		problemDetail.setTitle("Acesso negado");
		problemDetail.setType(URI.create("https://api.exemplo.com/erros/proibido"));
		problemDetail.setProperty("timestamp", Instant.now());
		problemDetail.setProperty("caminho", request.getDescription(false).replace("uri=", ""));

		return problemDetail;
	}

	@ExceptionHandler(AuthenticationException.class)
	public ProblemDetail handleAutenticacaoException(
			AuthenticationException ex,
			WebRequest request) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
				HttpStatus.UNAUTHORIZED,
				"Credenciais inválidas. Por favor, verifique seu e-mail e senha."
		);

		problemDetail.setTitle("Não autorizado");
		problemDetail.setType(URI.create("https://api.exemplo.com/erros/nao-autorizado"));
		problemDetail.setProperty("timestamp", Instant.now());
		problemDetail.setProperty("caminho", request.getDescription(false).replace("uri=", ""));

		return problemDetail;
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleErroGlobalException(
			Exception ex,
			WebRequest request) {

		String caminho = request.getDescription(false).replace("uri=", "");
		log.error("Erro interno do servidor no caminho: {}", caminho, ex);

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
				HttpStatus.INTERNAL_SERVER_ERROR,
				"Ocorreu um erro inesperado no servidor. Tente novamente mais tarde."
		);

		problemDetail.setTitle("Erro interno do servidor");
		problemDetail.setType(URI.create("https://api.exemplo.com/erros/interno"));
		problemDetail.setProperty("timestamp", Instant.now());
		problemDetail.setProperty("caminho", caminho);

		return problemDetail;
	}
}