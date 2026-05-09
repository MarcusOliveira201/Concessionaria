package com.entrevista.api_concessionaria.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.entrevista.api_concessionaria.enums.StatusSolicitacao;
import com.entrevista.api_concessionaria.enums.TipoSolicitacao;
import com.entrevista.api_concessionaria.model.Analise;
import com.entrevista.api_concessionaria.model.Consumidor;
import com.entrevista.api_concessionaria.model.Funcionario;
import com.entrevista.api_concessionaria.model.Solicitacao;
import com.entrevista.api_concessionaria.repository.AnaliseRepository;
import com.entrevista.api_concessionaria.repository.ConsumidorRepository;
import com.entrevista.api_concessionaria.repository.FuncionarioRepository;
import com.entrevista.api_concessionaria.repository.SolicitacaoRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class AnaliseRepositoryTest {
    @Autowired
    AnaliseRepository repo;

    @Autowired
    SolicitacaoRepository repoSolicitacao;

    @Autowired
    FuncionarioRepository repoFuncionario;

    @Autowired
    ConsumidorRepository repoConsumidor;

    @Test
    @Transactional
    public void deveSalvarUmaAnalise() {
        Funcionario funcionario = repoFuncionario.save(Funcionario.builder()
                .nome("João")
                .perfil("Técnico")
                .area("Manutenção")
                .build());

        Consumidor consumidor = repoConsumidor.save(Consumidor.builder()
                .nome("Maria")
                .cpf("11122233344")
                .email("maria@gmail.com")
                .endereco("Rua")
                .build());

        Solicitacao solicitacao = repoSolicitacao.save(Solicitacao.builder()
                .dataAbertura(new Date(System.currentTimeMillis()))
                .tipo(TipoSolicitacao.RECLAMACAO)
                .status(StatusSolicitacao.ABERTA)
                .funcionario(funcionario)
                .consumidor(consumidor)
                .build());

        Analise analise = Analise.builder()
                .solicitacao(solicitacao)
                .funcionario(funcionario)
                .dataAnalise(new Date(System.currentTimeMillis()))
                .parecer("Parecer favorável ao consumidor")
                .novoValorKwhSolicitado(new BigDecimal("120.50"))
                .build();

        Analise analiseSalva = repo.save(analise);

        Assertions.assertNotNull(analiseSalva, "A análise salva não pode ser nula");
        Assertions.assertNotNull(analiseSalva.getId(), "O id é para ter sido salvo");
        Assertions.assertEquals("Parecer favorável ao consumidor", analiseSalva.getParecer());
    }
    
    @Test
    @Transactional 
    public void deveAtualizarUmaAnalise() {
        Funcionario funcionario = repoFuncionario.save(Funcionario.builder()
                .nome("João")
                .perfil("Técnico")
                .area("Manutenção")
                .build());

        Consumidor consumidor = repoConsumidor.save(Consumidor.builder()
                .nome("Maria")
                .cpf("11122233344")
                .email("maria@gmail.com")
                .endereco("Rua")
                .build());

        Solicitacao solicitacao = repoSolicitacao.save(Solicitacao.builder()
                .funcionario(funcionario)
                .tipo(TipoSolicitacao.RECLAMACAO)
                .status(StatusSolicitacao.ABERTA)
                .dataAbertura(new Date(System.currentTimeMillis()))
                .consumidor(consumidor)
                .build());

        Analise analise = Analise.builder()
                .solicitacao(solicitacao)
                .funcionario(funcionario)
                .dataAnalise(new Date(System.currentTimeMillis()))
                .parecer("Parecer favorável ao consumidor")
                .novoValorKwhSolicitado(new BigDecimal("120.50"))
                .build();

        Analise analiseSalva = repo.save(analise);
        Long idParaBusca = analiseSalva.getId();

        analiseSalva.setParecer("Parecer desfavorável ao consumidor");
        analiseSalva.setNovoValorKwhSolicitado(new BigDecimal("125.50"));
        
        repo.save(analiseSalva);

        Analise analiseAtualizada = repo.findById(idParaBusca)
                .orElseThrow(() -> new RuntimeException("Análise não encontrada"));

        Assertions.assertEquals("Parecer desfavorável ao consumidor", analiseAtualizada.getParecer());
        Assertions.assertEquals(new BigDecimal("125.50"), analiseAtualizada.getNovoValorKwhSolicitado());
        Assertions.assertEquals(idParaBusca, analiseAtualizada.getId());
    }
    
    @Test
    @Transactional
    public void deveDeletarUmaAnalise() {
        Funcionario funcionario = repoFuncionario.save(Funcionario.builder()
                .nome("João")
                .perfil("Técnico")
                .area("Manutenção")
                .build());

        Consumidor consumidor = repoConsumidor.save(Consumidor.builder()
                .nome("Maria")
                .cpf("11122233344")
                .email("maria@gmail.com")
                .endereco("Rua")
                .build());

        Solicitacao solicitacao = repoSolicitacao.save(Solicitacao.builder()
                .dataAbertura(new Date(System.currentTimeMillis()))
                .tipo(TipoSolicitacao.RECLAMACAO)
                .status(StatusSolicitacao.ABERTA)
                .funcionario(funcionario)
                .consumidor(consumidor)
                .build());

        Analise analise = Analise.builder()
                .solicitacao(solicitacao)
                .funcionario(funcionario)
                .dataAnalise(new Date(System.currentTimeMillis()))
                .parecer("Analise para deleção")
                .novoValorKwhSolicitado(new BigDecimal("100.00"))
                .build();

        Analise analiseSalva = repo.save(analise);
        Long idParaDeletar = analiseSalva.getId();

        repo.delete(analiseSalva);

        Optional<Analise> resultado = repo.findById(idParaDeletar);

        Assertions.assertTrue(resultado.isEmpty(), "A análise deveria ter sido removida do banco de dados");
    }
    
}
