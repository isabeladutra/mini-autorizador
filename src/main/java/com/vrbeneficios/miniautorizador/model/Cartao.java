package com.vrbeneficios.miniautorizador.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cartoes")
public class Cartao {

	@Id 
	private String numeroCartao;
	private String senha;
	private double saldo;
}
