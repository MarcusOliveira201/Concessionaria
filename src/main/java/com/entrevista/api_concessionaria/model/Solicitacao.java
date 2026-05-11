package com.entrevista.api_concessionaria.model;

import java.sql.Date;
import java.util.List;

import com.entrevista.api_concessionaria.enums.StatusSolicitacao;
import com.entrevista.api_concessionaria.enums.TipoSolicitacao;
import com.entrevista.api_concessionaria.exception.ServiceRunTimeException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitacao")
public class Solicitacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_abertura")
    private Date dataAbertura;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoSolicitacao tipo;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusSolicitacao status;

    @ManyToOne
    @JoinColumn(name = "consumidor_id", nullable = false)
    private Consumidor consumidor;

    @ManyToOne
    @JoinColumn(name = "funcionario_responsavel_id", nullable = false)
    private Funcionario funcionario;

    @Column(name = "data_conclusao")
    private Date dataConclusao;

    @Column(name = "resposta_final")
    private String respostaFinal;

    @OneToMany
    @JoinColumn(name = "solicitacao_id")
    private List<Analise> analises;

    public void garantirQuePodeSerAlterada() {
        if (this.status == StatusSolicitacao.CONCLUIDA) {
            throw new ServiceRunTimeException("A solicitação está concluída.");
        }
    }
}
