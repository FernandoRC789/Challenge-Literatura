package com.alura.literatura;

import com.alura.literatura.principal.LogicaUsuario;
import com.alura.literatura.service.LibroService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LiteraturaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiteraturaApplication.class, args);

	}
	// Bean que ejecuta la lógica de usuario después de iniciar Spring
	@Bean
	public CommandLineRunner run(LogicaUsuario logicaUsuario) {
		return args -> logicaUsuario.menu();
	}

}
