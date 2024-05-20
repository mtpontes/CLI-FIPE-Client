package br.com.desafio.service;

import java.time.Year;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.desafio.model.veiculos.Dados;
import br.com.desafio.model.veiculos.ModeloEAnos;
import br.com.desafio.model.veiculos.Modelos;
import br.com.desafio.model.veiculos.Veiculo;

@Service
public class CliService {
	
	@Autowired
	private ApiConsumer consumer;
	private String URL_DINAMICA = "";

	
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
			System.out.println("Você escolheu a categoria Carros \n");
			URL_DINAMICA = "/carros/marcas";
			mostraMarcas(URL_DINAMICA);
			break;
			
		case 2:
			System.out.println("Você escolheu a categoria Motos \n");
			URL_DINAMICA = "/motos/marcas";
			mostraMarcas(URL_DINAMICA);
			break;
			
		case 3:
			System.out.println("Você escolheu a categoria Caminhoes \n");
			URL_DINAMICA = "/caminhoes/marcas";
			mostraMarcas(URL_DINAMICA);
			break;
			
		case 0:
			sair = 0;
			System.out.println("*** Programa finalizado ***");
			break;
			
		default:
			System.out.println("Você escolheu a categoria padrão, Carros \n");
			URL_DINAMICA = "/carros/marcas";
			mostraMarcas(URL_DINAMICA);
			break;
		}
	}
	
	private void mostraMarcas(String URL_CATEGORIA) {
		System.out.println("** Lista de marcas: **" + "\n");

		List<Dados> marcas = consumer.getDados(URL_CATEGORIA);
		marcas.stream()
			.sorted(Comparator.comparing(Dados::codigo))
			.forEach(System.out::println);
		System.out.println();
	}
	
	public List<Dados> mostraModelosMarca(Scanner leitura) {
		System.out.println("Digite o número da marca para a busca por modelos \n");
		
		String numeroMarca = leitura.nextLine();
		this.URL_DINAMICA = URL_DINAMICA + "/" + numeroMarca + "/modelos";

		Modelos modelos = consumer.getModelos(URL_DINAMICA);
		modelos.listaModelos().forEach(System.out::println);
		System.out.println();
		
		return modelos.listaModelos();
	}
	
	public ModeloEAnos mostraAnosDoModelo(Scanner leitura, List<Dados> modelos) {
		System.out.println("-- Digite o número do modelo -- \n");
		
		String numeroModelo = leitura.nextLine();
		Dados modelo = modelos.stream()
				.filter(d -> d.codigo().equals(numeroModelo))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("!!! Modelo não encontrado !!!"));

		this.URL_DINAMICA = URL_DINAMICA + "/" + numeroModelo + "/anos";
		List<Dados> anos = consumer.getDados(URL_DINAMICA);

		System.out.println("""
				Escolha o tipo de modelo:

				1 - Cinco mais recentes
				2 - Cinco mais antigos
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
		
		return new ModeloEAnos(modelo, anos);
	}
	
	public void mostraEdicoesEncontradas(List<Dados> anos, Dados modelo) {
		System.out.println("Edições encontradas para o modelo " + modelo.nome() + ": \n");

		anos.forEach(ano -> {
			String url = URL_DINAMICA + "/" + ano.codigo();
			
			Veiculo veiculo = consumer.getVeiculo(url);
			System.out.println(veiculo);
		});
	}
}