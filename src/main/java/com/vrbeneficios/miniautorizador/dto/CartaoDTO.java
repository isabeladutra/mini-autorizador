package com.vrbeneficios.miniautorizador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartaoDTO {

	private String numeroCartao;
	private String senha;

}
