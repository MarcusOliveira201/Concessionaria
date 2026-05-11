package com.entrevista.api_concessionaria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entrevista.api_concessionaria.dto.ConsumidorDto;
import com.entrevista.api_concessionaria.dto.FuncionarioDto;
import com.entrevista.api_concessionaria.model.Consumidor;
import com.entrevista.api_concessionaria.model.Funcionario;
import com.entrevista.api_concessionaria.repository.ConsumidorRepository;

import jakarta.transaction.Transactional;

@Service
public class ConsumidorService {
    
    @Autowired
    private ConsumidorRepository repo;

    public List<ConsumidorDto> buscar() {
        List<Consumidor> solicitacoes = repo.findAll();
        
        return solicitacoes.stream()
                .map(ConsumidorDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void salvarNovoConsumidor(ConsumidorDto dto) {
        Consumidor consumidor = converterParaEntidade(dto);
        repo.save(consumidor);
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
