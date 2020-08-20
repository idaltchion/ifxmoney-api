package com.idaltchion.ifxmoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idaltchion.ifxmoney.api.model.Pessoa;
import com.idaltchion.ifxmoney.api.repository.PessoaRepository;

/*
 * Classe responsavel pelas regras de negocio da classe Pessoa
 */

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa atualizarPessoa(Long codigo, Pessoa pessoa) {
		//Etapa 1 - busca no banco
		Pessoa pessoaSalva = pessoaRepository.findById(codigo).get();
		pessoaSalva.getContatos().clear();
		pessoaSalva.getContatos().addAll(pessoa.getContatos());
		pessoaSalva.getContatos().forEach(c -> c.setPessoa(pessoaSalva));
		
		//Etapa 2 - copia as propriedades da pessoa conforme a requisição
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo", "contatos");
				
		//Etapa 3 - salva a pessoa novamente, agora com os atributos atualizados
		return pessoaRepository.save(pessoaSalva);
	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		//Etapa 1 - busca no banco
		Pessoa pessoaSalva = pessoaRepository.findById(codigo).get();
		
		//Etapa 2 - atualiza a propriedade da pessoa
		pessoaSalva.setAtivo(ativo);
		
		//Etapa 3 - salva no banco
		pessoaRepository.save(pessoaSalva);
	}
	
	public Pessoa salvar(Pessoa pessoa) {
		pessoa.getContatos().forEach(c -> c.setPessoa(pessoa));
		return pessoaRepository.save(pessoa);
	}
	
}
