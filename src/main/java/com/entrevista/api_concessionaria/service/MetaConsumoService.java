package com.entrevista.api_concessionaria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entrevista.api_concessionaria.repository.MetaConsumoRepository;
import com.entrevista.api_concessionaria.model.MetaConsumo;
import java.util.Optional;

@Service
public class MetaConsumoService {
    
    @Autowired
    private MetaConsumoRepository repository;

    public Optional<MetaConsumo> buscarMeta(Long consumidorId, Integer mes, Integer ano) {
        return repository.findByConsumidorIdAndMesAndAno(consumidorId, mes, ano);
    }
}
