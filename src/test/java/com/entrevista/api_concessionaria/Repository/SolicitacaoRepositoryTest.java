package com.entrevista.api_concessionaria.Repository;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.entrevista.api_concessionaria.enums.PerfilFuncionario;
import com.entrevista.api_concessionaria.enums.StatusSolicitacao;
import com.entrevista.api_concessionaria.enums.TipoSolicitacao;
import com.entrevista.api_concessionaria.model.Consumidor;
import com.entrevista.api_concessionaria.model.Funcionario;
import com.entrevista.api_concessionaria.model.Solicitacao;
import com.entrevista.api_concessionaria.repository.ConsumidorRepository;
import com.entrevista.api_concessionaria.repository.FuncionarioRepository;
import com.entrevista.api_concessionaria.repository.SolicitacaoRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class SolicitacaoRepositoryTest {

    @Autowired
    SolicitacaoRepository repo;

    @Autowired
    ConsumidorRepository repoConsumidor;

    @Autowired
    FuncionarioRepository repoFuncionario;

    @Test
    @Transactional
    public void deveSalvarUmaSolicitacao() {
        Consumidor consumidor = repoConsumidor.save(Consumidor.builder()
                .nome("Maria")
                .cpf("11122233334")
                .email("maria@gmail.com")
                .endereco("Rua")
                .build());

        Funcionario funcionario = repoFuncionario.save(Funcionario.builder()
                .nome("João")
                .perfil(PerfilFuncionario.ANALISTA)
                .area("Manutenção")
                .build());

        Solicitacao solicitacao = Solicitacao.builder()
                .dataAbertura(new Date(System.currentTimeMillis()))
                .tipo(TipoSolicitacao.RECLAMACAO)
                .status(StatusSolicitacao.ABERTA)
                .consumidor(consumidor)
                .funcionario(funcionario)
                .build();

        Solicitacao solicitacaoSalva = repo.save(solicitacao);

        Assertions.assertNotNull(solicitacaoSalva, "A solicitação salva não pode ser nula");
        Assertions.assertNotNull(solicitacaoSalva.getId(), "Deve ter id após save");
        Assertions.assertEquals(StatusSolicitacao.ABERTA, solicitacaoSalva.getStatus());
    }

    @Test
    @Transactional
    public void deveAtualizarUmaSolicitacao() {
        Consumidor consumidor = repoConsumidor.save(Consumidor.builder()
                .nome("Maria")
                .cpf("11122233334")
                .email("maria@gmail.com")
                .endereco("Rua")
                .build());

        Funcionario funcionario = repoFuncionario.save(Funcionario.builder()
                .nome("João")
                .perfil(PerfilFuncionario.ANALISTA)
                .area("Manutenção")
                .build());

        Solicitacao solicitacao = repo.save(Solicitacao.builder()
                .dataAbertura(new Date(System.currentTimeMillis()))
                .tipo(TipoSolicitacao.RECLAMACAO)
                .status(StatusSolicitacao.ABERTA)
                .consumidor(consumidor)
                .funcionario(funcionario)
                .build());

        Long idParaBusca = solicitacao.getId();

        solicitacao.setStatus(StatusSolicitacao.EM_ANALISE);
        solicitacao.setRespostaFinal("Análise iniciada pelo setor técnico");
        
        repo.save(solicitacao);

        Solicitacao solicitacaoAtualizada = repo.findById(idParaBusca)
                .orElseGet(() -> Assertions.fail("Solicitação não encontrada após a atualização"));

        Assertions.assertEquals(StatusSolicitacao.EM_ANALISE, solicitacaoAtualizada.getStatus());
        Assertions.assertEquals("Análise iniciada pelo setor técnico", solicitacaoAtualizada.getRespostaFinal());
        Assertions.assertEquals(idParaBusca, solicitacaoAtualizada.getId());
    }

    @Test
    @Transactional
    public void deveDeletarUmaSolicitacao() {
        Consumidor consumidor = repoConsumidor.save(Consumidor.builder()
                .nome("Maria")
                .cpf("11122233334")
                .email("maria@gmail.com")
                .endereco("Rua")
                .build());
        Funcionario funcionario = repoFuncionario.save(Funcionario.builder()
                .nome("João")
                .perfil(PerfilFuncionario.ANALISTA)
                .area("Manutenção")
                .build());

        Solicitacao solicitacao = repo.save(Solicitacao.builder()
                .dataAbertura(new Date(System.currentTimeMillis()))
                .tipo(TipoSolicitacao.RECLAMACAO)
                .status(StatusSolicitacao.ABERTA)
                .consumidor(consumidor)
                .funcionario(funcionario)
                .build());

        Long idParaDeletar = solicitacao.getId();
        repo.delete(solicitacao);

        Optional<Solicitacao> resultado = repo.findById(idParaDeletar);
        Assertions.assertTrue(resultado.isEmpty(), "Solicitação removida do banco de dados");
    }
}
