package com.idaltchion.ifxmoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
* A anotação ResourceServerConfigurerAdapter gera um WebSecurityConfigurerAdapter.
* A Order do WebSecurityConfigurerAdapter gerado pelo ResourceServerConfigurerAdapter é 3.
* A Order deste WebSecurityConfigurerAdapter deve ser > 3, para que o configure(HttpSecurity http) do
* ResourceServerConfig tenha precedência sobre o configure(HttpSecurity http) do SecurityConfig.
* 
* Observacoes adicionais:
* - Em algum momento essa classe foi denominada "ResourceServerConfig", agora e SecurityConf - verificar momento dessa confusao
* - Agora essa classe nao sera mais necessaria, sendo algumas funcoes distribuidas para as novas classes 
* 		"ResourceServerConfig" e "OAuthServerConfig"
*/
@Configuration
/*
 * anotacao movida para nova classe "ResourceServerConfig"
 */
//@EnableWebSecurity 
@Order(4)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// https://stackoverflow.com/questions/46999940/spring-boot-passwordencoder-error
		// auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ROLE");
		/*
		 * - entender melhor funcionamento dessa classe padrao UserDetailsService
		 * - ver tambem nova classe customizada AppUserDetaislService
		 * - a senha no banco esta "encodada" utilizando BCrypt, por isso a utilizacao do passwordEncoder
		 */
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/*
	 * porque esse Bean? entender
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
