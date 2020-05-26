package com.idaltchion.ifxmoney.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idaltchion.ifxmoney.api.event.ResourceCreatedEvent;
import com.idaltchion.ifxmoney.api.model.Categoria;
import com.idaltchion.ifxmoney.api.repository.CategoriaRepository;

/*
 * Classe que expoe o recurso '/categorias' no REST
 */

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping //GET
	//permissao do usuario e permissao do escopo (ver tambem classe AuthorizationServerConfig
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Categoria> listar() {
		return categoriaRepository.findAll();
	}
		
	@PostMapping //POST
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	// A annotation @Valid é utilizada para validação de campos (ver tambem a classe Categoria - campo nome)
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		// etapa1: salva a entidade no banco
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		//monta a URI no formato desejado
		publisher.publishEvent(new ResourceCreatedEvent(this, response, categoriaSalva.getCodigo()));
		
		/*
		 * etapa2:
		 * - efetua a criação do builder de acordo com o uri passado e devolve o resultado, ou seja, o body criado.
		 * - monta a resposta para a API.
		 * - codigo http: 201 - CREATED
		 */
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
		
		//etapa1: busca no banco
		Categoria categoria = categoriaRepository.findById(codigo).orElse(null);
		
		/*
		 * etapa2:
		 * - monta a resposta para a API.
		 * 		registro encontrado: retorna o body com codigo http 200
		 *  	registro nao encontrado: retorna o codigo http 404 e sem body
		 */
		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}
}
