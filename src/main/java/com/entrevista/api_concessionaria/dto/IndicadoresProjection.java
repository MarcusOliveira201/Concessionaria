package com.entrevista.api_concessionaria.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ 
    "totalSolicitacoes", 
    "totalPendentes", 
    "totalConcluidas", 
    "totalReclamacoes", 
    "totalRevisoesMetas", 
    "percentualConcluidas" 
})
public interface IndicadoresProjection {
    Long getTotalSolicitacoes();
    Long getTotalPendentes();
    Long getTotalConcluidas();
    Long getTotalReclamacoes();
    Long getTotalRevisoesMetas();
    Double getPercentualConcluidas();
}
