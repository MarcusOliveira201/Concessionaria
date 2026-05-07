package com.entrevista.api_concessionaria.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "analise") 
public class Analise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "consumidor_id", nullable = false)
    private Consumidor consumidor;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime dataAnalise;

    @Column(nullable = false)
    private String parecer;

    @Column(nullable = false)
    private BigDecimal novoValorKwhSolicitado;

}
