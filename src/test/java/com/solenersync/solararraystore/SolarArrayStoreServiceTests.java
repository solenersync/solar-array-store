package com.solenersync.solararraystore;

import com.solenersync.solararraystore.model.Mounting;
import com.solenersync.solararraystore.model.SolarArray;
import com.solenersync.solararraystore.model.SolarArrayRequest;
import com.solenersync.solararraystore.model.SolarArrayUpdateRequest;
import com.solenersync.solararraystore.repository.SolarArrayRepository;
import com.solenersync.solararraystore.service.SolarArrayService;
import net.joshka.junit.json.params.JsonFileSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.json.JsonObject;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatRuntimeException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith({MockitoExtension.class})
public class SolarArrayStoreServiceTests {

    private SolarArrayService solarArrayService;
    private SolarArrayRequest solarArrayRequest;
    private SolarArrayUpdateRequest solarArrayUpdateRequest;
    private SolarArray solarArray;
    private LocalDateTime date;

    @Mock
    private SolarArrayRepository solarArrayRepository;

    @BeforeEach
    void setUp() {
        solarArrayService = new SolarArrayService(solarArrayRepository);
        solarArray = SolarArray.builder()
            .solarArrayId(23001)
            .userId(10001)
            .angle(35f)
            .aspect(2f)
            .lat(52.1234f)
            .lon(8.1234f)
            .loss(0.14f)
            .mounting(Mounting.FREE)
            .peakPower(6f)
            .created_date(date)
            .build();

        solarArrayRequest = SolarArrayRequest.builder()
            .userId(10001)
            .angle(35f)
            .aspect(2f)
            .lat(52.1234f)
            .lon(8.1234f)
            .loss(0.14f)
            .mounting("Roof Mounted")
            .peakPower(6f)
            .build();

        solarArrayUpdateRequest = SolarArrayUpdateRequest.builder()
            .solarArrayId(23001)
            .angle(35f)
            .aspect(2f)
            .lat(52.1234f)
            .lon(8.1234f)
            .loss(0.14f)
            .mounting("Roof Mounted")
            .peakPower(6f)
            .build();
    }

    @Test
    public void should_return_solar_array_when_found_by_id() {
        Integer id = 23001;
        when(solarArrayRepository.findById(id)).thenReturn(Optional.of(solarArray));
        Optional<SolarArray> result = solarArrayService.findById(id);
        assertThat(result).isEqualTo(Optional.of(solarArray));
    }

    @Test
    void should_return_empty_when_solar_array_not_found_by_id() throws Exception {
        Integer id = 23002;
        when(solarArrayRepository.findById(id)).thenReturn(Optional.empty());
        Optional<SolarArray> result = solarArrayService.findById(id);
        assertThat(result).isEqualTo(Optional.empty());
    }

    @ParameterizedTest
    @JsonFileSource(resources = "/create-solar-array.json")
    void should_return_solar_array_when_new_array_created(JsonObject json) throws Exception {
        when(solarArrayRepository.save(any(SolarArray.class))).thenReturn(solarArray);
        Optional<SolarArray> result = solarArrayService.create(solarArrayRequest);
        assertThat(result).isEqualTo(Optional.of(solarArray));
    }

    @ParameterizedTest
    @JsonFileSource(resources = "/create-solar-array.json")
    public void should_return_empty_when_error_creating_solar_array(JsonObject json) throws Exception {
        when(solarArrayRepository.save(any(SolarArray.class))).thenReturn(null);
        Optional<SolarArray> result = solarArrayService.create(solarArrayRequest);
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    void should_return_new_solar_array_when_solar_array_is_updated() throws Exception {
        when(solarArrayRepository.findById(23001)).thenReturn(Optional.of(solarArray));
        when(solarArrayRepository.save(any(SolarArray.class))).thenReturn(solarArray);
        Optional<SolarArray> result = solarArrayService.update(solarArrayUpdateRequest);
        assertThat(result).isEqualTo(Optional.of(solarArray));
    }

    @Test
    public void should_return_empty_when_error_updating_solar_array() throws Exception {
        when(solarArrayRepository.save(any(SolarArray.class))).thenReturn(null);
        Optional<SolarArray> result = solarArrayService.create(solarArrayRequest);
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    void should_return_solar_array_when_found_by_userid() throws Exception {
        Integer id = 10001;
        when(solarArrayRepository.findByUserId(id)).thenReturn(Optional.of(solarArray));
        Optional<SolarArray> result = solarArrayService.findByUserId(id);
        assertThat(result).isEqualTo(Optional.of(solarArray));
    }

    @Test
    void should_return_empty_when_solar_array_not_found_by_userid() throws Exception {
        Integer id = 10001;
        when(solarArrayRepository.findByUserId(id)).thenReturn(Optional.empty());
        Optional<SolarArray> result = solarArrayService.findByUserId(id);
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void should_not_throw_error_when_solar_array_is_deleted() throws Exception {
        Integer id = 23001;
        doNothing().when(solarArrayRepository).deleteById(id);
        solarArrayService.deleteById(id);
        verify(solarArrayRepository, times(1)).deleteById(id);
    }

    @Test
    public void should_return_runtime_exception_when_error_deleting_solar_array() throws Exception {
        Integer id = 23001;
        doThrow(new RuntimeException()).when(solarArrayRepository).deleteById(id);
        try {
            solarArrayService.deleteById(id);
        } catch (Exception e) {
            assertThat(assertThatRuntimeException());
        }
        verify(solarArrayRepository, times(1)).deleteById(id);
    }

}
