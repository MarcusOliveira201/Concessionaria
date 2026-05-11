package com.entrevista.api_concessionaria.dto;

import java.sql.Date;
import java.util.List;

import com.entrevista.api_concessionaria.enums.StatusSolicitacao;
import com.entrevista.api_concessionaria.enums.TipoSolicitacao;
import com.entrevista.api_concessionaria.model.Solicitacao;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Builder
public record SolicitacaoDto(
    @NotNull(message = "O tipo deve ser informado")
    @Schema(example = "RECLAMACAO")
    TipoSolicitacao tipo,    
    
    @NotBlank(message = "O CPF do consumidor é obrigatório")
    @Schema(example = "55522233344")
    String cpfConsumidor,

    @NotNull(message = "O ID do funcionário é obrigatório")
    @Schema(example = "13")
    Long funcionarioId
) {
}
