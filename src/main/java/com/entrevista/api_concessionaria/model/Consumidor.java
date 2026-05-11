package com.entrevista.api_concessionaria.model;
import java.util.List;

import com.entrevista.api_concessionaria.enums.StatusSolicitacao;
import com.entrevista.api_concessionaria.exception.ServiceRunTimeException;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consumidor")
public class Consumidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf", unique = true)
    @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos numéricos")
    private String cpf;

    @Column(name = "email")
    private String email;

    @Column(name = "endereco")
    private String endereco;

    @OneToMany
    @JoinColumn(name = "consumidor_id")
    private List<Solicitacao> solicitacoes;

    @OneToMany
    @JoinColumn(name = "consumidor_id")
    private List<MetaConsumo> metasConsumo;
}