package com.entrevista.api_concessionaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entrevista.api_concessionaria.model.MetaConsumo;
import java.util.Optional;

public interface MetaConsumoRepository extends JpaRepository<MetaConsumo, Long>{
    
    boolean existsByConsumidorIdAndMesAndAno(Long consumidorId, Integer mes, Integer ano);

    Optional<MetaConsumo> findByConsumidorIdAndMesAndAno(Long consumidorId, Integer mes, Integer ano);
}
