package com.entrevista.api_concessionaria.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Optional;
import java.util.List;
import org.mockito.ArgumentCaptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.entrevista.api_concessionaria.dto.SolicitacaoDto;
import com.entrevista.api_concessionaria.dto.SolicitacaoRespostaDto;
import com.entrevista.api_concessionaria.enums.StatusSolicitacao;
import com.entrevista.api_concessionaria.enums.TipoSolicitacao;
import com.entrevista.api_concessionaria.exception.ServiceRunTimeException;
import com.entrevista.api_concessionaria.model.Analise;
import com.entrevista.api_concessionaria.model.Consumidor;
import com.entrevista.api_concessionaria.model.Solicitacao;
import com.entrevista.api_concessionaria.repository.ConsumidorRepository;
import com.entrevista.api_concessionaria.repository.SolicitacaoRepository;

@ExtendWith(MockitoExtension.class)
public class SolicitacaoServiceTest {

    @Mock
    private SolicitacaoRepository repo;

    @Mock
    private ConsumidorRepository consumidorRepo;

    @InjectMocks
    private SolicitacaoService service;

    @Test
    @DisplayName("Deve retornar os dados, consumidor, análises e resposta final ao buscar todos")
    public void buscarTodosComSucesso() {
        Solicitacao solicitacao1 = Solicitacao.builder()
                .id(1L)
                .status(StatusSolicitacao.ABERTA)
                .build();
        Solicitacao solicitacao2 = Solicitacao.builder()
                .id(2L)
                .status(StatusSolicitacao.CONCLUIDA)
                .build();
                
        when(repo.findAll()).thenReturn(List.of(solicitacao1, solicitacao2));

        List<SolicitacaoRespostaDto> resultado = service.buscar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).id());
        assertEquals(2L, resultado.get(1).id());
        verify(repo, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar na busca os dados, consumidor, análises e resposta final quando o ID existir")
    public void buscarPorIdComSucesso() {
        Long id = 1L;
        Consumidor consumidor = Consumidor.builder()
                .id(10L)
                .nome("Maria da Silva")
                .build();
        Analise analise = Analise.builder()
                .id(100L)
                .parecer("Me parece certo")
                .build();
        Solicitacao solicitacao = Solicitacao.builder()
                .id(id)
                .status(StatusSolicitacao.CONCLUIDA)
                .consumidor(consumidor)
                .analises(List.of(analise))
                .respostaFinal("Deferida")
                .build();
        when(repo.findById(id)).thenReturn(Optional.of(solicitacao));
        SolicitacaoRespostaDto resultado = service.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.id());
        assertEquals("Solicitação deferida", resultado.respostaFinal());
        assertNotNull(resultado.consumidor());
        assertNotNull(resultado.analises());
        assertEquals(1, resultado.analises().size());

        verify(repo, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve dar erro quando o ID não existir")
    void buscarPorIdErro() {
        Long idInexistente = 2L;
        when(repo.findById(idInexistente)).thenReturn(Optional.empty());
        assertThrows(ServiceRunTimeException.class, () -> {service.buscarPorId(idInexistente);});
    }

    @Test
    @DisplayName("A solicitação deve iniciar com status ABERTA")
    void salvarComSucesso() {
        String cpf = "12345678901";
        SolicitacaoDto dto = SolicitacaoDto.builder()
                .cpfConsumidor(cpf)
                .tipo(TipoSolicitacao.REVISAO_META)
                .build();

        Consumidor consumidor = new Consumidor(); 
        when(consumidorRepo.findByCpf(cpf)).thenReturn(Optional.of(consumidor));

        Solicitacao solicitacaoSalva = Solicitacao.builder()
                                        .id(1L)
                                        .status(StatusSolicitacao.ABERTA)
                                        .tipo(TipoSolicitacao.REVISAO_META)
                                        .build();

        when(repo.save(any(Solicitacao.class))).thenReturn(solicitacaoSalva);

        service.salvar(dto);

        ArgumentCaptor<Solicitacao> captor = ArgumentCaptor.forClass(Solicitacao.class);
        verify(repo, times(1)).save(captor.capture());
        
        Solicitacao solicitacaoCapturada = captor.getValue();
        assertNotNull(solicitacaoCapturada);
        assertEquals(TipoSolicitacao.REVISAO_META, solicitacaoCapturada.getTipo());
        assertEquals(StatusSolicitacao.ABERTA, solicitacaoCapturada.getStatus(), "A solicitação deve iniciar com o status ABERTA");
        
        verify(consumidorRepo).findByCpf(cpf);
    }

    @Test
    @DisplayName("Deve dar um erro quando não achar o Consumidor")
    void salvarComErroSeNaoEncontrarOConsumidor() {
        String cpf = "0";
        SolicitacaoDto dto = SolicitacaoDto.builder()
                .cpfConsumidor(cpf)
                .tipo(TipoSolicitacao.REVISAO_META)
                .build();

        Consumidor consumidor = new Consumidor(); 
        when(consumidorRepo.findByCpf(cpf)).thenReturn(Optional.empty());

        ServiceRunTimeException exception = assertThrows(ServiceRunTimeException.class, () -> {
            service.salvar(dto);});

        assertTrue(exception.getMessage().contains("Não foi possível encontrar um consumidor com o CPF: " 
            + dto.cpfConsumidor()));

        verify(repo, never()).save(any(Solicitacao.class));
    }


}
