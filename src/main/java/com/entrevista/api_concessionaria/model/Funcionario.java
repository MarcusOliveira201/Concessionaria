package com.entrevista.api_concessionaria.model;
import java.util.List;

import com.entrevista.api_concessionaria.enums.PerfilFuncionario;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "perfil")
    @Enumerated(EnumType.STRING)
    private PerfilFuncionario perfil;

    @Column(name = "area")
    private String area;

    @OneToMany
    @JoinColumn(name = "funcionario_id")
    private List<Analise> analises;

    @OneToMany
    @JoinColumn(name = "funcionario_responsavel_id")
    private List<Solicitacao> solicitacoes;


}