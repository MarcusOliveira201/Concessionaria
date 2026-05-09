package com.entrevista.api_concessionaria.dto;

import java.sql.Date;
import java.util.List;

import com.entrevista.api_concessionaria.enums.StatusSolicitacao;
import com.entrevista.api_concessionaria.enums.TipoSolicitacao;
import com.entrevista.api_concessionaria.model.Analise;
import com.entrevista.api_concessionaria.model.Consumidor;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolicitacaoDto {

    private Long id;

    private Date dataAbertura;
    
    @NotNull(message = "O tipo deve ser informado")
    private TipoSolicitacao tipo;
    
    private StatusSolicitacao status;

    private String respostaFinal;
    
    @NotBlank(message = "O CPF do consumidor é obrigatório")
    private String cpfConsumidor;

    private ConsumidorDto consumidor;
    
    private Long funcionarioId;

    private List<AnaliseDto> analises;

}
