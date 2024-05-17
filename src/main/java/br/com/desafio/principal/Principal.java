package br.com.desafio.principal;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.desafio.model.carros.Dados;

@Service
public class Principal {

	private Scanner leitura = new Scanner(System.in);
	
	@Autowired
	private CliService cli;


	public void menu() {
		var sair = -1;
		List<Dados> dados = null;

		while (true) {
			cli.mostraMenu(leitura, sair);
			if(sair == 0) 
				break;

			dados = cli.mostraModelosMarca(leitura);
			
			var mapa = cli.mostraAnosDoModelo(leitura, dados);
			List<Dados> anos = (List<Dados>) mapa.get("anos");
			Dados modelo = (Dados) mapa.get("modelo");
			
			if (anos == null || anos.isEmpty()) {
				System.out.println("Nenhum modelo encontrado para este ano \n");
				break;
			}
			cli.mostraEdicoesEncontradas(anos, modelo);
		}
	}
}