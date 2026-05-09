package com.entrevista.api_concessionaria.service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entrevista.api_concessionaria.dto.DecisaoDto;
import com.entrevista.api_concessionaria.dto.IndicadoresProjection;
import com.entrevista.api_concessionaria.dto.RegistroAnaliseDto;
import com.entrevista.api_concessionaria.dto.SolicitacaoDto;
import com.entrevista.api_concessionaria.enums.DecisaoGerente;
import com.entrevista.api_concessionaria.enums.StatusSolicitacao;
import com.entrevista.api_concessionaria.enums.TipoSolicitacao;
import com.entrevista.api_concessionaria.exception.ServiceRunTimeException;
import com.entrevista.api_concessionaria.model.Analise;
import com.entrevista.api_concessionaria.model.Consumidor;
import com.entrevista.api_concessionaria.model.Funcionario;
import com.entrevista.api_concessionaria.model.MetaConsumo;
import com.entrevista.api_concessionaria.model.Solicitacao;
import com.entrevista.api_concessionaria.repository.AnaliseRepository;
import com.entrevista.api_concessionaria.repository.ConsumidorRepository;
import com.entrevista.api_concessionaria.repository.FuncionarioRepository;
import com.entrevista.api_concessionaria.repository.MetaConsumoRepository;
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
    private FuncionarioService funcionarioServ;

    @Autowired
    private AnaliseRepository analiseRepo;

    @Autowired
    private MetaConsumoRepository metaRepo;

    public List<SolicitacaoDto> buscar() {
        List<Solicitacao> solicitacoes = repo.findAll();
        
        return solicitacoes.stream()
                .map(SolicitacaoDto::from)
                .collect(Collectors.toList());
    }

    public SolicitacaoDto buscarPorId(Long id) {
        Solicitacao solicitacao = repo.findById(id)
                .orElseThrow(() -> new ServiceRunTimeException("Solicitação não encontrada com o ID: " + id));
        
        return SolicitacaoDto.from(solicitacao);
    }

    @Transactional
    public SolicitacaoDto salvar(SolicitacaoDto dto) {
        Solicitacao solicitacao = converterParaEntidade(dto);
        
        solicitacao = repo.save(solicitacao);

        return SolicitacaoDto.from(solicitacao);
    }

    @Transactional
    public void registrarDecisao(Long id, DecisaoDto dto) {
        Funcionario gerente = funcionarioServ.validarSeEhGerenteERetorna(dto.funcionarioId());

        Solicitacao solicitacao = repo.findById(id)
                .orElseThrow(() -> new ServiceRunTimeException("Solicitação não encontrada"));

        solicitacao.setFuncionario(gerente);

        concluirDecisao(solicitacao, dto);

        if (solicitacao.getTipo() == TipoSolicitacao.REVISAO_META && dto.decisao() == DecisaoGerente.APROVADA) {
            criarNovaMeta(solicitacao, dto);
        }
    }

    private void concluirDecisao(Solicitacao solicitacao, DecisaoDto dto) {
        if (solicitacao.getAnalises() == null || solicitacao.getAnalises().isEmpty()) {
            throw new ServiceRunTimeException("Não é possível concluir uma solicitação sem análises.");
        }
        solicitacao.setDataConclusao(new Date(System.currentTimeMillis()));
        solicitacao.setStatus(StatusSolicitacao.CONCLUIDA);
        solicitacao.setRespostaFinal(dto.respostaFinal());
    }

    private void criarNovaMeta(Solicitacao solicitacao, DecisaoDto dto) {
        Analise analise = solicitacao.getAnalises().get(0);

        MetaConsumo novaMeta = MetaConsumo.builder()
                .consumidor(solicitacao.getConsumidor())
                .mes(solicitacao.getDataAbertura().toLocalDate().getMonthValue())
                .ano(solicitacao.getDataAbertura().toLocalDate().getYear())
                .valorKwh(analise.getNovoValorKwhSolicitado())
                .build();
        metaRepo.save(novaMeta);
    }

    @Transactional
    public void registrarAnalise(Long id, RegistroAnaliseDto dto){
        Solicitacao solicitacao = repo.findById(id)
                .orElseThrow(() -> new ServiceRunTimeException("Solicitação não encontrada"));
        
        Funcionario funcionario = funcionarioRepo.findById(dto.funcionarioId())
                .orElseThrow(() -> new ServiceRunTimeException("Funcionário não encontrado"));

        Analise novaAnalise = Analise.builder()
                .parecer(dto.parecer())
                .dataAnalise(new Date(System.currentTimeMillis()))
                .funcionario(funcionario)
                .solicitacao(solicitacao)
                .novoValorKwhSolicitado(dto.novoValorKwhSolicitado())
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
        Consumidor consumidor = consumidorRepo.findByCpf(dto.cpfConsumidor())
            .orElseThrow(() -> new ServiceRunTimeException("Não foi possível encontrar um consumidor com o CPF: " 
            + dto.cpfConsumidor()));

        return Solicitacao.builder()
                .id(dto.id())
                .dataAbertura(new Date(System.currentTimeMillis()))
                .tipo(dto.tipo())
                .status(StatusSolicitacao.ABERTA)
                .consumidor(consumidor)
                .build();
    }

    private void regrasReclamacao(Solicitacao solicitacao, Analise novaAnalise) {
        if (solicitacao.getStatus() != null && solicitacao.getStatus() == StatusSolicitacao.CONCLUIDA) {
            throw new ServiceRunTimeException("Essa solicitação já foi concluída.");
        }

        if (solicitacao.getAnalises() == null || solicitacao.getAnalises().isEmpty()) {
            solicitacao.setStatus(StatusSolicitacao.EM_ANALISE);
            solicitacao.setFuncionario(novaAnalise.getFuncionario());
        }
    }

    private void regrasRevisaoMeta(Solicitacao solicitacao, Analise novaAnalise) {

        if (solicitacao.getStatus() != null && solicitacao.getStatus() == StatusSolicitacao.CONCLUIDA) {
            throw new ServiceRunTimeException("Essa solicitação já foi concluída.");
        }

        if (solicitacao.getAnalises() != null && !solicitacao.getAnalises().isEmpty()) {
            throw new ServiceRunTimeException("Já existe uma análise para esta revisão de meta.");
        }
        
        if (novaAnalise.getNovoValorKwhSolicitado() == null) {
            throw new ServiceRunTimeException("Novo valor de kWh é obrigatório para revisão de meta.");
        }

        solicitacao.setFuncionario(novaAnalise.getFuncionario());
        solicitacao.setStatus(StatusSolicitacao.AGUARDANDO_APROVACAO);
    }

    public IndicadoresProjection buscarIndicadores() {
        return repo.buscarIndicadores();
    }

}
