package com.idaltchion.ifxmoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 * Profile: formas de setar propriedades conforme a necessidade, tal como por tipo de ambiente (dev, prod, etc) entre outras necessidades
 * Ver utilizacao na classe RefrehTokenPostProcessor
 * Ver arquivo Procfile na raiz da aplicacao
 */
@ConfigurationProperties("ifxmoney")
public class IfxmoneyApiProperty {

	private String origemPermitida = "http://localhost:8000";

	public String getOrigemPermitida() {
		return origemPermitida;
	}

	public void setOrigemPermitida(String origemPermitida) {
		this.origemPermitida = origemPermitida;
	}


	private final Seguranca seguranca = new Seguranca();
	public Seguranca getSeguranca() {
		return seguranca;
	}

	public static class Seguranca {

		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}

	}
	
	private final Mail mail = new Mail();
	public Mail getMail() {
		return mail;
	}
	
	public static class Mail {
		private String host;
		private Integer port;
		private String usuario;
		private String password;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public String getUsuario() {
			return usuario;
		}

		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}

}
