package com.entrevista.api_concessionaria.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegistroAnaliseDto(
    @NotNull(message = "O parecer é obrigatório")
    String parecer,
    @NotNull(message = "O ID do funcionário é obrigatório")
    Long funcionarioId,
    BigDecimal novoValorKwhSolicitado
) {
}
