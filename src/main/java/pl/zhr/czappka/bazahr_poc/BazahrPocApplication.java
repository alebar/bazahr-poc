package pl.zhr.czappka.bazahr_poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BazahrPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(BazahrPocApplication.class, args);
	}

}


