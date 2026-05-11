package com.entrevista.api_concessionaria.dto;

import com.entrevista.api_concessionaria.enums.PerfilFuncionario;
import com.entrevista.api_concessionaria.model.Funcionario;

import lombok.Builder;

@Builder
public record FuncionarioDto(
    String nome,
    String area,
    PerfilFuncionario perfil
) {
}
