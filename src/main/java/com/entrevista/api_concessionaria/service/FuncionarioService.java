package com.entrevista.api_concessionaria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entrevista.api_concessionaria.dto.FuncionarioDto;
import com.entrevista.api_concessionaria.enums.PerfilFuncionario;
import com.entrevista.api_concessionaria.exception.ServiceRunTimeException;
import com.entrevista.api_concessionaria.model.Funcionario;
import com.entrevista.api_concessionaria.repository.FuncionarioRepository;

import jakarta.transaction.Transactional;

@Service
public class FuncionarioService {
    @Autowired
    private FuncionarioRepository repository;

    public Funcionario validarSeEhGerenteERetorna(Long funcionarioId) {
        Funcionario funcionario = repository.findById(funcionarioId)
            .orElseThrow(() -> new ServiceRunTimeException("Funcionário não encontrado"));
            
        if (!funcionario.getPerfil().equals(PerfilFuncionario.GERENTE)) {
            throw new ServiceRunTimeException("Apenas gerentes podem realizar esta operação.");
        }

        return funcionario;
    }

    @Transactional
    public void salvarNovoFuncionário(FuncionarioDto dto) {
        Funcionario funcionario = converterParaEntidade(dto);
        repository.save(funcionario);
    }

    private Funcionario converterParaEntidade(FuncionarioDto dto) {
        return Funcionario.builder()
                .nome(dto.nome())
                .area(dto.area())
                .perfil(dto.perfil())
                .build();
    }
}
