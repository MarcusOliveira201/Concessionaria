package com.entrevista.api_concessionaria.dto;

import com.entrevista.api_concessionaria.model.Consumidor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ConsumidorDto(
    @Schema(example = "Milena")
    @NotBlank(message = "O nome deve ser informado")
    String nome,

    @Schema(example = "11122233344")
    @NotBlank(message = "O CPF deve ser informado")
    String cpf,

    @Schema(example = "maria@gmail.com")
    @NotBlank(message = "O email deve ser informado")
    String email,

    @Schema(example = "Rua Sessenta")
    @NotBlank(message = "O endereço deve ser informado")
    String endereco
){

    public static ConsumidorDto from(Consumidor consumidor) {
    if (consumidor == null) return null;

        return ConsumidorDto.builder()
                .nome(consumidor.getNome())
                .cpf(consumidor.getCpf())
                .email(consumidor.getEmail())
                .endereco(consumidor.getEndereco())
                .build();
    }
}