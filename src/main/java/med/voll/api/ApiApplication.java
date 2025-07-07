package med.voll.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
	//@Override
	public void run(String... args) throws Exception {
		System.out.println("welcome app with spring boot 3, aplicar mejores practicas y proteger API");
	}
}
