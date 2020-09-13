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

	/*
	 * Seguranca properties
	 */
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
	
	/*
	 * Mail properties
	 */
	private final Mail mail = new Mail();

	public Mail getMail() {
		return mail;
	}

	public static class Mail {
		private String host;
		private Integer port;
		private String username;
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

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}

	/*
	 * AmazonS3 properties
	 */
	private final S3 s3 = new S3();
	
	public S3 getS3() {
		return s3;
	}

	public static class S3 {
		private String accessKey;
		private String secretAccessKey;
		private String bucket = "ifx-ifxmoney-files";
		
		public String getBucket() {
			return bucket;
		}
		
		public void setBucket(String bucket) {
			this.bucket = bucket;
		}
		
		public String getAccessKey() {
			return accessKey;
		}

		public void setAccessKey(String accessKey) {
			this.accessKey = accessKey;
		}

		public String getSecretAccessKey() {
			return secretAccessKey;
		}

		public void setSecretAccessKey(String secretAccessKey) {
			this.secretAccessKey = secretAccessKey;
		}
		
	}

}
