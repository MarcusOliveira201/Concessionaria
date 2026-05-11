package com.entrevista.api_concessionaria.dto;

import com.entrevista.api_concessionaria.enums.PerfilFuncionario;
import com.entrevista.api_concessionaria.model.Funcionario;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FuncionarioDto(
    @Schema(example = "Milena")
    @NotBlank(message = "O nome deve ser informado")
    String nome,
    @Schema(example = "Manutenção")
    @NotBlank(message = "O área deve ser informada")
    String area,
    @Schema(example = "ATENDENTE, ANALISTA, GERENTE")
    @NotNull(message = "O cargo deve ser informado")
    PerfilFuncionario perfil
) {
    public static FuncionarioDto from(Funcionario funcionario){
        if (funcionario == null) return null;
        return FuncionarioDto.builder()
                .nome(funcionario.getNome())
                .area(funcionario.getArea())
                .perfil(funcionario.getPerfil())
                .build();
    }
}
