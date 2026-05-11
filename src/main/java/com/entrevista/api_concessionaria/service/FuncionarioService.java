package com.entrevista.api_concessionaria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entrevista.api_concessionaria.dto.FuncionarioDto;
import com.entrevista.api_concessionaria.dto.SolicitacaoRespostaDto;
import com.entrevista.api_concessionaria.enums.PerfilFuncionario;
import com.entrevista.api_concessionaria.exception.ServiceRunTimeException;
import com.entrevista.api_concessionaria.model.Funcionario;
import com.entrevista.api_concessionaria.model.Solicitacao;
import com.entrevista.api_concessionaria.repository.FuncionarioRepository;

import jakarta.transaction.Transactional;

@Service
public class FuncionarioService {
    @Autowired
    private FuncionarioRepository repo;

    public Funcionario validarSeEhGerenteERetorna(Long funcionarioId) {
        Funcionario funcionario = repo.findById(funcionarioId)
            .orElseThrow(() -> new ServiceRunTimeException("Funcionário não encontrado"));
            
        if (!funcionario.getPerfil().equals(PerfilFuncionario.GERENTE)) {
            throw new ServiceRunTimeException("Apenas gerentes podem realizar esta operação.");
        }

        return funcionario;
    }

    public Funcionario validarSeEhAnalistaERetorna(Long funcionarioId) {
        Funcionario funcionario = repo.findById(funcionarioId)
            .orElseThrow(() -> new ServiceRunTimeException("Funcionário não encontrado"));
            
        if ((!funcionario.getPerfil().equals(PerfilFuncionario.ANALISTA)) && (!funcionario.getPerfil().equals(PerfilFuncionario.GERENTE))) {
            throw new ServiceRunTimeException("Apenas Analistas ou Gerentes podem realizar esta operação.");
        }

        return funcionario;
    }

    public List<FuncionarioDto> buscar() {
        List<Funcionario> solicitacoes = repo.findAll();
        
        return solicitacoes.stream()
                .map(FuncionarioDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void salvarNovoFuncionário(FuncionarioDto dto) {
        Funcionario funcionario = converterParaEntidade(dto);
        repo.save(funcionario);
    }

    private Funcionario converterParaEntidade(FuncionarioDto dto) {
        return Funcionario.builder()
                .nome(dto.nome())
                .area(dto.area())
                .perfil(dto.perfil())
                .build();
    }
}
