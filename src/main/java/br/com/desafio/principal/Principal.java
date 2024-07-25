package br.com.desafio.principal;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.desafio.model.veiculos.Dados;
import br.com.desafio.service.CliService;

@Service
public class Principal {
	
	@Autowired
	private CliService cli;
	private Scanner leitura = new Scanner(System.in);

	
	public void menu() {
		var sair = -1;
		List<Dados> dados = null;

		while (true) {
			cli.mostraMenu(leitura, sair);
			if(sair == 0) 
				break;

			dados = cli.mostraModelosMarca(leitura);
			
			var modeloEAnos = cli.mostraAnosDoModelo(leitura, dados);
			
			if (modeloEAnos.anos() == null || modeloEAnos.anos().isEmpty()) {
				System.out.println("Nenhum modelo encontrado para este ano \n");
				break;
			}
			cli.mostraEdicoesEncontradas(modeloEAnos.anos(), modeloEAnos.modelo());
		}
	}
}