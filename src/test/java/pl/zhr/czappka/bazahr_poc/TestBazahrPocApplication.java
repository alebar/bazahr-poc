package pl.zhr.czappka.bazahr_poc;

import org.springframework.boot.SpringApplication;

public class TestBazahrPocApplication {

	public static void main(String[] args) {
		SpringApplication.from(BazahrPocApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
