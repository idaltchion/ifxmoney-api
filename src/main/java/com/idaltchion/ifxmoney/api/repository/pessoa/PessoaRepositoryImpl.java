package com.idaltchion.ifxmoney.api.repository.pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.idaltchion.ifxmoney.api.model.Pessoa;
import com.idaltchion.ifxmoney.api.model.Pessoa_;
import com.idaltchion.ifxmoney.api.repository.filter.PessoaFilter;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery {

	// 1. Permite fazer a interacao com as entidades (models)
	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable) {
		// 2. Criando um objeto que prepara uma consulta a uma entidade
		CriteriaBuilder builder = manager.getCriteriaBuilder();

		// 3. Definindo o tipo (query) e o objeto (Pessoa) da interacao desejada
		CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);

		// 4. Definindo de qual entidade (model) o resultado sera obtido
		Root<Pessoa> root = criteria.from(Pessoa.class);

		// 5. Criando as restricoes/criterios desejadas na pesquisa
		Predicate[] predicates = criarRestricoes(pessoaFilter, builder, root);

		// 6. Restringindo a query para atender as restricoes desejadas
		criteria.where(predicates);

		// 7. Criando um objeto que executa a query desejada
		TypedQuery<Pessoa> query = manager.createQuery(criteria);

		// 8. Configura as caracteristicas de paginacao
		adicionarRestricoesDePaginacao(query, pageable);

		// 9. Traz os resultados encontrados e com a paginacao desejada
		return new PageImpl<>(query.getResultList(), pageable, total(pessoaFilter));
	}

	private Long total(PessoaFilter pessoaFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Pessoa> root = criteria.from(Pessoa.class);
		Predicate[] predicates = criarRestricoes(pessoaFilter, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));

		return manager.createQuery(criteria).getSingleResult();
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<Pessoa> query, Pageable pageable) {

		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);

	}

	private Predicate[] criarRestricoes(PessoaFilter pessoaFilter, CriteriaBuilder builder, Root<Pessoa> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(pessoaFilter.getNome())) {
			predicates.add(builder.like(builder.lower(root.get(Pessoa_.NOME)),
					"%" + pessoaFilter.getNome().toLowerCase() + "%"));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
