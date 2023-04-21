package com.vrbeneficios.miniautorizador.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.Assert;

import com.vrbeneficios.miniautorizador.dto.TransacaoDTO;
import com.vrbeneficios.miniautorizador.exceptions.CartaoExistenteException;
import com.vrbeneficios.miniautorizador.exceptions.CartaoNaoExistenteException;
import com.vrbeneficios.miniautorizador.exceptions.SaldoInsuficienteException;
import com.vrbeneficios.miniautorizador.exceptions.SenhaInvalidaException;
import com.vrbeneficios.miniautorizador.model.Cartao;

public class CartoesServiceTest {
	private AutoCloseable closeable;
	private CartoesRepository cartoesRepository = Mockito.mock(CartoesRepository.class);

	@BeforeEach
	public void initMocks() {
		 closeable = MockitoAnnotations.openMocks(this);
	}
	@AfterEach 
	public void releaseMocks() throws Exception {
        closeable.close();
    }
	
	@Test
	public void testaSalvarCartaoJaExistente() throws CartaoExistenteException {
		CartoesService cartoesService = new CartoesService();
		cartoesService.setCartoesRepo(cartoesRepository);
		Cartao cartao = new Cartao("12345", "abc", 500.00);
		when(cartoesRepository.findByNumeroCartao(any())).thenReturn(cartao);
		assertThrows(CartaoExistenteException.class, () -> {cartoesService.salvarCartao("1234", "abcd");});
				
	}
	
	@Test
	public void testaSalvarCartaoNovo() throws CartaoExistenteException {
		CartoesService cartoesService = new CartoesService();
		cartoesService.setCartoesRepo(cartoesRepository);
		Cartao cartao = new Cartao("12345", "abc", 500.00);
		when(cartoesRepository.findByNumeroCartao(any())).thenReturn(null);
		when(cartoesRepository.save(any())).thenReturn(cartao);
		cartoesService.salvarCartao("1234", "abcd");
		verify(cartoesRepository, times(1)).save(any());
		
	}
	
	@Test
	public void testaRealizarTransacaoComSucesso() throws CartaoNaoExistenteException, SenhaInvalidaException, SaldoInsuficienteException {
		CartoesService cartoesService = new CartoesService();
		cartoesService.setCartoesRepo(cartoesRepository);
		TransacaoDTO transacaoDTO = new TransacaoDTO("1234", "abcd", 10.00);
		Cartao cartao  = Mockito.mock(Cartao.class);
		when(cartao.getNumeroCartao()).thenReturn("1234");
		when(cartao.getSaldo()).thenReturn(500.00);
		when(cartao.getSenha()).thenReturn("abcd");
		when(cartoesRepository.findByNumeroCartao(any())).thenReturn(cartao);
		cartoesService.realizaTransacao(transacaoDTO);
		verify(cartao, times(1)).setSaldo(any());
	}
	
	@Test
	public void testaRealizarTransacaoComCartaoInexistente() throws CartaoNaoExistenteException, SenhaInvalidaException, SaldoInsuficienteException {
		CartoesService cartoesService = new CartoesService();
		cartoesService.setCartoesRepo(cartoesRepository);
		TransacaoDTO transacaoDTO = new TransacaoDTO("1234", "abcd", 10.00);
		when(cartoesRepository.findByNumeroCartao(any())).thenReturn(null);
		assertThrows(CartaoNaoExistenteException.class, () -> {cartoesService.realizaTransacao(transacaoDTO);});
	}
	
	@Test
	public void testaRealizarTransacaoComSenhaInvalida() throws CartaoNaoExistenteException, SenhaInvalidaException, SaldoInsuficienteException {
		CartoesService cartoesService = new CartoesService();
		cartoesService.setCartoesRepo(cartoesRepository);
		TransacaoDTO transacaoDTO = new TransacaoDTO("1234", "abcd", 10.00);
		when(cartoesRepository.findByNumeroCartao(any())).thenReturn(new Cartao("1234", "ab", 500.00));
		assertThrows(SenhaInvalidaException.class, () -> {cartoesService.realizaTransacao(transacaoDTO);});
	}
	
	@Test
	public void testaRealizarTransacaoComSaldoInsuficiente() throws CartaoNaoExistenteException, SenhaInvalidaException, SaldoInsuficienteException {
		CartoesService cartoesService = new CartoesService();
		cartoesService.setCartoesRepo(cartoesRepository);
		TransacaoDTO transacaoDTO = new TransacaoDTO("1234", "abcd", 200.00);
		when(cartoesRepository.findByNumeroCartao(any())).thenReturn(new Cartao("1234", "abcd", 100.00));
		assertThrows(SaldoInsuficienteException.class, () -> {cartoesService.realizaTransacao(transacaoDTO);});
	}

}
