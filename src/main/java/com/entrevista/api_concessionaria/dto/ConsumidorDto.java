package com.entrevista.api_concessionaria.dto;

import com.entrevista.api_concessionaria.model.Consumidor;

import lombok.Builder;

@Builder
public record ConsumidorDto(
    String nome,
    String cpf,
    String email,
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