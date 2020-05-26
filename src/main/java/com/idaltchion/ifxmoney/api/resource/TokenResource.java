package com.idaltchion.ifxmoney.api.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idaltchion.ifxmoney.api.config.property.IfxmoneyApiProperty;

/*
 * Estudar sobre refreshToken, accessToken e Cookie
 * - refreshToken: fica no cookie e no protocolo https (mais seguro), tempo mair
 * - accessToken: fica no client (javascript) e pode ser mais facilmente burlado, por isso o tempo eh menor 
 */

@RestController
@RequestMapping("/tokens")
public class TokenResource {

	@Autowired
	private IfxmoneyApiProperty ifxmoneyApiProperty;
	
	/*
	 * - O acesso a esse recurso vai ficar da seguinte maneira: http://<server>:<porta>/tokens/revoke
	 * - O "logout" vai ser simplesmente deixa o cookie (que tem o refreshToken) nulo e com tempo zerado
	 */
	@DeleteMapping("/revoke")
	public void revoke(HttpServletRequest req, HttpServletResponse resp) {
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setSecure(ifxmoneyApiProperty.getSeguranca().isEnableHttps());
		cookie.setHttpOnly(true); 
		cookie.setPath(req.getContextPath() + "/oauth/token");
		cookie.setMaxAge(0);
		
		resp.addCookie(cookie);
		resp.setStatus(HttpStatus.NO_CONTENT.value());
	}
	
}
