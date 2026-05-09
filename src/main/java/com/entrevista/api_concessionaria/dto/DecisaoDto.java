package com.entrevista.api_concessionaria.dto;

import com.entrevista.api_concessionaria.enums.DecisaoGerente;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DecisaoDto(
    @NotNull(message = "A decisão é obrigatória")
    DecisaoGerente decisao,
    @NotNull(message = "A resposta final é obrigatória")    
    String respostaFinal,
    @NotNull(message = "O id do funcionário é obrigatório")
    Long funcionarioId
) {
}
