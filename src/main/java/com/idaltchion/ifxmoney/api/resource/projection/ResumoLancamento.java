package com.idaltchion.ifxmoney.api.resource.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.idaltchion.ifxmoney.api.model.TipoLancamento;

/*
 * Projections: funcao que retorna apenas alguns campos de uma determinada classe, util para performance
 * Projections do Lancamento
 */
public class ResumoLancamento {

	private Long codigo;
	private String descricao;
	private LocalDate dataPagamento;
	private LocalDate dataVencimento;
	private BigDecimal valor;
	private TipoLancamento tipoLancamento;
	private String categoria;
	private String pessoa;

	/*
	 * No criteriaJpa sera utilizado o construtor abaixo para retirnar a projections
	 */
	public ResumoLancamento(Long codigo, String descricao, LocalDate dataPagamento, LocalDate dataVencimento,
			BigDecimal valor, TipoLancamento tipoLancamento, String categoria, String pessoa) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.dataPagamento = dataPagamento;
		this.dataVencimento = dataVencimento;
		this.valor = valor;
		this.tipoLancamento = tipoLancamento;
		this.categoria = categoria;
		this.pessoa = pessoa;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getPessoa() {
		return pessoa;
	}

	public void setPessoa(String pessoa) {
		this.pessoa = pessoa;
	}

}
