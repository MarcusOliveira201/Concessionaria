package com.entrevista.api_concessionaria.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entrevista.api_concessionaria.model.Solicitacao;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>{
    @Query("SELECT s FROM Solicitacao s LEFT JOIN FETCH s.funcionario WHERE s.id = :id")
    Optional<Solicitacao> findById(@Param("id") Long id);
}
