package com.entrevista.api_concessionaria.service;

import java.sql.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entrevista.api_concessionaria.dto.AnaliseDto;
import com.entrevista.api_concessionaria.dto.ConsumidorDto;
import com.entrevista.api_concessionaria.dto.RegistroAnaliseDto;
import com.entrevista.api_concessionaria.dto.SolicitacaoDto;
import com.entrevista.api_concessionaria.enums.StatusSolicitacao;
import com.entrevista.api_concessionaria.enums.TipoSolicitacao;
import com.entrevista.api_concessionaria.exception.ServiceRunTimeException;
import com.entrevista.api_concessionaria.model.Analise;
import com.entrevista.api_concessionaria.model.Consumidor;
import com.entrevista.api_concessionaria.model.Funcionario;
import com.entrevista.api_concessionaria.model.Solicitacao;
import com.entrevista.api_concessionaria.repository.AnaliseRepository;
import com.entrevista.api_concessionaria.repository.ConsumidorRepository;
import com.entrevista.api_concessionaria.repository.FuncionarioRepository;
import com.entrevista.api_concessionaria.repository.SolicitacaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolicitacoesService {

    @Autowired
    private SolicitacaoRepository repo;

    @Autowired
    private ConsumidorRepository consumidorRepo;

    @Autowired
    private FuncionarioRepository funcionarioRepo;

    @Autowired
    private AnaliseRepository analiseRepo;

    private static final Logger logger = LoggerFactory.getLogger(SolicitacoesService.class);

    public List<SolicitacaoDto> buscar() {
        List<Solicitacao> solicitacoes = repo.findAll();
        
        return solicitacoes.stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public SolicitacaoDto buscarPorId(Long id) {
        Solicitacao solicitacao = repo.findById(id)
                .orElseThrow(() -> new ServiceRunTimeException("Solicitação não encontrada com o ID: " + id));
        
        return converterParaDto(solicitacao);
    }

    @Transactional
    public SolicitacaoDto salvar(SolicitacaoDto dto) {
        Solicitacao solicitacao = converterParaEntidade(dto);
        
        solicitacao = repo.save(solicitacao);

        return converterParaDto(solicitacao);
    }

    private void verificarId(Solicitacao solicitacao) {
        if ((solicitacao == null) || (solicitacao.getId() == null) ||
                !(repo.existsById(solicitacao.getId()))) {
            throw new ServiceRunTimeException("ID de solicitacao inválido");
        }
    }

    @Transactional
    public void registrarAnalise(Long id, RegistroAnaliseDto dto){
        Solicitacao solicitacao = repo.findById(id)
                .orElseThrow(() -> new ServiceRunTimeException("Solicitação não encontrada"));
        
        Funcionario funcionario = funcionarioRepo.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new ServiceRunTimeException("Funcionário não encontrado"));

        Analise novaAnalise = Analise.builder()
                .parecer(dto.getParecer())
                .dataAnalise(new Date(System.currentTimeMillis()))
                .funcionario(funcionario)
                .solicitacao(solicitacao)
                .novoValorKwhSolicitado(dto.getNovoValorKwhSolicitado())
                .build();

        if (solicitacao.getTipo() == TipoSolicitacao.RECLAMACAO) {
            regrasReclamacao(solicitacao, novaAnalise);
        } 
        else if (solicitacao.getTipo() == TipoSolicitacao.REVISAO_META) {
            regrasRevisaoMeta(solicitacao, novaAnalise);
        }

        analiseRepo.save(novaAnalise);
    }

    private Solicitacao converterParaEntidade(SolicitacaoDto dto) {
        Consumidor consumidor = consumidorRepo.findByCpf(dto.getCpfConsumidor())
            .orElseThrow(() -> new ServiceRunTimeException("Não foi possível encontrar um consumidor com o CPF: " + dto.getCpfConsumidor()));

        return Solicitacao.builder()
                .id(dto.getId())
                .dataAbertura(new Date(System.currentTimeMillis()))
                .tipo(dto.getTipo())
                .status(StatusSolicitacao.ABERTA)
                .respostaFinal(dto.getRespostaFinal())
                .consumidor(consumidor)
                .build();
    }

    private SolicitacaoDto converterParaDto(Solicitacao solicitacao) {
        return SolicitacaoDto.builder()
                .id(solicitacao.getId())
                .dataAbertura(solicitacao.getDataAbertura())
                .tipo(solicitacao.getTipo())
                .status(solicitacao.getStatus())
                .respostaFinal(solicitacao.getRespostaFinal())
                .cpfConsumidor(solicitacao.getConsumidor() != null? solicitacao.getConsumidor().getCpf(): null)
                .consumidor(ConsumidorDto.from(solicitacao.getConsumidor()))
                .funcionarioId(solicitacao.getFuncionario() != null ? solicitacao.getFuncionario().getId() : null)
                .analises(AnaliseDto.from(solicitacao.getAnalises()))
                .build();
    }

    private void regrasReclamacao(Solicitacao solicitacao, Analise novaAnalise) {
        if (solicitacao.getAnalises() == null || solicitacao.getAnalises().isEmpty()) {
            solicitacao.setStatus(StatusSolicitacao.EM_ANALISE);
            solicitacao.setFuncionario(novaAnalise.getFuncionario());
        }
    }

    private void regrasRevisaoMeta(Solicitacao solicitacao, Analise novaAnalise) {

        if (solicitacao.getAnalises() != null && !solicitacao.getAnalises().isEmpty()) {
            throw new ServiceRunTimeException("Já existe uma análise para esta revisão de meta.");
        }
        
        if (novaAnalise.getNovoValorKwhSolicitado() == null) {
            throw new ServiceRunTimeException("Novo valor de kWh é obrigatório para revisão de meta.");
        }

        solicitacao.setStatus(StatusSolicitacao.AGUARDANDO_APROVACAO);
    }
}
