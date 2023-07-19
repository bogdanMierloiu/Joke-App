package accesa.eu.jokeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JokeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(JokeAppApplication.class, args);
	}

}
