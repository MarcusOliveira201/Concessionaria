package com.entrevista.api_concessionaria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entrevista.api_concessionaria.enums.PerfilFuncionario;
import com.entrevista.api_concessionaria.exception.ServiceRunTimeException;
import com.entrevista.api_concessionaria.model.Funcionario;
import com.entrevista.api_concessionaria.repository.FuncionarioRepository;

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
}
