package com.entrevista.api_concessionaria.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.entrevista.api_concessionaria.model.Solicitacao;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>{
}
