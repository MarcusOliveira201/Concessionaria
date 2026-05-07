package com.entrevista.api_concessionaria.repository;

import com.entrevista.api_concessionaria.model.Analise;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface AnaliseRepository extends JpaRepository<Analise, Long> {

}
