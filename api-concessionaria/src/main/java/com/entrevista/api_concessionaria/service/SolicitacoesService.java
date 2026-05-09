package com.entrevista.api_concessionaria.service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entrevista.api_concessionaria.dto.AnaliseDto;
import com.entrevista.api_concessionaria.dto.ConsumidorDto;
import com.entrevista.api_concessionaria.dto.SolicitacaoDto;
import com.entrevista.api_concessionaria.enums.StatusSolicitacao;
import com.entrevista.api_concessionaria.exception.ServiceRunTimeException;
import com.entrevista.api_concessionaria.model.Analise;
import com.entrevista.api_concessionaria.model.Consumidor;
import com.entrevista.api_concessionaria.model.Solicitacao;
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

    private Analise registrarAnalise(Solicitacao solicitacao, AnaliseDto analiseDto) {

        

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
}
