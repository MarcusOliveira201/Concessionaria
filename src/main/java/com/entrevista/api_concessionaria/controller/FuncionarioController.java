package com.entrevista.api_concessionaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrevista.api_concessionaria.dto.FuncionarioDto;
import com.entrevista.api_concessionaria.service.FuncionarioService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
@Tag(name = "Funcionario", description = "Endpoint Criado para facilitar na hora de fazer o teste da aplicação, pois requer um funcionário.")
public class FuncionarioController {

    @Autowired
    private FuncionarioService service;
    
    @PostMapping("funcionarios")
    public ResponseEntity<Void> salvarNovoFuncionario(@Valid @RequestBody FuncionarioDto dto) {
        service.salvarNovoFuncionário(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
