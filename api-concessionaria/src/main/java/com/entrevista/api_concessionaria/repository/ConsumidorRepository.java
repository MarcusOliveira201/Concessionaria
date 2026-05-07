package com.entrevista.api_concessionaria.repository;

import com.entrevista.api_concessionaria.model.Consumidor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ConsumidorRepository extends JpaRepository<Consumidor, Long> {
}
