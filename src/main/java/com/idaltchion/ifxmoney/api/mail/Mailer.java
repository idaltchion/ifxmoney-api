package com.idaltchion.ifxmoney.api.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.idaltchion.ifxmoney.api.model.Lancamento;
import com.idaltchion.ifxmoney.api.model.Usuario;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine thymeleaf;
	
	public void enviarEmail(String remetente,
			List<String> destinatarios, String assunto, String mensagem) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
		try {
			mimeHelper.setFrom(remetente);
			mimeHelper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			mimeHelper.setSubject(assunto);
			mimeHelper.setText(mensagem, true);
			mailSender.send(mimeMessage);
		}
		catch(MessagingException e) {
			throw new RuntimeException("Problema com o envio de e-mail: ", e);
		}
	}
	
	public void enviarEmail(String remetente,
			List<String> destinatarios, 
			String assunto, 
			String template, 
			Map<String, Object> variaveis) {
		
		Context context = new Context(new Locale("pt", "BR"));
		variaveis.entrySet().forEach(
				lancamento -> context.setVariable(lancamento.getKey(), lancamento.getValue()));
		String mensagem = thymeleaf.process(template, context);
		this.enviarEmail(remetente, destinatarios, assunto, mensagem);
	}
	
	public void notificarLancamentosVencidos(List<Lancamento> lancamentosVencidos, List<Usuario> destinatarios) {
		String template = "mail/notificacao-lancamentos-vencidos";
		Map<String, Object> variaveis = new HashMap<>();
		/* esse valor 'lancamentos' esta definido no template .html utilizado */
		variaveis.put("lancamentos", lancamentosVencidos);
		List<String> emails = destinatarios.stream()
				.map(usuario -> usuario.getEmail())
				.collect(Collectors.toList());
		this.enviarEmail("idaltchions@id.uff.br", emails, "Lançamentos Vencidos", template, variaveis);
	}
	
	/*
	import java.util.Arrays;
	import org.springframework.boot.context.event.ApplicationReadyEvent;
	import org.springframework.context.event.EventListener;
	import com.idaltchion.ifxmoney.api.repository.LancamentoRepository;
	@Autowired
	private LancamentoRepository lancamentoRepository;
	@EventListener
	private void sendEmailTest(ApplicationReadyEvent event) {
		String template = "mail/notificacao-lancamentos-vencidos";
		List<Lancamento> lista = lancamentoRepository.findAll();
		Map<String, Object> variaveis = new HashMap<>();
		// esse valor 'lancamentos' esta definido no template .html utilizado
		variaveis.put("lancamentos", lista); 
		this.enviarEmail("idaltchions@id.uff.br", 
				Arrays.asList("idaltchions@id.uff.br", "idaltchion@gmail.com"), 
				"Teste de envio de email via Spring", 
				template, variaveis);
		System.out.println("Envio de e-mail finalizado....");
	}
	*/
}
