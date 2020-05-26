package com.idaltchion.ifxmoney.api.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.idaltchion.ifxmoney.api.config.property.IfxmoneyApiProperty;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
// Classe que ajusta as configuracoes do cabecalho (parametros CORS) para consultas atraves do browser  
public class CorsFilter implements Filter {
	
	
	@Autowired
	private IfxmoneyApiProperty ifxmoneyApiProperty;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {	
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		//todas as requisicoes precisam desses headers
		response.setHeader("Access-Control-Allow-Origin", ifxmoneyApiProperty.getOrigemPermitida());
		response.setHeader("Access-Control-Allow-Credentials", "true");
		
		//caso seja a primiera consulta aos recursos (OPTIONS - ver no cabecalho do browser) faz/add as seguintes autorizacoes no cabecalho
		if ("OPTIONS".equals(request.getMethod()) && ifxmoneyApiProperty.getOrigemPermitida().equals(request.getHeader("Origin"))  ) {
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
			response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
			response.setHeader("Access-Control-Allow-Max-Age", "3600");
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			chain.doFilter(req, resp);
		}
	}
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {		
	}


	@Override
	public void destroy() {
	}
	
}
