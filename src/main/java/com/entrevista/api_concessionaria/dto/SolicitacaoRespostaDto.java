package com.entrevista.api_concessionaria.dto;

import java.io.ObjectInputFilter.Status;
import java.sql.Date;
import java.util.List;

import com.entrevista.api_concessionaria.enums.StatusSolicitacao;
import com.entrevista.api_concessionaria.enums.TipoSolicitacao;
import com.entrevista.api_concessionaria.model.Solicitacao;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SolicitacaoRespostaDto(
    Long id,
    Date dataAbertura,
    TipoSolicitacao tipo,    
    String respostaFinal,
    StatusSolicitacao status,
    String cpfConsumidor,
    ConsumidorDto consumidor,
    Long funcionarioId,
    List<AnaliseRespostaDto> analises
) {
    public static SolicitacaoRespostaDto from(Solicitacao solicitacao) {
        if (solicitacao == null) return null;
        
        return SolicitacaoRespostaDto.builder()
                .id(solicitacao.getId())
                .dataAbertura(solicitacao.getDataAbertura())
                .tipo(solicitacao.getTipo())
                .status(solicitacao.getStatus())
                .respostaFinal(solicitacao.getRespostaFinal())
                .cpfConsumidor(solicitacao.getConsumidor() != null ? solicitacao.getConsumidor().getCpf() : null)
                .consumidor(ConsumidorDto.from(solicitacao.getConsumidor()))
                .funcionarioId(solicitacao.getFuncionario() != null ? solicitacao.getFuncionario().getId() : null)
                .analises(AnaliseRespostaDto.from(solicitacao.getAnalises()))
                .build();
    }
}
