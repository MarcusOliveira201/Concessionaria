package com.entrevista.api_concessionaria.dto;

import com.entrevista.api_concessionaria.enums.DecisaoGerente;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DecisaoDto(

    @NotNull(message = "A decisão é obrigatória")
    @Schema(example = "APROVADA ou REPROVADA")
    DecisaoGerente decisao,

    @NotNull(message = "A resposta final é obrigatória")   
    @Schema(example = "Resposta final para o Consumidor") 
    String respostaFinal,

    @NotNull(message = "O id do funcionário é obrigatório")
    @Schema(example = "13")
    Long funcionarioId
) {
}
