package com.entrevista.api_concessionaria.dto;

public record IndicadoresDto(
    Long totalSolicitacoes,
    Long totalPendentes,
    Long totalConcluidas,
    Long totalReclamacoes,
    Long totalRevisoesMetas,
    Double percentualConcluidas
) {
}
