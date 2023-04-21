package com.vrbeneficios.miniautorizador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import com.vrbeneficios.miniautorizador.dto.CartaoDTO;
import com.vrbeneficios.miniautorizador.dto.TransacaoDTO;
import com.vrbeneficios.miniautorizador.exceptions.CartaoExistenteException;
import com.vrbeneficios.miniautorizador.exceptions.CartaoNaoExistenteException;
import com.vrbeneficios.miniautorizador.exceptions.SaldoInsuficienteException;
import com.vrbeneficios.miniautorizador.exceptions.SenhaInvalidaException;
import com.vrbeneficios.miniautorizador.model.Cartao;
import com.vrbeneficios.miniautorizador.services.CartoesService;

public class MiniAutorizadorAppTests {

	private CartoesService cartoesService = Mockito.mock(CartoesService.class);
	private AutoCloseable closeable;

	@BeforeEach
	public void initMocks() {
		 closeable = MockitoAnnotations.openMocks(this);
	}
	@AfterEach 
	public void releaseMocks() throws Exception {
        closeable.close();
    }

	@Test
	public void testaConsultarSaldoCartao() throws CartaoExistenteException {
		MiniAutorizadorApplication app = new MiniAutorizadorApplication();
		Cartao cartao = new Cartao("12345", "abc", 500.00);
		when(cartoesService.findByNumeroCartao(any())).thenReturn(cartao);
		app.setCartoesService(cartoesService);
		ResponseEntity<Double> resposta = app.consultarSaldoCartao("12345");
		Assert.isInstanceOf(ResponseEntity.class, resposta);

	}
	
	@Test
	public void testaConsultarSaldoCartaoInexistente() {
		MiniAutorizadorApplication app = new MiniAutorizadorApplication();
		when(cartoesService.findByNumeroCartao(any())).thenReturn(null);
		app.setCartoesService(cartoesService);
		ResponseEntity<Double> resposta = app.consultarSaldoCartao("12345");
		assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
		
	}

	@Test
	public void testaAdicionarCartao() throws CartaoExistenteException {
		MiniAutorizadorApplication app = new MiniAutorizadorApplication();
		Cartao cartao = new Cartao("12345", "abc", 500.00);
		app.setCartoesService(cartoesService);
		when(cartoesService.salvarCartao(any(), any())).thenReturn(cartao);
		ResponseEntity<CartaoDTO> retorno = app.adicionarCartao(cartao);
		Assert.isInstanceOf(CartaoDTO.class, retorno.getBody());
	}
	
	@Test
	public void testaCriarCartaoQueJaExiste() throws CartaoExistenteException {
		MiniAutorizadorApplication app = new MiniAutorizadorApplication();
		Cartao cartao = new Cartao("12345", "abc", 500.00);
		app.setCartoesService(cartoesService);
		when(cartoesService.salvarCartao(any(), any())).thenThrow(CartaoExistenteException.class);
		ResponseEntity<CartaoDTO> retorno = app.adicionarCartao(cartao);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, retorno.getStatusCode());
	}
	
	@Test
	public void testaRealizarTransacao() {
		MiniAutorizadorApplication app = new MiniAutorizadorApplication();
		TransacaoDTO transacaoDTO = new TransacaoDTO("1234", "abcd", 10.00);
		app.setCartoesService(cartoesService);
		ResponseEntity<String> retorno = app.realizarTransacao(transacaoDTO);
		assertEquals("OK", retorno.getBody());		
		
	}
	
	
	@Test()
	public void testaRealizarTransacaoCartaoInvalido() throws CartaoNaoExistenteException, SenhaInvalidaException, SaldoInsuficienteException {
		MiniAutorizadorApplication app = new MiniAutorizadorApplication();
		TransacaoDTO transacaoDTO = new TransacaoDTO("1234", "abcd", 10.00);
		app.setCartoesService(cartoesService);
		doThrow(CartaoNaoExistenteException.class).when(cartoesService).realizaTransacao(transacaoDTO);
		ResponseEntity<String> retorno = app.realizarTransacao(transacaoDTO);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, retorno.getStatusCode());
	}
	
	@Test
	public void testaRealizarTransacaoComSenhaInvalida() throws CartaoNaoExistenteException, SenhaInvalidaException, SaldoInsuficienteException {
		MiniAutorizadorApplication app = new MiniAutorizadorApplication();
		TransacaoDTO transacaoDTO = new TransacaoDTO("1234", "abcd", 10.00);
		app.setCartoesService(cartoesService);
		doThrow(SenhaInvalidaException.class).when(cartoesService).realizaTransacao(transacaoDTO);
		ResponseEntity<String> retorno = app.realizarTransacao(transacaoDTO);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, retorno.getStatusCode());
		
	}
	
	
	@Test
	public void testaRealizarTransacaoComSaldoInsuficiente() throws CartaoNaoExistenteException, SenhaInvalidaException, SaldoInsuficienteException {
		MiniAutorizadorApplication app = new MiniAutorizadorApplication();
		TransacaoDTO transacaoDTO = new TransacaoDTO("1234", "abcd", 10.00);
		app.setCartoesService(cartoesService);
		doThrow(SaldoInsuficienteException.class).when(cartoesService).realizaTransacao(transacaoDTO);
		ResponseEntity<String> retorno = app.realizarTransacao(transacaoDTO);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, retorno.getStatusCode());
		
	}
}
