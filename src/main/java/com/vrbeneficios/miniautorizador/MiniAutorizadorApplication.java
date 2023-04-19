package com.vrbeneficios.miniautorizador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vrbeneficios.miniautorizador.dto.CartaoDTO;
import com.vrbeneficios.miniautorizador.exceptions.CartaoExistenteException;
import com.vrbeneficios.miniautorizador.mappers.CartaoDTOMapper;
import com.vrbeneficios.miniautorizador.model.Cartao;
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
	public ResponseEntity<CartaoDTO> adicionarCartao(@RequestBody Cartao cartao) {
		Cartao novoCartao = cartao;
		CartaoDTO cartaoDTO = new CartaoDTO();
		HttpStatus returnStatusCode = HttpStatus.CREATED;
		
		
		try {
			novoCartao = cartoesService.salvarCartao(cartao.getNumeroCartao(), cartao.getSenha());
			
		} catch (CartaoExistenteException e) {
			returnStatusCode = HttpStatus.UNPROCESSABLE_ENTITY;
			//return new ResponseEntity<CartaoDTO>(cartaoDTO, HttpStatus.UNPROCESSABLE_ENTITY);
			//e.printStackTrace();
		}
		cartaoDTO = CartaoDTOMapper.mapper(novoCartao);
		return new ResponseEntity<CartaoDTO>(cartaoDTO, returnStatusCode);
		
	}

	@GetMapping(value = "/cartoes/{numeroCartao}")
	public ResponseEntity<Double> consultarSaldoCartao(@PathVariable String numeroCartao)  {
		Cartao cartao = cartoesService.findByNumeroCartao(numeroCartao);
		if (cartao != null) {
			return new ResponseEntity<Double>(cartao.getSaldo(), HttpStatus.OK);
		}
		return new ResponseEntity<Double>(HttpStatus.NOT_FOUND);
	}
	
}
