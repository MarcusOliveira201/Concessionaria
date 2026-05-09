package com.entrevista.api_concessionaria.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrevista.api_concessionaria.dto.SolicitacaoDto;
import com.entrevista.api_concessionaria.model.Solicitacao;
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
    private SolicitacoesService solicitacoesService;

    @PostMapping("solicitacoes")
    public ResponseEntity<SolicitacaoDto> salvarUmaSolicitacao(@Valid @RequestBody SolicitacaoDto dto) {
        SolicitacaoDto solicitacaoSalva = solicitacoesService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitacaoSalva);
    }
    
    @GetMapping("solicitacoes")
    public ResponseEntity<List<SolicitacaoDto>> buscarTodas() {
        List<SolicitacaoDto> solicitacoes = solicitacoesService.buscar();
        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("solicitacoes/{id}")
    public ResponseEntity<SolicitacaoDto> buscarPorId(@PathVariable Long id) {
        SolicitacaoDto solicitacao = solicitacoesService.buscarPorId(id);
        return ResponseEntity.ok(solicitacao);
    }
}
