package com.entrevista.api_concessionaria.Repository;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.entrevista.api_concessionaria.enums.PerfilFuncionario;
import com.entrevista.api_concessionaria.model.Funcionario;
import com.entrevista.api_concessionaria.repository.FuncionarioRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class FuncionarioRepositoryTest {

    @Autowired
    FuncionarioRepository repo;

    @Test
    @Transactional
    public void deveSalvarUmFuncionario() {
        Funcionario funcionario = Funcionario.builder()
                .nome("Carlos")
                .perfil(PerfilFuncionario.ANALISTA)
                .area("Manutenção")
                .build();

        Funcionario funcionarioSalvo = repo.save(funcionario);

        Assertions.assertNotNull(funcionarioSalvo, "O funcionário não nulo");
        Assertions.assertNotNull(funcionarioSalvo.getId(), "Deve ter id após save");
        Assertions.assertEquals("Carlos", funcionarioSalvo.getNome());
    }

    @Test
    @Transactional
    public void deveAtualizarUmFuncionario() {
        Funcionario funcionario = Funcionario.builder()
                .nome("Carlos")
                .perfil(PerfilFuncionario.ANALISTA)
                .area("Manutenção")
                .build();

        Funcionario funcionarioSalvo = repo.save(funcionario);
        Long idParaBusca = funcionarioSalvo.getId();

        funcionarioSalvo.setNome("Carlos Silva");
        funcionarioSalvo.setPerfil(PerfilFuncionario.GERENTE);
        funcionarioSalvo.setArea("Supervisão");
        
        repo.save(funcionarioSalvo);

        Funcionario funcionarioAtualizado = repo.findById(idParaBusca)
                .orElseGet(() -> Assertions.fail("Funcionário não encontrado após a atualização"));

        Assertions.assertEquals("Carlos Silva", funcionarioAtualizado.getNome());
        Assertions.assertEquals(PerfilFuncionario.GERENTE, funcionarioAtualizado.getPerfil());
        Assertions.assertEquals("Supervisão", funcionarioAtualizado.getArea());
        Assertions.assertEquals(idParaBusca, funcionarioAtualizado.getId());
    }

    @Test
    @Transactional
    public void deveDeletarUmFuncionario() {
        Funcionario funcionario = Funcionario.builder()
                .nome("Carlos")
                .perfil(PerfilFuncionario.ANALISTA)
                .area("Manutenção")
                .build();

        Funcionario funcionarioSalvo = repo.save(funcionario);
        Long idParaDeletar = funcionarioSalvo.getId();

        repo.delete(funcionarioSalvo);

        Optional<Funcionario> resultado = repo.findById(idParaDeletar);

        Assertions.assertTrue(resultado.isEmpty(), "Funcionário removido do banco de dados");
    }
}
