package br.com.desafio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import br.com.desafio.principal.Principal;

@SpringBootApplication
@EnableFeignClients
public class DemoApplication implements CommandLineRunner {
	
	@Autowired
	Principal principal;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.menu();
	}
}