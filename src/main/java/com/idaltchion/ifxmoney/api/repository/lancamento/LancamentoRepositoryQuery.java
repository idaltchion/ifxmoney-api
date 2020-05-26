package com.idaltchion.ifxmoney.api.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.idaltchion.ifxmoney.api.model.Lancamento;
import com.idaltchion.ifxmoney.api.repository.filter.LancamentoFilter;
import com.idaltchion.ifxmoney.api.resource.projection.ResumoLancamento;

/*
 * - Interface criada com o proposito de criar metodos especifico para o Lancamento, ja que no Repository "nao e possivel"
 * - As implementacoes dos metodos abaixo serao realizadas na classe LancamentoRepositoryImpl
 */
public interface LancamentoRepositoryQuery {

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
	
}
