package com.solenersync.solararraystore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SolarArrayStoreControllerIT {

	@Autowired
	private TestRestTemplate template;

//	@Test
//	public void getSolarTest() throws Exception {
//		ResponseEntity<String> response = template.getForEntity("/api/v1/solar-arrays/solar-array/10001", String.class);
//		assertThat(response.getBody()).isEqualTo("Solar Testing 1...2...");
//	}
}
