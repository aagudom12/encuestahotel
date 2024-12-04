package com.alfredo.encuestahotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EncuestahotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(EncuestahotelApplication.class, args);

		// Generar el hash de la contraseña
		//PasswordEncoder encoder = new BCryptPasswordEncoder();
		//String password = "password123";  // La contraseña que deseas cifrar
		//String encodedPassword = encoder.encode(password);  // Cifra la contraseña
		//System.out.println("Encoded password: " + encodedPassword);  // Imprime el hash en la consola
	}

}
