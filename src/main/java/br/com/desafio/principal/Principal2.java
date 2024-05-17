package br.com.desafio.principal;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.desafio.model.carros.Dados;
import br.com.desafio.model.carros.Modelos;
import br.com.desafio.model.carros.Veiculo;
import br.com.desafio.service.ApiConsumer;
import br.com.desafio.service.DataConverter;

public class Principal2 {

	private Scanner leitura = new Scanner(System.in);
	private ApiConsumer apiConsumer = new ApiConsumer();
	private DataConverter converter = new DataConverter();
	private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";


	public void menu() {
		var sair = -1;

		while (true) {
			System.out.println();
			String menu = """
					*** MENU ***

					Digite um das opções para consultar:

					1 - Carro
					2 - Moto
					3 - Caminhao
					0 - Sair 

					""";
			System.out.println(menu);
			Integer entrada = leitura.nextInt();
			leitura.nextLine();
			String URL_CATEGORIA = "/marcas";

			switch(entrada) {
			case 1:
				System.out.println("-- Você escolheu a categoria Carros-- ");
				URL_CATEGORIA = URL_BASE + "carros" + URL_CATEGORIA;
				mostraMarcas(URL_CATEGORIA);
				break;
			case 2:
				System.out.println("-- Você escolheu a categoria Motos -- ");
				URL_CATEGORIA = URL_BASE + "motos" + URL_CATEGORIA;
				mostraMarcas(URL_CATEGORIA);
				break;
			case 3:
				System.out.println("-- Você escolheu a categoria Caminhoes -- ");
				URL_CATEGORIA = URL_BASE + "caminhoes" + URL_CATEGORIA;
				mostraMarcas(URL_CATEGORIA);
				break;
			case 0:
				sair = 0;
				System.out.println("*** Programa finalizado ***");
				break;
			default:
				System.out.println("-- Você escolheu a categoria padrão, Carros --");
				URL_CATEGORIA = URL_BASE + "carros" + URL_CATEGORIA;
				mostraMarcas(URL_CATEGORIA);
				break;
			}
			if(sair == 0) break;

			//mostra a lista de modelos da marca
			System.out.println("-- Digite o número da marca para a busca por modelos -- ");
			pula();
			String numeroMarca = leitura.nextLine();
			String urlModelos = URL_CATEGORIA + "/" + numeroMarca + "/modelos";
			String json = apiConsumer.getData(urlModelos);
			Modelos modelos = converter.convertData(json, Modelos.class);
			modelos.listaModelos().forEach(System.out::println);
			pula();



			//mostra a lista de anos do modelo
			System.out.println("-- Digite o número do modelo -- ");
			pula();
			String numeroModelo = leitura.nextLine();

			Dados modelo = modelos.listaModelos().stream()
					.filter(d -> d.codigo().equals(numeroModelo))
					.findFirst()
					.orElseThrow(() -> new RuntimeException("!!! Modelo não encontrado !!!"));

			String urlAnos = urlModelos + "/" + numeroModelo + "/anos";
			json = apiConsumer.getData(urlAnos);
			List<Dados> anos = converter.convertData(json, new TypeReference<List<Dados>>() {});

			System.out.println("""
					Escolha o tipo de modelo:

					1 - 5 mais recentes
					2 - 5 mais antigos
					3 - Inferior a (ano)
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


			case 3:
				System.out.println("-- Insira o ano --");
				int inferior = leitura.nextInt(); 
				leitura.nextLine();

				anos = anos.stream()
						.filter(d -> Integer.valueOf(d.codigo().split("-")[0]) < inferior)
						.collect(Collectors.toList());
				break;

			default:
				throw new IllegalArgumentException("Valor inesperado: " + tipo);
			}

			if (anos == null || anos.isEmpty()) {
				System.out.println("Nenhum modelo encontrado para este ano");
				break;

			} else {
				System.out.println("Estes são os valores encontrados para o modelo " + modelo.nome() + ": ");
				pula();

				anos.forEach(ano -> {
					String urlPrecos = urlAnos + "/" + ano.codigo();
					String jsonResponse = apiConsumer.getData(urlPrecos);
					Veiculo veiculo = converter.convertData(jsonResponse, Veiculo.class);
					System.out.println("Ano: " + ano.codigo() + " - Valor: " + veiculo.valor());
				});
			}
		}
	}

	//mostra a lista de marcas de veículos
	private void mostraMarcas(String URL_CATEGORIA) {
		System.out.println("** Lista de marcas: **" + "\n");

		String json = apiConsumer.getData(URL_CATEGORIA);
		List<Dados> marcas = converter.convertDataToList(json, Dados.class);

		marcas.stream()
		.sorted(Comparator.comparing(Dados::codigo))
		.forEach(System.out::println);
		pula();
	}

	private void pula() {
		System.out.println();
	}
}