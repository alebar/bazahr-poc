package pl.zhr.czappka.bazahr_poc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;

import pl.zhr.czappka.bazahr_poc.memberships.MembershipsCollectionDto;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BazahrPocApplicationTests {

	@Autowired
	TestRestTemplate testRestTemplate;

	@Test
	void contextLoads() {
		assertThat(getMembershipsCount()).isEqualTo(0);

		testRestTemplate.postForEntity("/v1/memberships/", null, null);

		assertThat(getMembershipsCount()).isEqualTo(1);
	}

	private long getMembershipsCount() {
		return testRestTemplate.getForEntity("/v1/memberships", MembershipsCollectionDto.class).getBody().getCount();
	}

}
