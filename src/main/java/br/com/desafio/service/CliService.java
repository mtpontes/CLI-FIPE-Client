package br.com.desafio.service;

import java.time.Year;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.desafio.model.carros.Dados;
import br.com.desafio.model.carros.Modelos;
import br.com.desafio.model.carros.Veiculo;

@Service
public class CliService {
	
	private ApiConsumer apiConsumer = new ApiConsumer();
	private DataConverter converter = new DataConverter();
	private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
	private String URL_DINAMICA = "/marcas";

	
	public void mostraMenu(Scanner leitura, int sair) {
		System.out.println();
		String menu = """
				*** MENU ***

				Digite uma das opções para consultar:

				1 - Carro
				2 - Moto
				3 - Caminhao
				0 - Sair 

				""";
		System.out.println(menu);
		Integer entrada = leitura.nextInt();
		leitura.nextLine();

		switch(entrada) {
		case 1:
			System.out.println("Você escolheu a categoria Carros");
			URL_DINAMICA = URL_BASE + "carros" + URL_DINAMICA;
			mostraMarcas(URL_DINAMICA);
			break;
			
		case 2:
			System.out.println("Você escolheu a categoria Motos");
			URL_DINAMICA = URL_BASE + "motos" + URL_DINAMICA;
			mostraMarcas(URL_DINAMICA);
			break;
			
		case 3:
			System.out.println("Você escolheu a categoria Caminhoes");
			URL_DINAMICA = URL_BASE + "caminhoes" + URL_DINAMICA;
			mostraMarcas(URL_DINAMICA);
			break;
			
		case 0:
			sair = 0;
			System.out.println("*** Programa finalizado ***");
			break;
			
		default:
			System.out.println("Você escolheu a categoria padrão, Carros");
			URL_DINAMICA = URL_BASE + "carros" + URL_DINAMICA;
			mostraMarcas(URL_DINAMICA);
			break;
		}
	}
	
	private void mostraMarcas(String URL_CATEGORIA) {
		System.out.println("** Lista de marcas: **" + "\n");

		String json = apiConsumer.getData(URL_CATEGORIA);
		List<Dados> marcas = converter.convertDataToList(json, Dados.class);

		marcas.stream()
			.sorted(Comparator.comparing(Dados::codigo))
			.forEach(System.out::println);
		System.out.println();
	}
	
	public List<Dados> mostraModelosMarca(Scanner leitura) {
		System.out.println("Digite o número da marca para a busca por modelos");
		System.out.println();
		
		String numeroMarca = leitura.nextLine();
		this.URL_DINAMICA = URL_DINAMICA + "/" + numeroMarca + "/modelos";
		String json = apiConsumer.getData(URL_DINAMICA);
		Modelos modelos = converter.convertData(json, Modelos.class);
		
		modelos.listaModelos().forEach(System.out::println);
		System.out.println();
		
		return modelos.listaModelos();
	}
	
	public Map<String, Object> mostraAnosDoModelo(Scanner leitura, List<Dados> modelos) {
		System.out.println("-- Digite o número do modelo -- ");
		System.out.println();
		
		String numeroModelo = leitura.nextLine();
		Dados modelo = modelos.stream()
				.filter(d -> d.codigo().equals(numeroModelo))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("!!! Modelo não encontrado !!!"));

		this.URL_DINAMICA = URL_DINAMICA + "/" + numeroModelo + "/anos";
		String json = apiConsumer.getData(URL_DINAMICA);
		List<Dados> anos = converter.convertData(json, new TypeReference<List<Dados>>() {});

		System.out.println("""
				Escolha o tipo de modelo:

				1 - 5 mais recentes
				2 - 5 mais antigos
				3 - Inferior a (insira o ano)
				""");

		int tipo = leitura.nextInt(); 
		leitura.nextLine();

		switch (tipo) {
			case 1:
				anos = anos.stream()
					.limit(5)
					.collect(Collectors.toList());
				break; 
	
			case 2: 
				anos = anos.stream()
					.skip(Math.max(0, anos.size() - 5))
					.collect(Collectors.toList());
				break;
	
			default:
				if (tipo < 1950 || tipo > Year.now().getValue()) {
					anos = null;
					break;
				}
				
				anos = anos.stream()
						.filter(d -> Integer.valueOf(d.codigo().split("-")[0]) < tipo)
						.collect(Collectors.toList());
				break;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("modelo", modelo);
		map.put("anos", anos);
		
		return map;
	}
	
	public void mostraEdicoesEncontradas(List<Dados> anos, Dados modelo) {
		System.out.println("Edições encontradas para o modelo " + modelo.nome() + ": ");
		System.out.println();

		anos.forEach(ano -> {
			String url = URL_DINAMICA + "/" + ano.codigo();
			String jsonResponse = apiConsumer.getData(url);
			Veiculo veiculo = converter.convertData(jsonResponse, Veiculo.class);
			System.out.println(ano.codigo().split("-")[0] + " - " + veiculo);
		});
	}
}