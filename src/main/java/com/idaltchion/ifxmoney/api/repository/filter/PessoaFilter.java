package com.idaltchion.ifxmoney.api.repository.filter;

/*
 * Classe responsavel pelos filtros (parametros) que podem ser passados na API
 * Nessa classe devera conter os campos pelo qual podem ser filtrada/pesquisada e que estarao disponiveis na API 
 */
public class PessoaFilter {

	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
