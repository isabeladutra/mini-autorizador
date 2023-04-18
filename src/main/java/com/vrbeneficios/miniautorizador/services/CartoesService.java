package com.vrbeneficios.miniautorizador.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vrbeneficios.miniautorizador.exceptions.CartaoNaoEncontradoException;

@Service
public class CartoesService {
	
	@Autowired
	private CartoesRepository cartoesRepository;

	public Cartao salvarCartao(String numeroCartao, String senha) throws CartaoNaoEncontradoException {
		if (findByNumeroCartao(numeroCartao) != null) {
			 throw new CartaoNaoEncontradoException("cartao nao existe");
			 }
		return cartoesRepository.save(new Cartao(numeroCartao, senha, 500.00));	
	}
	
	public Cartao findByNumeroCartao(String numeroCartao) {
		return cartoesRepository.findByNumeroCartao(numeroCartao);
	}
}