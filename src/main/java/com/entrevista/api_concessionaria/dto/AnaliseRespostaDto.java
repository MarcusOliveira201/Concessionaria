package com.entrevista.api_concessionaria.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.List;

import com.entrevista.api_concessionaria.model.Analise;

import lombok.Builder;

@Builder
public record AnaliseRespostaDto(
    Long id,
    Date dataAnalise,
    String parecer,
    BigDecimal novoValorKwhSolicitado,
    Long funcionarioId
){

    public static AnaliseRespostaDto from(Analise analise) {
        if (analise == null) return null;

        return AnaliseRespostaDto.builder()
                .id(analise.getId())
                .dataAnalise(analise.getDataAnalise())
                .parecer(analise.getParecer())
                .novoValorKwhSolicitado(analise.getNovoValorKwhSolicitado())
                .funcionarioId(analise.getFuncionario() != null ? analise.getFuncionario().getId() : null)
                .build();
    }

    public static List<AnaliseRespostaDto> from(List<Analise> analises) {
        if (analises == null) return Collections.emptyList();
        
        return analises.stream()
                .map(AnaliseRespostaDto::from)
                .toList();
    }
}
