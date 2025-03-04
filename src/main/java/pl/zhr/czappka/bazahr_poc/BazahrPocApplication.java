package pl.zhr.czappka.bazahr_poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
public class BazahrPocApplication {

	public static void main(final String[] args) {
		SpringApplication.run(BazahrPocApplication.class, args);
	}

}


