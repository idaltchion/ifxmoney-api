package com.idaltchion.ifxmoney.api.service.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorDeSenha {
	
	public static void main(String[] args) {
		//TODO: fazer um metodo decrypt
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("m0b1l30"));
	}

}
