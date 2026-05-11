package com.entrevista.api_concessionaria.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entrevista.api_concessionaria.dto.AnaliseDto;
import com.entrevista.api_concessionaria.dto.DecisaoDto;
import com.entrevista.api_concessionaria.dto.IndicadoresProjection;
import com.entrevista.api_concessionaria.dto.SolicitacaoDto;
import com.entrevista.api_concessionaria.dto.SolicitacaoRespostaDto;
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


@Service
public class SolicitacaoService {

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

    @Autowired
    private MetaConsumoService metaConsumoService;

    public List<SolicitacaoRespostaDto> buscar() {
        List<Solicitacao> solicitacoes = repo.findAll();
        
        return solicitacoes.stream()
                .map(SolicitacaoRespostaDto::from)
                .collect(Collectors.toList());
    }

    public SolicitacaoRespostaDto buscarPorId(Long id) {
        Solicitacao solicitacao = repo.findById(id)
                .orElseThrow(() -> new ServiceRunTimeException("Solicitação não encontrada com o ID: " + id));
        
        return SolicitacaoRespostaDto.from(solicitacao);
    }

    @Transactional
    public void salvar(SolicitacaoDto dto) {
        Solicitacao solicitacao = converterParaEntidade(dto);
        
        solicitacao = repo.save(solicitacao);
    }

    @Transactional
    public void registrarDecisao(Long id, DecisaoDto dto) {
        Funcionario gerente = funcionarioServ.validarSeEhGerenteERetorna(dto.funcionarioId());

        Solicitacao solicitacao = repo.findById(id)
                .orElseThrow(() -> new ServiceRunTimeException("Solicitação não encontrada"));

        solicitacao.garantirQuePodeSerAlterada();

        solicitacao.setFuncionario(gerente);

        concluirDecisao(solicitacao, dto);

        if (solicitacao.getTipo() == TipoSolicitacao.REVISAO_META && dto.decisao() == DecisaoGerente.APROVADA) {
            criarNovaMeta(solicitacao, dto);
        }

        repo.save(solicitacao);
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

        LocalDate dataRef = solicitacao.getDataAbertura().toLocalDate();
        Integer mes = dataRef.getMonthValue();
        Integer ano = dataRef.getYear();

        Analise analise = solicitacao.getAnalises().get(0);

        MetaConsumo meta = metaConsumoService
        .buscarMeta(solicitacao.getConsumidor().getId(), mes, ano)
        .orElse(new MetaConsumo());

        meta.setConsumidor(solicitacao.getConsumidor());
        meta.setMes(mes);
        meta.setAno(ano);
        meta.setValorKwh(analise.getNovoValorKwhSolicitado());
        
        metaRepo.save(meta);
    }

    @Transactional
    public void registrarAnalise(Long id, AnaliseDto dto){
        
        Solicitacao solicitacao = repo.findById(id)
                .orElseThrow(() -> new ServiceRunTimeException("Solicitação não encontrada"));

        solicitacao.garantirQuePodeSerAlterada();
        
        Funcionario funcionario = funcionarioRepo.findById(dto.funcionarioId())
                .orElseThrow(() -> new ServiceRunTimeException("Funcionário não encontrado"));

        Analise novaAnalise = Analise.builder()
                .parecer(dto.parecer())
                .dataAnalise(new Date(System.currentTimeMillis()))
                .funcionario(funcionario)
                .solicitacao(solicitacao)
                // .novoValorKwhSolicitado(dto.novoValorKwhSolicitado())
                .build();

        if (solicitacao.getTipo() == TipoSolicitacao.RECLAMACAO) {
            regrasReclamacao(solicitacao, novaAnalise);
        } 
        else if (solicitacao.getTipo() == TipoSolicitacao.REVISAO_META) {
            novaAnalise.setNovoValorKwhSolicitado(dto.novoValorKwhSolicitado());
            regrasRevisaoMeta(solicitacao, novaAnalise);
        }

        analiseRepo.save(novaAnalise);
    }

    private Solicitacao converterParaEntidade(SolicitacaoDto dto) {
        Consumidor consumidor = consumidorRepo.findByCpf(dto.cpfConsumidor())
            .orElseThrow(() -> new ServiceRunTimeException("Não foi possível encontrar um consumidor com o CPF:" 
            + dto.cpfConsumidor()));

        return Solicitacao.builder()
                .dataAbertura(new Date(System.currentTimeMillis()))
                .tipo(dto.tipo())
                .status(StatusSolicitacao.ABERTA)
                .consumidor(consumidor)
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

        solicitacao.setFuncionario(novaAnalise.getFuncionario());
        solicitacao.setStatus(StatusSolicitacao.AGUARDANDO_APROVACAO);
    }

    public IndicadoresProjection buscarIndicadores() {
        return repo.buscarIndicadores();
    }

}
