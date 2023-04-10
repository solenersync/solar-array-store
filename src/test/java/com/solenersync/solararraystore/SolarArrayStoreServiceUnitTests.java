package com.solenersync.solararraystore;

import com.solenersync.solararraystore.model.Mounting;
import com.solenersync.solararraystore.model.SolarArray;
import com.solenersync.solararraystore.model.SolarArrayRequest;
import com.solenersync.solararraystore.model.SolarArrayUpdateRequest;
import com.solenersync.solararraystore.repository.SolarArrayRepository;
import com.solenersync.solararraystore.service.SolarArrayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith({MockitoExtension.class})
class SolarArrayStoreServiceUnitTests {

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
    void should_return_solar_array_when_found_by_id() {
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
        assertThat(result).isEmpty();
    }

    @Test
    void should_return_solar_array_when_new_array_created() throws Exception {
        when(solarArrayRepository.save(any(SolarArray.class))).thenReturn(solarArray);
        Optional<SolarArray> result = solarArrayService.create(solarArrayRequest);
        assertThat(result).isEqualTo(Optional.of(solarArray));
    }

    @Test
    void should_return_empty_when_error_creating_solar_array() throws Exception {
        when(solarArrayRepository.save(any(SolarArray.class))).thenReturn(null);
        Optional<SolarArray> result = solarArrayService.create(solarArrayRequest);
        assertThat(result).isEmpty();
    }

    @Test
    void should_return_new_solar_array_when_solar_array_is_updated() throws Exception {
        when(solarArrayRepository.findById(23001)).thenReturn(Optional.of(solarArray));
        when(solarArrayRepository.save(any(SolarArray.class))).thenReturn(solarArray);
        Optional<SolarArray> result = solarArrayService.update(solarArrayUpdateRequest);
        assertThat(result).isEqualTo(Optional.of(solarArray));
    }

    @Test
    void should_return_empty_when_error_updating_solar_array() throws Exception {
        when(solarArrayRepository.save(any(SolarArray.class))).thenReturn(null);
        Optional<SolarArray> result = solarArrayService.create(solarArrayRequest);
        assertThat(result).isEmpty();
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
        assertThat(result).isEmpty();
    }

    @Test
    void should_not_throw_error_when_solar_array_is_deleted() throws Exception {
        Integer id = 23001;
        doNothing().when(solarArrayRepository).deleteById(id);
        solarArrayService.deleteById(id);
        verify(solarArrayRepository, times(1)).deleteById(id);
    }

    @Test
    void should_return_runtime_exception_when_error_deleting_solar_array() throws Exception {
        Integer id = 23001;
        doThrow(new RuntimeException()).when(solarArrayRepository).deleteById(id);
        assertThrows(RuntimeException.class, () -> solarArrayService.deleteById(id));
        verify(solarArrayRepository, times(1)).deleteById(id);
    }

    @Test
    void should_not_create_ad_with_invalid_lat() {
        solarArrayRequest.setLat(-1000);
        assertThat(solarArrayService.create(solarArrayRequest).isPresent()).isFalse();
    }

    @Test
    void should_not_create_ad_with_invalid_lon() {
        solarArrayRequest.setLon(-1000);
        assertThat(solarArrayService.create(solarArrayRequest).isPresent()).isFalse();
    }

    @Test
    void should_not_create_ad_with_invalid_angle() {
        solarArrayRequest.setAngle(-1000);
        assertThat(solarArrayService.create(solarArrayRequest).isPresent()).isFalse();
    }

    @Test
    void should_not_create_ad_with_invalid_loss() {
        solarArrayRequest.setLoss(-1000);
        assertThat(solarArrayService.create(solarArrayRequest).isPresent()).isFalse();
    }

    @Test
    void should_validate_lat_is_between_minus_90_and_plus_90() {
        assertThat(solarArrayService.isValidLat(-90.0)).isTrue();
        assertThat(solarArrayService.isValidLat(90.0)).isTrue();
        assertThat(solarArrayService.isValidLat(-90.1)).isFalse();
        assertThat(solarArrayService.isValidLat(90.1)).isFalse();
    }

    @Test
    void should_validate_lon_is_between_minus_180_and_plus_180() {
        assertThat(solarArrayService.isValidLon(-180.0)).isTrue();
        assertThat(solarArrayService.isValidLon(180.0)).isTrue();
        assertThat(solarArrayService.isValidLon(-180.1)).isFalse();
        assertThat(solarArrayService.isValidLon(180.1)).isFalse();
    }

    @Test
    void should_validate_loss_is_between_0_and_100() {
        assertThat(solarArrayService.isValidLoss(0)).isTrue();
        assertThat(solarArrayService.isValidLoss(100.0)).isTrue();
        assertThat(solarArrayService.isValidLoss(-0.1)).isFalse();
        assertThat(solarArrayService.isValidLoss(100.1)).isFalse();
    }

    @Test
    void should_validate_angle_is_between_minus_180_and_plus_180() {
        assertThat(solarArrayService.isValidAngle(-180.0)).isTrue();
        assertThat(solarArrayService.isValidAngle(180.0)).isTrue();
        assertThat(solarArrayService.isValidAngle(-180.1)).isFalse();
        assertThat(solarArrayService.isValidAngle(180.1)).isFalse();
    }
}
