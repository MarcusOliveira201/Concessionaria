package com.entrevista.api_concessionaria.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.entrevista.api_concessionaria.dto.IndicadoresProjection;
import com.entrevista.api_concessionaria.model.Solicitacao;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>{
    @Query("SELECT s FROM Solicitacao s LEFT JOIN FETCH s.funcionario WHERE s.id = :id")
    Optional<Solicitacao> findById(@Param("id") Long id);

    @Query(value = """
            SELECT 
                COUNT(*) AS totalSolicitacoes,
                SUM(CASE WHEN status <> 'CONCLUIDA' THEN 1 ELSE 0 END) AS totalPendentes,
                SUM(CASE WHEN status = 'CONCLUIDA' THEN 1 ELSE 0 END) AS totalConcluidas,
                SUM(CASE WHEN tipo = 'RECLAMACAO' THEN 1 ELSE 0 END) AS totalReclamacoes,
                SUM(CASE WHEN tipo = 'REVISAO_META' THEN 1 ELSE 0 END) AS totalRevisoesMetas,
                CASE 
                    WHEN COUNT(*) = 0 THEN 0 
                    ELSE ROUND((SUM(CASE WHEN status = 'CONCLUIDA' THEN 1 ELSE 0 END) * 100.0) / COUNT(*), 2)
                END AS percentualConcluidas
            FROM concessionaria.solicitacao;
        """, nativeQuery = true)
    IndicadoresProjection buscarIndicadores();
}
