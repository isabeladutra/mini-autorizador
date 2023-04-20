package com.vrbeneficios.miniautorizador.exceptions;

import com.vrbeneficios.miniautorizador.enums.ErrosTransacao;

public class CartaoNaoExistenteException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 496382424355854277L;

	public CartaoNaoExistenteException(ErrosTransacao msg) {
		super(msg.toString());
	}

}
