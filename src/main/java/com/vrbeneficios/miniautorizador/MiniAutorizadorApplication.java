package com.vrbeneficios.miniautorizador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.vrbeneficios.miniautorizador.dto.CartaoDTO;
import com.vrbeneficios.miniautorizador.dto.TransacaoDTO;
import com.vrbeneficios.miniautorizador.exceptions.CartaoExistenteException;
import com.vrbeneficios.miniautorizador.exceptions.CartaoNaoExistenteException;
import com.vrbeneficios.miniautorizador.exceptions.SaldoInsuficienteException;
import com.vrbeneficios.miniautorizador.exceptions.SenhaInvalidaException;
import com.vrbeneficios.miniautorizador.mappers.CartaoDTOMapper;
import com.vrbeneficios.miniautorizador.model.Cartao;
import com.vrbeneficios.miniautorizador.services.CartoesService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@OpenAPIDefinition(info = @Info(title = "Mini-autorizador", description = "API que implementa regras de autorização para o processamento de transações de Vale Refeição e Vale Alimentação", contact = @Contact(name="Isabela Dutra", email = "dutraisabela88@gmail.com")))
@SpringBootApplication
@RestController
public class MiniAutorizadorApplication {

	@Autowired
	CartoesService cartoesService;
	
	public void setCartoesService(CartoesService cartoeService) {
		this.cartoesService = cartoeService;
	}

	public static void main(String[] args) {
		SpringApplication.run(MiniAutorizadorApplication.class, args);
	}

	@Operation(summary = "Cria um novo cartão", responses = {
    @ApiResponse(responseCode = "201", description = "Cartão criado com Sucesso",content = @Content(mediaType ="application/json")),
    @ApiResponse(responseCode = "422", description = "Falha : Cartão já existe",content = @Content(mediaType ="application/json"))})
	@PostMapping(value = "/cartoes", consumes="application/json", produces="application/json")
	public ResponseEntity<CartaoDTO> adicionarCartao(@RequestBody Cartao cartao) {
		Cartao novoCartao = cartao;
		CartaoDTO cartaoDTO = new CartaoDTO();
		HttpStatus returnStatusCode = HttpStatus.CREATED;

		try {
			novoCartao = cartoesService.salvarCartao(cartao.getNumeroCartao(), cartao.getSenha());

		} catch (CartaoExistenteException e) {
			returnStatusCode = HttpStatus.UNPROCESSABLE_ENTITY;
			
		}
		cartaoDTO = CartaoDTOMapper.mapper(novoCartao);
		return new ResponseEntity<CartaoDTO>(cartaoDTO, returnStatusCode);

	}
    
    @Operation(summary = "Consulta o saldo do cartão", responses = {
    @ApiResponse(responseCode = "200", description = "Consulta Realizada com Sucesso", content = @Content(mediaType ="application/json")),
    @ApiResponse(responseCode = "404", description = "Cartão Inexistente")})
	@GetMapping(value = "/cartoes/{numeroCartao}")
	public ResponseEntity<Double> consultarSaldoCartao(@PathVariable String numeroCartao) {
		Cartao cartao = cartoesService.findByNumeroCartao(numeroCartao);
		if (cartao != null) {
			return new ResponseEntity<Double>(cartao.getSaldo(), HttpStatus.OK);
		}
		return new ResponseEntity<Double>(HttpStatus.NOT_FOUND);
	}

    @Operation(summary = "Realiza uma transação", responses = {
    @ApiResponse(responseCode = "201", description = "Transação Realizada com Sucesso"),
    @ApiResponse(responseCode = "422", description = "Falha na Transação")})
	@PostMapping(value = "/transacoes")
	public ResponseEntity<String> realizarTransacao(@RequestBody TransacaoDTO transacaoDTO) {

		try {
			cartoesService.realizaTransacao(transacaoDTO);
		} catch (CartaoNaoExistenteException | SenhaInvalidaException | SaldoInsuficienteException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}

		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

}
