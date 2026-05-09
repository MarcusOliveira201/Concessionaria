package com.entrevista.api_concessionaria.repository;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.entrevista.api_concessionaria.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{

    @Query("SELECT f FROM Funcionario f ORDER BY function('random')")
    List<Funcionario> findRandom(Pageable pageable);
}