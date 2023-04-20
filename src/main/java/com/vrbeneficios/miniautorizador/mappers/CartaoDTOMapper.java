package com.vrbeneficios.miniautorizador.mappers;

import com.vrbeneficios.miniautorizador.dto.CartaoDTO;
import com.vrbeneficios.miniautorizador.model.Cartao;
import lombok.Data;

@Data
public class CartaoDTOMapper {

	public static CartaoDTO mapper(Cartao cartao) {
		CartaoDTO cartaodto = new CartaoDTO();
		cartaodto.setNumeroCartao(cartao.getNumeroCartao());
		cartaodto.setSenha(cartao.getSenha());
		return cartaodto;
	}

}
