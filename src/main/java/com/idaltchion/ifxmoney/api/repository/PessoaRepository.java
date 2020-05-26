package com.idaltchion.ifxmoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idaltchion.ifxmoney.api.model.Pessoa;
import com.idaltchion.ifxmoney.api.repository.pessoa.PessoaRepositoryQuery;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery {

}
