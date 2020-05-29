package com.idaltchion.ifxmoney.api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.idaltchion.ifxmoney.api.config.token.CustomTokenEnhancer;

/*
 * Classe responsavel por fornecer o token para a aplicacao
 * Na consulta de dados pela API a autenticacao:
 * - utilizando token: sera necessario um access token e informar o token nas consultas
 * - utilizando basic: nao precisa mais do token, basta o usuario/senha da aplicacao 
 */
@Profile("oauth-security")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("angular")
				//.secret("{noop}@ngul@r0") //alterado apos a senha passar a ser encodada com BCrypt
				.secret("$2a$10$6VfRAOL1GT1mXtGmp2xvbeKjrJe3ecI9DZYn5iAqlDb/hm7W1EVcG")
				.scopes("read", "write")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(1800)
				.refreshTokenValiditySeconds(3600 * 24)
			.and()
				.withClient("mobile")
				.secret("$2a$10$wNzfWC8zvP1bq6g7Ru1AQ.5tpSCoNlzbsvPlACtK.QYboEqu9i4ua")
				.scopes("read")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(1800)
				.refreshTokenValiditySeconds(3600 * 24);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		/*
		 * TokenEnhancer esta sendo utilizado para incluir no accessToken o nome do usuario logado 
		 * Ver classes UsuarioSistema e CustomTokenEnhancer
		 */
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
		
		endpoints
			.tokenStore(tokenStore())
			.tokenEnhancer(tokenEnhancerChain)
			.reuseRefreshTokens(false)
			.authenticationManager(authenticationManager);
	}
	

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("ifxmoney");
		return accessTokenConverter;
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {	
		return new CustomTokenEnhancer();
	}
	
}
