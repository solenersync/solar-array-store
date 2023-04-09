package com.solenersync.solararraystore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solenersync.solararraystore.controller.SolarArrayController;
import com.solenersync.solararraystore.model.Mounting;
import com.solenersync.solararraystore.model.SolarArray;
import com.solenersync.solararraystore.model.SolarArrayRequest;
import com.solenersync.solararraystore.model.SolarArrayUpdateRequest;
import com.solenersync.solararraystore.service.SolarArrayService;
import net.joshka.junit.json.params.JsonFileSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import javax.json.JsonObject;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class})
class SolarArrayStoreControllerUnitTests {

	@Mock
	private SolarArrayService solarArrayService;

	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private SolarArray solarArray;
	private LocalDateTime date;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new SolarArrayController(solarArrayService)).build();
		date = LocalDateTime.of(2022, 8, 12, 19, 30, 30);
		solarArray = SolarArray.builder()
			.solarArrayId(23001)
			.userId(10001)
			.angle(35f)
			.aspect(2f)
			.lat(52.1234f)
			.lon(8.1234f)
			.loss(0.14f)
			.mounting(Mounting.FREE)
			.created_date(date)
			.build();
	}

	@ParameterizedTest
	@JsonFileSource(resources = "/get-solar-array.json")
	void should_return_solar_array_when_found_by_id(JsonObject json) throws Exception {
		when(solarArrayService.findById(23001)).thenReturn(Optional.of(solarArray));
		mockMvc.perform(get("/api/v1/solar-arrays/array/23001"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().json(json.toString()));
	}

	@Test
	void should_return_404_when_solar_array_not_found_by_id() throws Exception {
		when(solarArrayService.findById(23002)).thenReturn(Optional.empty());
		mockMvc.perform(get("/api/v1/solar-arrays/array/23002"))
			.andExpect(status().isNotFound());
	}

	@ParameterizedTest
	@JsonFileSource(resources = "/create-solar-array.json")
	void should_return_ok_when_solar_array_is_created(JsonObject json) throws Exception {
		when(solarArrayService.create(any(SolarArrayRequest.class))).thenReturn(Optional.of(new SolarArray()));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/solar-arrays/create")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json.toString()))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@ParameterizedTest
	@JsonFileSource(resources = "/create-solar-array.json")
	void should_return_internal_server_error_when_error_creating_solar_array(JsonObject json) throws Exception {
		doAnswer(invocation -> {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating solar array");
		}).when(solarArrayService).create(any(SolarArrayRequest.class));
		mockMvc.perform(post("/api/v1/solar-arrays/create")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json.toString()))
			.andExpect(status().isInternalServerError());
		verify(solarArrayService, times(1)).create(any(SolarArrayRequest.class));
	}

	@ParameterizedTest
	@JsonFileSource(resources = "/update-solar-array.json")
	void should_return_ok_when_solar_array_is_updated(JsonObject json) throws Exception {
		when(solarArrayService.update(any(SolarArrayUpdateRequest.class))).thenReturn(Optional.of(new SolarArray()));
		mockMvc.perform(post("/api/v1/solar-arrays/update")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json.toString()))
			.andExpect(status().isOk());
		verify(solarArrayService).update(any(SolarArrayUpdateRequest.class));
	}

	@ParameterizedTest
	@JsonFileSource(resources = "/update-solar-array.json")
	void should_return_internal_server_error_when_error_updating_solar_array(JsonObject json) throws Exception {
		doAnswer(invocation -> {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating solar array");
		}).when(solarArrayService).update(any(SolarArrayUpdateRequest.class));
		mockMvc.perform(post("/api/v1/solar-arrays/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.toString()))
			.andExpect(status().isInternalServerError());
		verify(solarArrayService, times(1)).update(any(SolarArrayUpdateRequest.class));
	}

	@ParameterizedTest
	@JsonFileSource(resources = "/get-solar-array.json")
	void should_return_solar_array_when_found_by_userid(JsonObject json) throws Exception {
		when(solarArrayService.findByUserId(10001)).thenReturn(Optional.of(solarArray));
		mockMvc.perform(get("/api/v1/solar-arrays/array/user/10001"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().json(json.toString()));
	}

	@Test
	void should_return_not_found_when_solar_array_not_found_by_userid() throws Exception {
		when(solarArrayService.findByUserId(10002)).thenReturn(Optional.empty());
		mockMvc.perform(get("/api/v1/solar-arrays/array/user/10002"))
			.andExpect(status().isNotFound());
	}

	@Test
	void should_return_ok_when_solar_array_is_deleted() throws Exception {
		Integer id = 23001;
		doNothing().when(solarArrayService).deleteById(id);
		mockMvc.perform(delete("/api/v1/solar-arrays/array/23001"))
			.andExpect(status().isOk());
		verify(solarArrayService, times(1)).deleteById(id);
	}

	@Test
	void should_return_internal_server_error_when_error_deleting_solar_array() throws Exception {
		Integer id = 23001;
		doThrow(new RuntimeException()).when(solarArrayService).deleteById(id);
		mockMvc.perform(delete("/api/v1/solar-arrays/array/23001"))
			.andExpect(status().isInternalServerError());
		verify(solarArrayService, times(1)).deleteById(id);
	}
}
