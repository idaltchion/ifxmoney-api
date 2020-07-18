package com.idaltchion.ifxmoney.api.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idaltchion.ifxmoney.api.dto.LancamentoEstatisticaPorPessoa;
import com.idaltchion.ifxmoney.api.model.Lancamento;
import com.idaltchion.ifxmoney.api.model.Pessoa;
import com.idaltchion.ifxmoney.api.repository.LancamentoRepository;
import com.idaltchion.ifxmoney.api.repository.PessoaRepository;
import com.idaltchion.ifxmoney.api.service.exception.PessoaInexistenteOuInativaException;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

}
