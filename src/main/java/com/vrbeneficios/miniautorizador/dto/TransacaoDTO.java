package com.vrbeneficios.miniautorizador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class TransacaoDTO {

	public String numeroCartao;
	public String senhaCartao;
	public Double valor;

}
