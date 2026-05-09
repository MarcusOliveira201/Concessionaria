package com.entrevista.api_concessionaria.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrevista.api_concessionaria.dto.RegistroAnaliseDto;
import com.entrevista.api_concessionaria.dto.SolicitacaoDto;
import com.entrevista.api_concessionaria.service.SolicitacoesService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/")
public class SolicitacaoController {

    @Autowired
    private SolicitacoesService service;

    @PostMapping("solicitacoes")
    public ResponseEntity<?> salvarUmaSolicitacao(@Valid @RequestBody SolicitacaoDto dto) {
        SolicitacaoDto solicitacaoSalva = service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitacaoSalva);
    }
    
    @GetMapping("solicitacoes")
    public ResponseEntity<?> buscarTodas() {
        List<SolicitacaoDto> solicitacoes = service.buscar();
        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("solicitacoes/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        SolicitacaoDto solicitacao = service.buscarPorId(id);
        return ResponseEntity.ok(solicitacao);
    }

    @PostMapping("solicitacoes/{id}/analise")
    public ResponseEntity<?> registrarAnalise(@PathVariable Long id,@RequestBody @Valid RegistroAnaliseDto dto) {
        service.registrarAnalise(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
