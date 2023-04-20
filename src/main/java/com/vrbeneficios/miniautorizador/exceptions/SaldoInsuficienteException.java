package com.vrbeneficios.miniautorizador.exceptions;

import com.vrbeneficios.miniautorizador.enums.ErrosTransacao;

public class SaldoInsuficienteException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1529347587835831372L;

	public SaldoInsuficienteException(ErrosTransacao msg) {
		super(msg.toString());
	}

}
