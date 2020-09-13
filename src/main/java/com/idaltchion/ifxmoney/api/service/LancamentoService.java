package com.idaltchion.ifxmoney.api.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.idaltchion.ifxmoney.api.dto.LancamentoEstatisticaPorPessoa;
import com.idaltchion.ifxmoney.api.mail.Mailer;
import com.idaltchion.ifxmoney.api.model.Lancamento;
import com.idaltchion.ifxmoney.api.model.Pessoa;
import com.idaltchion.ifxmoney.api.model.Usuario;
import com.idaltchion.ifxmoney.api.repository.LancamentoRepository;
import com.idaltchion.ifxmoney.api.repository.PessoaRepository;
import com.idaltchion.ifxmoney.api.repository.UsuarioRepository;
import com.idaltchion.ifxmoney.api.service.exception.PessoaInexistenteOuInativaException;
import com.idaltchion.ifxmoney.api.storage.S3;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/*
 * Classe Services são utilizadas para efetuar regras de negocio da classe repository. Nesse caso Lancamento.
 */
@Service
public class LancamentoService {

	private static final String PERMISSAO = "ROLE_PESQUISAR_LANCAMENTO";
	
	/*
	 * A classe repository pode ser utilizada em qq parte do sistema? Nao viola o
	 * MVC? Verificar.
	 */
	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private Mailer mailer;
	
	@Autowired
	private S3 s3;
	
	private static final Logger logger = LoggerFactory.getLogger(Lancamento.class);
	
	public Lancamento salvar(@Valid Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.getOne(lancamento.getPessoa().getCodigo());
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		if (StringUtils.hasText(lancamento.getAnexo())) {
			s3.salvar(lancamento.getAnexo());
		}
		
		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizarLancamento(Long codigo, Lancamento lancamento) {
		// Etapa 1: busca o lancamento no banco
		Lancamento lancamentoSalvo = lancamentoRepository.findById(codigo).get();

		if (StringUtils.isEmpty(lancamento.getAnexo()) && StringUtils.hasText(lancamentoSalvo.getAnexo())) {
			s3.remover(lancamentoSalvo.getAnexo());
		}
		else if (StringUtils.hasText(lancamento.getAnexo()) && !lancamento.getAnexo().equals(lancamentoSalvo.getAnexo())) {
			s3.substituir(lancamentoSalvo.getAnexo(), lancamento.getAnexo());
		}
		
		// Etapa 2: Copia as propriedades do lancamento
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

		// Etapa 3: Salva o lancamento com as atualizacoes realizadas
		return lancamentoRepository.save(lancamentoSalvo);
	}
	
	public byte[] relatorioPorPessoa(LocalDate dataInicial, LocalDate dataFinal) throws Exception {
		List<LancamentoEstatisticaPorPessoa> dados = lancamentoRepository.porPessoa(dataInicial, dataFinal);
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIAL", Date.valueOf(dataInicial));
		parametros.put("DT_FINAL", Date.valueOf(dataFinal));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, new JRBeanCollectionDataSource(dados));
		
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
	/* second, minute, hour, day of month, month and day of week */
	@Scheduled(cron = "0 0 6 * * *")
	//@Scheduled(fixedDelay = 1000 * 60 * 5)
	public void notificacaoLancamentosVencidos() {
		if (logger.isDebugEnabled()) {
			logger.debug("Procedimento para envio de email iniciado");
		}
		List<Lancamento> lancamentosVencidos = lancamentoRepository
				.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
		if (lancamentosVencidos.isEmpty()) {
			logger.info("Nao existem lancamentos vencidos para envio de e-mail");
			return;
		}
		logger.info("Existem {} lancamentos vencidos", lancamentosVencidos.size());
		List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(PERMISSAO);
		if (destinatarios.isEmpty()) {
			logger.warn("Existem lancamentos vencidos para envio de e-mail, mas nenhum destinatario cadastrado");
			return;
		}
		mailer.notificarLancamentosVencidos(lancamentosVencidos, destinatarios);
		if (logger.isDebugEnabled()) {
			logger.debug("Procedimento para envio de email concluido");			
		}
	}
	

}
