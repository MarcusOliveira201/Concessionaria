package com.entrevista.api_concessionaria.Repository;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.entrevista.api_concessionaria.model.Consumidor;
import com.entrevista.api_concessionaria.repository.ConsumidorRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class ConsumidorRepositoryTest {

    @Autowired
    ConsumidorRepository repo;

    @Test
    @Transactional
    public void deveSalvarUmConsumidor() {
        Consumidor consumidor = Consumidor.builder()
                .nome("Maria")
                .cpf("11122233344")
                .email("maria@gmail.com")
                .endereco("Rua")
                .build();

        Consumidor consumidorSalvo = repo.save(consumidor);

        Assertions.assertNotNull(consumidorSalvo, "O consumidor salvo pode ser nulo");
        Assertions.assertNotNull(consumidorSalvo.getId(), "Deve ter id após save");
        Assertions.assertEquals("Maria", consumidorSalvo.getNome());
    }

    @Test
    @Transactional
    public void deveAtualizarUmConsumidor() {
        Consumidor consumidor = Consumidor.builder()
                .nome("Maria")
                .cpf("11122233344")
                .email("maria@gmail.com")
                .endereco("Rua")
                .build();

        Consumidor consumidorSalvo = repo.save(consumidor);
        Long idParaBusca = consumidorSalvo.getId();

        consumidorSalvo.setNome("Luzia");
        consumidorSalvo.setEmail("maria.silva@email.com");
        
        repo.save(consumidorSalvo);

        Consumidor consumidorAtualizado = repo.findById(idParaBusca)
                .orElseThrow(() -> new RuntimeException("Consumidor não encontrado"));

        Assertions.assertEquals("Luzia", consumidorAtualizado.getNome());
        Assertions.assertEquals("maria.silva@email.com", consumidorAtualizado.getEmail());
        Assertions.assertEquals(idParaBusca, consumidorAtualizado.getId());
    }

    @Test
    @Transactional
    public void deveDeletarUmConsumidor() {
        Consumidor consumidor = Consumidor.builder()
                .nome("Maria")
                .cpf("11122233344")
                .email("maria@gmail.com")
                .endereco("Rua")
                .build();

        Consumidor consumidorSalvo = repo.save(consumidor);
        Long idParaDeletar = consumidorSalvo.getId();

        repo.delete(consumidorSalvo);

        Optional<Consumidor> resultado = repo.findById(idParaDeletar);

        Assertions.assertTrue(resultado.isEmpty(), "Consumidor removido do banco de dados");
    }
}
