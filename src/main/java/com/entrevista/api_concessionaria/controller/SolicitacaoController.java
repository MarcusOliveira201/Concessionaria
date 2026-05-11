package com.entrevista.api_concessionaria.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrevista.api_concessionaria.dto.AnaliseDto;
import com.entrevista.api_concessionaria.dto.DecisaoDto;
import com.entrevista.api_concessionaria.dto.IndicadoresProjection;
import com.entrevista.api_concessionaria.dto.SolicitacaoDto;
import com.entrevista.api_concessionaria.dto.SolicitacaoRespostaDto;
import com.entrevista.api_concessionaria.service.SolicitacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/")
@Tag(name = "Solicitações", description = "Endpoints para gestão da concessionaria, permite buscar solicitações,"
        + "salvar, registrar uma análise, uma decisão e retorna os indicadores")
public class SolicitacaoController {

    @Autowired
    private SolicitacaoService service;

    @Operation(summary = "Salva uma solicitação", description = "### Campos Obrigatórios:\n" +
                  "* **ID Funcionário**: Deve ser um ID válido já cadastrado.\n" +
                  "* **CPF Consumidor**: Deve ser um Consumidor já cadastrado.\n" +
                  "* **Tipo**: Aceita apenas `RECLAMACAO` ou `REVISAO_META`.")
    @PostMapping("solicitacoes")
    public ResponseEntity<?> salvarUmaSolicitacao(@Valid @RequestBody SolicitacaoDto dto) {
        service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Busca Solicitações",description="Busca todas as solicitações que estão salvas.")
    @GetMapping("solicitacoes")
    public ResponseEntity<?> buscarTodas() {
        List<SolicitacaoRespostaDto> solicitacoes = service.buscar();
        return ResponseEntity.ok(solicitacoes);
    }

    @Operation(summary = "Busca uma Solicitação",description="Busca uma solicitação pelo ID.")
    @GetMapping("solicitacoes/{id}")
    public ResponseEntity<?> buscarPorId(
        @Parameter(description = "ID da solicitação que será buscada", example = "10")
        @PathVariable Long id) {
        SolicitacaoRespostaDto solicitacao = service.buscarPorId(id);
        return ResponseEntity.ok(solicitacao);
    }

    @Operation(summary = "Salva uma análise para uma solicitação escolhida", 
    description = "### Campos Obrigatórios Gerais:\n" +
                  "   * `parecer`: Texto detalhando a decisão da análise.\n" +
                  "   * `idFuncionario`: ID do funcionário que está analisando.\n" +
                  "**REVISAO_META**:\n" +
                  "   * Se a análise for para a solicitação do tipo `REVISAO_META`, o campo `novoValorKwhSolicitado` é **obrigatório**.")
    @PostMapping("solicitacoes/{id}/analise")
    public ResponseEntity<?> registrarAnalise(
        @Parameter(description = "ID da solicitação que será analisada", example = "10")
        @PathVariable Long id,
        @RequestBody @Valid AnaliseDto dto) {
        service.registrarAnalise(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(summary = "Salva uma a análise final para uma solicitação escolhida",
    description = "### Campos Obrigatórios Gerais:\n" +
                  "* `decisao`: Deve ser `APROVADO` ou `REPROVADO`, se a decisão for aprovada um novo meta consumo será criado.\n" +
                  "* `respostaFinal`: Texto explicativo que será enviado ao consumidor.\n" +
                  "* `funcionarioId`: ID do gerente/coordenador responsável pela ação.")
    @PostMapping("solicitacoes/{id}/decisao")
    public ResponseEntity<?> registrarDecisao(@PathVariable Long id,@RequestBody @Valid DecisaoDto dto) {
        service.registrarDecisao(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(
    summary = "Retorna os indicadores da concessionária",
    description = "### Métricas de Operação:\n" +
                  "* `totalSolicitacoes`: Volume total de registros.\n" +
                  "* `totalPendentes`: Solicitações aguardando análise.\n" +
                  "* `totalConcluidas`: Solicitações finalizadas.\n" +
                  "* `totalReclamacoes`: Quantidade de queixas técnicas.\n" +
                  "* `totalRevisoesMetas`: Quantidade de pedidos de revisão de meta.\n" +
                  "* `percentualConcluidas`: Taxa de conversão/finalização (%)"
    )
    @GetMapping("indicadores/solicitacoes")
    public ResponseEntity<IndicadoresProjection> getIndicadores() {
        return ResponseEntity.ok(service.buscarIndicadores());
    }
}
