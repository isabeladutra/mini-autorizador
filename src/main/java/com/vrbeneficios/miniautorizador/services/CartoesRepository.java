package com.vrbeneficios.miniautorizador.services;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vrbeneficios.miniautorizador.model.Cartao;

public interface CartoesRepository extends MongoRepository<Cartao, String>{
	

	Cartao findByNumeroCartao(String numeroCartao);
}
