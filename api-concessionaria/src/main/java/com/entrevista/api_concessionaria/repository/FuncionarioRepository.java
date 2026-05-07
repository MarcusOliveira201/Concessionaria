package com.entrevista.api_concessionaria.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.entrevista.api_concessionaria.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{
}