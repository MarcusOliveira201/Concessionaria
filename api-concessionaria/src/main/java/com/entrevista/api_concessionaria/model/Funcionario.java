package com.entrevista.api_concessionaria.model;
import java.util.List;

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

    @Column(name = "nome", length = 150)
    private String nome;

    @Column(name = "perfil", length = 20)
    private String perfil;

    @Column(name = "area", length = 100)
    private String area;

    @OneToMany
    @JoinColumn(name = "funcionario_id")
    private List<Analise> analises;


}