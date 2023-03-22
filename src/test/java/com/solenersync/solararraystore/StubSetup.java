package com.solenersync.solararraystore;

import au.com.dius.pact.provider.junitsupport.State;
import com.solenersync.solararraystore.model.Mounting;
import com.solenersync.solararraystore.model.SolarArray;
import com.solenersync.solararraystore.repository.SolarArrayRepository;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@UtilityClass
public class StubSetup {

    SolarArray solarArray = SolarArray.builder()
        .solarArrayId(1)
        .userId(1)
        .lat(52.207306f)
        .lon(-6.52026f)
        .peakPower(8.2f)
        .loss(0.145f)
        .angle(35.0f)
        .aspect(2.0f)
        .mounting(Mounting.FREE)
        .build();


    public void stubForGetSolarArray(SolarArrayRepository solarArrayRepository) {
        when(solarArrayRepository.findByUserId(anyInt())).thenAnswer(invocation -> Optional.of(solarArray));
    }

    public void stubForCreateSolarArray(SolarArrayRepository solarArrayRepository) {
        when(solarArrayRepository.save(any())).thenAnswer(invocation -> solarArray);
    }

    public void stubForUpdateSolarArray(SolarArrayRepository solarArrayRepository) {
        when(solarArrayRepository.findById(1)).thenReturn(Optional.of(solarArray));
        when(solarArrayRepository.save(any())).thenAnswer(invocation -> solarArray);
    }
}
