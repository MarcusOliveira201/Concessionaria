package com.entrevista.api_concessionaria.dto;

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
public record SolicitacaoDto(
    Long id,
    Date dataAbertura,
    @NotNull(message = "O tipo deve ser informado")
    TipoSolicitacao tipo,    
    StatusSolicitacao status,
    String respostaFinal,
    @NotBlank(message = "O CPF do consumidor é obrigatório")
    String cpfConsumidor,
    ConsumidorDto consumidor,
    Long funcionarioId,
    List<AnaliseDto> analises
) {
    public static SolicitacaoDto from(Solicitacao solicitacao) {
        if (solicitacao == null) return null;
        
        return SolicitacaoDto.builder()
                .id(solicitacao.getId())
                .dataAbertura(solicitacao.getDataAbertura())
                .tipo(solicitacao.getTipo())
                .status(solicitacao.getStatus())
                .respostaFinal(solicitacao.getRespostaFinal())
                .cpfConsumidor(solicitacao.getConsumidor() != null ? solicitacao.getConsumidor().getCpf() : null)
                .consumidor(ConsumidorDto.from(solicitacao.getConsumidor()))
                .funcionarioId(solicitacao.getFuncionario() != null ? solicitacao.getFuncionario().getId() : null)
                .analises(AnaliseDto.from(solicitacao.getAnalises()))
                .build();
    }
}
