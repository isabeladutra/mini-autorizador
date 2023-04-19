package com.vrbeneficios.miniautorizador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.vrbeneficios.miniautorizador.exceptions.CartaoExistenteException;
import com.vrbeneficios.miniautorizador.services.Cartao;
import com.vrbeneficios.miniautorizador.services.CartoesService;

@SpringBootApplication
@RestController
public class MiniAutorizadorApplication {
	
	@Autowired
	CartoesService cartoesService;

	public static void main(String[] args) {
		SpringApplication.run(MiniAutorizadorApplication.class, args);
	}
	
	@PostMapping(value = "/cartoes")
	public ResponseEntity<Cartao> adicionarCartao(@RequestBody Cartao cartao) {
		Cartao novoCartao = cartao;
		try {
			novoCartao = cartoesService.salvarCartao(cartao.getNumeroCartao(), cartao.getSenha());
		} catch (CartaoExistenteException e) {
			return new ResponseEntity<Cartao>(novoCartao, HttpStatus.UNPROCESSABLE_ENTITY);
			//e.printStackTrace();
		}
		return new ResponseEntity<Cartao>(novoCartao, HttpStatus.CREATED);
		
	}

}
