package io.github.nivaldosilva.cadastro_usuarios.mappers;

import io.github.nivaldosilva.cadastro_usuarios.dto.request.TelefoneRequest;
import io.github.nivaldosilva.cadastro_usuarios.dto.response.TelefoneResponse;
import io.github.nivaldosilva.cadastro_usuarios.entity.Telefone;
import lombok.experimental.UtilityClass;
import java.util.List;

@UtilityClass
public class TelefoneMapper {

    public static Telefone toEntity(TelefoneRequest request) {
        return Telefone.builder()
                .numero(request.numero())
                .ddd(request.ddd())
                .build();
    }

    public static TelefoneResponse toResponse(Telefone telefone) {
        return new TelefoneResponse(
                telefone.getId(),
                telefone.getNumero(),
                telefone.getDdd()
        );
    }

    public static List<TelefoneResponse> toResponseList(List<Telefone> telefones) {
        return telefones.stream()
                .map(TelefoneMapper::toResponse)
                .toList();
    }
}

