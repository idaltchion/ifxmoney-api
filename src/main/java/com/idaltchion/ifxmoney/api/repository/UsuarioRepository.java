package com.idaltchion.ifxmoney.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idaltchion.ifxmoney.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	/*
	 * entender relacao dessa instrucao com o tipo de relacionamento EAGER na classe Usuario
	 */
	public Optional<Usuario> findByEmail(String email);
	
	public List<Usuario> findByPermissoesDescricao(String permissaoDescricao);
}
