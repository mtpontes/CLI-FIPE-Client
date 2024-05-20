package br.com.desafio.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.desafio.model.carros.Dados;
import br.com.desafio.model.carros.Modelos;
import br.com.desafio.model.carros.Veiculo;

@FeignClient(name = "fipeClient", url = "https://parallelum.com.br/fipe/api/v1")
public interface ApiConsumer {
	
	@GetMapping(value = "{path}")
	List<Dados> getDados(@PathVariable String path);

	@GetMapping(value = "{path}")
	Modelos getModelos(@PathVariable String path);

	@GetMapping(value = "{path}")
	Veiculo getVeiculo(@PathVariable String path);
}