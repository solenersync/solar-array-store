package com.solenersync.solararraystore.controller;

import com.solenersync.solararraystore.model.Mounting;
import com.solenersync.solararraystore.model.SolarArray;
import com.solenersync.solararraystore.service.SolarArrayService;
import net.joshka.junit.json.params.JsonFileSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.json.JsonObject;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class})
class SolarArrayStoreControllerTests {

	@Mock
	private SolarArrayService solarArrayService;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new SolarArrayController(solarArrayService)).build();
	}

	@ParameterizedTest
	@JsonFileSource(resources = "/get-solar-array.json")
	public void shouldReturnSolarArrayFromId(JsonObject json) throws Exception {

		LocalDateTime date = LocalDateTime.of(2022, 8, 12, 19, 30, 30);
		SolarArray solarArray = SolarArray.builder()
			.solar_array_id(23001)
			.user_id(10001)
			.angle(35f)
			.aspect(2f)
			.lat(52.1234f)
			.lon(8.1234f)
			.loss(0.14f)
			.mounting(Mounting.FREE)
			.peakPower(6f)
			.created_date(date)
			.build();
		when(solarArrayService.findById(23001)).thenReturn(Optional.of(solarArray));
		mockMvc.perform(get("/api/v1/solar-arrays/array/23001"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().json(json.toString()));
	}

}
