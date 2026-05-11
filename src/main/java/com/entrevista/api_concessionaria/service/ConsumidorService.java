package com.entrevista.api_concessionaria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entrevista.api_concessionaria.dto.ConsumidorDto;
import com.entrevista.api_concessionaria.model.Consumidor;
import com.entrevista.api_concessionaria.repository.ConsumidorRepository;

import jakarta.transaction.Transactional;

@Service
public class ConsumidorService {
    
    @Autowired
    private ConsumidorRepository repository;

    @Transactional
    public void salvarNovoConsumidor(ConsumidorDto dto) {
        Consumidor consumidor = converterParaEntidade(dto);
        repository.save(consumidor);
    }

    private Consumidor converterParaEntidade(ConsumidorDto dto) {
        return Consumidor.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .email(dto.email())
                .endereco(dto.endereco())
                .build();
    }
}
