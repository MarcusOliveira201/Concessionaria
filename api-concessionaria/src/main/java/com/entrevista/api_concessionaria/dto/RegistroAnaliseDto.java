package com.entrevista.api_concessionaria.dto;

import java.math.BigDecimal;

import com.entrevista.api_concessionaria.enums.TipoSolicitacao;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistroAnaliseDto {
    @NotNull(message = "O parecer é obrigatório")
    private String parecer;

    @NotNull(message = "O ID do funcionário é obrigatório")
    private Long funcionarioId;

    private BigDecimal novoValorKwhSolicitado;
}
