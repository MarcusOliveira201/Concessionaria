package com.entrevista.api_concessionaria.dto;

import com.entrevista.api_concessionaria.model.Consumidor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsumidorDto {
    private String nome;
    private String cpf;
    private String email;

    public static ConsumidorDto from(Consumidor consumidor) {
    if (consumidor == null) return null;

        return ConsumidorDto.builder()
                .nome(consumidor.getNome())
                .cpf(consumidor.getCpf())
                .email(consumidor.getEmail())
                .build();
    }
}