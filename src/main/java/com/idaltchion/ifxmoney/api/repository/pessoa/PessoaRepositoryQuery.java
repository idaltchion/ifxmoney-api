package com.idaltchion.ifxmoney.api.repository.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.idaltchion.ifxmoney.api.model.Pessoa;
import com.idaltchion.ifxmoney.api.repository.filter.PessoaFilter;

public interface PessoaRepositoryQuery {

	public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable);
	
}
