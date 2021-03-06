package com.idaltchion.ifxmoney.api.resource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.idaltchion.ifxmoney.api.dto.Anexo;
import com.idaltchion.ifxmoney.api.dto.LancamentoEstatisticaPorCategoria;
import com.idaltchion.ifxmoney.api.dto.LancamentoEstatisticaPorDia;
import com.idaltchion.ifxmoney.api.event.ResourceCreatedEvent;
import com.idaltchion.ifxmoney.api.exceptionhandler.IfxmoneyExceptionHandler.Erro;
import com.idaltchion.ifxmoney.api.model.Lancamento;
import com.idaltchion.ifxmoney.api.repository.LancamentoRepository;
import com.idaltchion.ifxmoney.api.repository.filter.LancamentoFilter;
import com.idaltchion.ifxmoney.api.resource.projection.ResumoLancamento;
import com.idaltchion.ifxmoney.api.service.LancamentoService;
import com.idaltchion.ifxmoney.api.service.exception.PessoaInexistenteOuInativaException;
import com.idaltchion.ifxmoney.api.storage.S3;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private S3 s3;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		//aqui tem o problema do N + 1 do JPA. Verificar melhor solução para esse caso
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}
	
	/*
	 * - O params possibilita chamar a Projections criada
	 * - Enredeco de acesso com params: http://<server>:<porta>/lancamentos?resumo
	 */
	@GetMapping(params = "resumo")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.resumir(lancamentoFilter, pageable);
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> listar(@PathVariable Long codigo) {
		Lancamento lancamento = lancamentoRepository.findById(codigo).orElse(null);
		return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> remover(@PathVariable Long codigo) {
		lancamentoRepository.deleteById(codigo);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> adicionarLancamento(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		/* Ocorre autoincremento na tabela lancamento do banco quando passa um codigo de categoria ou pessoa que nao existem. 
		 * Verificar solucao para esse caso
		 */
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		
		publisher.publishEvent(new ResourceCreatedEvent(this, response, lancamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
	
	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	//TODO: Nao esta atualizando lancamento nem pessoa, verificar - 403-Forbidden
	public ResponseEntity<Lancamento> atualizarLancamento(@PathVariable Long codigo, @Valid @RequestBody Lancamento lancamento) {
		//Etapa 1: Atualiza as informacoe sdo lancamento conforme a requisicao realizada
		Lancamento lancamentoSalvo = lancamentoService.atualizarLancamento(codigo, lancamento);
		
		//Etapa 2: Retorna as informacoes para a requisicao
		return ResponseEntity.ok(lancamentoSalvo);
	}
	
	@ExceptionHandler( {PessoaInexistenteOuInativaException.class} )
	public ResponseEntity<Object> handlerPessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
	
	@GetMapping("/estatisticas/por-categoria")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<LancamentoEstatisticaPorCategoria> porCategoria() {
		//TODO: alterar codigo para possibilitar escolher o mes de referencia ?via paramentro na requisicao?
		return lancamentoRepository.porCategoria(LocalDate.of(2020, 6, 1));
	}
	
	@GetMapping("/estatisticas/por-dia")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<LancamentoEstatisticaPorDia> porDia() {
		//TODO: alterar codigo para possibilitar escolher o mes de referencia ?via paramentro na requisicao?
		return lancamentoRepository.porDia(LocalDate.of(2020, 6, 1));
	}
	
	@GetMapping("/relatorios/por-pessoa")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<byte[]> relatorioPorPessoa(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicial, 
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFinal) throws Exception {
		
		byte[] relatorio = lancamentoService.relatorioPorPessoa(dataInicial, dataFinal);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
		headers.add("Content-Disposition", "attachment; filename=relatorio-lancamentos-por-pessoa.pdf");
		return ResponseEntity.ok()
				.headers(headers)
				.body(relatorio);
	}
	
	@PostMapping("/anexo")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public Anexo anexarArquivo(@RequestParam MultipartFile anexo) throws IOException {
		String nome = s3.salvarTemporariamente(anexo);
		return new Anexo(nome, s3.configurarUrl(nome));
	}
	
}
