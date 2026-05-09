package com.entrevista.api_concessionaria.Repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.entrevista.api_concessionaria.model.Consumidor;
import com.entrevista.api_concessionaria.model.MetaConsumo;
import com.entrevista.api_concessionaria.repository.ConsumidorRepository;
import com.entrevista.api_concessionaria.repository.MetaConsumoRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class MetaConsumoRepositoryTest {

    @Autowired
    MetaConsumoRepository repo;

    @Autowired
    ConsumidorRepository repoConsumidor;

    @Test
    @Transactional
    public void deveSalvarUmaMetaDeConsumo() {
        Consumidor consumidor = repoConsumidor.save(Consumidor.builder()
                .nome("Maria")
                .cpf("11122233344")
                .email("maria@gmail.com")
                .endereco("Rua")
                .build());

        MetaConsumo meta = MetaConsumo.builder()
                .consumidor(consumidor)
                .mes(5)
                .ano(2026)
                .valorKwh(new BigDecimal("150.75"))
                .build();

        MetaConsumo metaSalva = repo.save(meta);

        Assertions.assertNotNull(metaSalva, "A meta salva não pode ser nula");
        Assertions.assertNotNull(metaSalva.getId(), "Deve ter id após save");
        Assertions.assertEquals(5, metaSalva.getMes());
        Assertions.assertEquals(consumidor.getId(), metaSalva.getConsumidor().getId());
    }

    @Test
    @Transactional
    public void deveAtualizarUmaMetaDeConsumo() {
        Consumidor consumidor = repoConsumidor.save(Consumidor.builder()
                .nome("Maria")
                .cpf("11122233344")
                .email("maria@gmail.com")
                .endereco("Rua")
                .build());

        MetaConsumo meta = repo.save(MetaConsumo.builder()
                .consumidor(consumidor)
                .mes(5)
                .ano(2026)
                .valorKwh(new BigDecimal("150.75"))
                .build());

        Long idParaBusca = meta.getId();

        meta.setValorKwh(new BigDecimal("180.00"));
        repo.save(meta);

        MetaConsumo metaAtualizada = repo.findById(idParaBusca)
                .orElseGet(() -> Assertions.fail("Meta de consumo não encontrada após a atualização"));

        Assertions.assertEquals(new BigDecimal("180.00"), metaAtualizada.getValorKwh());
        Assertions.assertEquals(idParaBusca, metaAtualizada.getId());
    }

    @Test
    @Transactional
    public void deveDeletarUmaMetaDeConsumo() {
        Consumidor consumidor = repoConsumidor.save(Consumidor.builder()
                .nome("Maria")
                .cpf("11122233344")
                .email("maria@gmail.com")
                .endereco("Rua")
                .build());
        
        MetaConsumo meta = repo.save(MetaConsumo.builder()
                .consumidor(consumidor)
                .mes(5)
                .ano(2026)
                .valorKwh(new BigDecimal("150.75"))
                .build());

        Long idParaDeletar = meta.getId();
        repo.delete(meta);
        Optional<MetaConsumo> resultado = repo.findById(idParaDeletar);
        Assertions.assertTrue(resultado.isEmpty(), "A meta de consumo deveria ter sido removida");
    }
}
