package com.entrevista.api_concessionaria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrevista.api_concessionaria.dto.FuncionarioDto;
import com.entrevista.api_concessionaria.dto.SolicitacaoRespostaDto;
import com.entrevista.api_concessionaria.service.FuncionarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
@Tag(name = "Funcionario", description = "Endpoint Criado para facilitar na hora de fazer o teste da aplicação, pois requer um funcionário.")
public class FuncionarioController {

    @Autowired
    private FuncionarioService service;
    
    @Operation(summary = "Salva um funcionário", description = "### Campos Obrigatórios:\n" +
                  "* **Nome**: O nome do funcionário.\n" +
                  "* **Perfil**: ATENDENTE,ANALISTA,GERENTE;"+
                  "* **Area**: Área de atuação do funcionário.")
    @PostMapping("funcionarios")
    public ResponseEntity<Void> salvarNovoFuncionario(@Valid @RequestBody FuncionarioDto dto) {
        service.salvarNovoFuncionário(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Busca Funcionarios",description="Busca todos os funcionarios que estão salvos.")
    @GetMapping("funcionarios")
    public ResponseEntity<?> buscarTodas() {
        List<FuncionarioDto> funcionarios = service.buscar();
        return ResponseEntity.ok(funcionarios);
    }
}
