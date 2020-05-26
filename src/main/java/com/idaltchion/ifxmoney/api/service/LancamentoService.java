package com.idaltchion.ifxmoney.api.service;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idaltchion.ifxmoney.api.model.Lancamento;
import com.idaltchion.ifxmoney.api.model.Pessoa;
import com.idaltchion.ifxmoney.api.repository.LancamentoRepository;
import com.idaltchion.ifxmoney.api.repository.PessoaRepository;
import com.idaltchion.ifxmoney.api.service.exception.PessoaInexistenteOuInativaException;

/*
 * Classe Services são utilizadas para efetuar regras de negocio da classe repository. Nesse caso Lancamento.
 */
@Service
public class LancamentoService {

	/*
	 * A classe repository pode ser utilizada em qq parte do sistema? Nao viola o
	 * MVC? Verificar.
	 */
	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	public Lancamento salvar(@Valid Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.getOne(lancamento.getPessoa().getCodigo());
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizarLancamento(Long codigo, Lancamento lancamento) {
		// Etapa 1: busca o lancamento no banco
		Lancamento lancamentoSalvo = lancamentoRepository.findById(codigo).get();

		// Etapa 2: Copia as propriedades do lancamento
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

		// Etapa 3: Salva o lancamento com as atualizacoes realizadas
		return lancamentoRepository.save(lancamentoSalvo);
	}

}
