package com.idaltchion.ifxmoney.api.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.idaltchion.ifxmoney.api.dto.LancamentoEstatisticaPorCategoria;
import com.idaltchion.ifxmoney.api.dto.LancamentoEstatisticaPorDia;
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
	public List<LancamentoEstatisticaPorCategoria> porCategoria(LocalDate mesReferencia);
	public List<LancamentoEstatisticaPorDia> porDia(LocalDate mesReferencia);
	
}
