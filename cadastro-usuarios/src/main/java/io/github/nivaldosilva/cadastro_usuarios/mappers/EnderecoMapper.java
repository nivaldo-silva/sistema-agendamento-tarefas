package io.github.nivaldosilva.cadastro_usuarios.mappers;

import io.github.nivaldosilva.cadastro_usuarios.dto.request.EnderecoRequest;
import io.github.nivaldosilva.cadastro_usuarios.dto.response.EnderecoResponse;
import io.github.nivaldosilva.cadastro_usuarios.entity.Endereco;
import lombok.experimental.UtilityClass;
import java.util.List;

@UtilityClass
public class EnderecoMapper {

    public static Endereco toEntity(EnderecoRequest request) {
        return Endereco.builder()
                .rua(request.rua())
                .numero(request.numero())
                .cidade(request.cidade())
                .complemento(request.complemento())
                .bairro(request.bairro())
                .cep(request.cep())
                .estado(request.estado())
                .build();
    }

    public static EnderecoResponse toResponse(Endereco endereco) {
        return new EnderecoResponse(
                endereco.getId(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
    }

    public static List<EnderecoResponse> toResponseList(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(EnderecoMapper::toResponse)
                .toList();
    }
}