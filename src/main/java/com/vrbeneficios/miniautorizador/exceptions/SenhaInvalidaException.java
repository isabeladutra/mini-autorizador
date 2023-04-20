package com.vrbeneficios.miniautorizador.exceptions;

import com.vrbeneficios.miniautorizador.enums.ErrosTransacao;

public class SenhaInvalidaException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3305901034301871804L;

	public SenhaInvalidaException(ErrosTransacao msg) {
		super(msg.toString());
	}

}
