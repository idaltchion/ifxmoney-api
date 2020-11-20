package com.idaltchion.ifxmoney.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idaltchion.ifxmoney.api.model.Cidade;
import com.idaltchion.ifxmoney.api.repository.CidadeRepository;

@RestController
@RequestMapping("/cidades")
public class CidadeResource {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@GetMapping
	public List<Cidade> pesquisar(@RequestParam Long codigoEstado) {
		return cidadeRepository.findByEstadoCodigo(codigoEstado);
	}
	
}
