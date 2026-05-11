package com.entrevista.api_concessionaria.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AnaliseDto(
    
    BigDecimal novoValorKwhSolicitado,

    @Schema(example = "Me parece que é possível aprovar essa solicitação")
    @NotBlank(message = "O parecer deve ser informado")
    String parecer,

    @Schema(example = "13")
    @NotNull(message = "O funcionário deve ser informado")
    Long funcionarioId
){
}
