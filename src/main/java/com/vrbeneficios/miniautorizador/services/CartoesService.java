package com.vrbeneficios.miniautorizador.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vrbeneficios.miniautorizador.dto.TransacaoDTO;
import com.vrbeneficios.miniautorizador.enums.ErrosTransacao;
import com.vrbeneficios.miniautorizador.exceptions.CartaoExistenteException;
import com.vrbeneficios.miniautorizador.exceptions.CartaoNaoExistenteException;
import com.vrbeneficios.miniautorizador.exceptions.SaldoInsuficienteException;
import com.vrbeneficios.miniautorizador.exceptions.SenhaInvalidaException;
import com.vrbeneficios.miniautorizador.model.Cartao;

@Service
public class CartoesService {
	


	@Autowired
	private CartoesRepository cartoesRepository;

	public Cartao salvarCartao(String numeroCartao, String senha) throws CartaoExistenteException {
		if (findByNumeroCartao(numeroCartao) != null) {
			throw new CartaoExistenteException("cartao existe");
		}
		return cartoesRepository.save(new Cartao(numeroCartao, senha, 500.00));
	}
	
	synchronized public  void realizaTransacao(TransacaoDTO transacaoDTO) throws CartaoNaoExistenteException, SenhaInvalidaException, SaldoInsuficienteException {
		//primeiro verificar se cartao existe
		Cartao cartao = findByNumeroCartao(transacaoDTO.getNumeroCartao());
		if (cartao != null) {
			// se o cartao existir, verifica se a senha é a correta
			if (transacaoDTO.getSenhaCartao().equals(cartao.getSenha())){
			//verifica se possui saldo disponivel (se o valor da transacao for maior que o saldo n dá, se for menor ou igual, dá
			Double valorSaldo = cartao.getSaldo();
			Double valorTransacao = transacaoDTO.getValor();
			if (valorTransacao <= valorSaldo) {
				//realiza a transacao alterando o saldo do cartao
				cartao.setSaldo(valorSaldo-valorTransacao);
				cartoesRepository.save(cartao);
			}else {
				//saldo indisponivel
				throw new SaldoInsuficienteException(ErrosTransacao.SALDO_INSUFICIENTE);
			}
			}else {
				//senha invalida
				throw new SenhaInvalidaException(ErrosTransacao.SENHA_INVALIDA);
			}
		}else {
			//cartao nao existe
			throw new CartaoNaoExistenteException(ErrosTransacao.CARTAO_INEXISTENTE);
		}
		
	}

	public Cartao findByNumeroCartao(String numeroCartao) {
		return cartoesRepository.findByNumeroCartao(numeroCartao);
	}
}
