package pl.zhr.czappka.bazahr_poc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;

import pl.zhr.czappka.bazahr_poc.memberships.MembershipsCollectionDto;
import pl.zhr.czappka.bazahr_poc.units.UnitDto;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BazahrPocApplicationTests {

	@Autowired
	TestRestTemplate testRestTemplate;

	@Test
	void handleIncomingEvent() throws InterruptedException {
		assertThat(getMembershipsCount()).isEqualTo(0);
		assertThat(getUnitNumerosity()).isEqualTo(0);

		// body and type of the message is hardcoded in controller
		testRestTemplate.postForEntity("/v1/messages/", null, null);

		Thread.sleep(5000);

		assertThat(getMembershipsCount()).isEqualTo(2);
		assertThat(getUnitNumerosity()).isEqualTo(2);
	}

	private long getMembershipsCount() {
		return testRestTemplate.
				getForEntity("/v1/memberships", MembershipsCollectionDto.class).
				getBody().
				getCount();
	}

	private long getUnitNumerosity() {
		var unitDto = testRestTemplate.getForEntity(
				"/v1/units/x",UnitDto.class);
		return unitDto.getBody().getNumerosity();
	}

}
