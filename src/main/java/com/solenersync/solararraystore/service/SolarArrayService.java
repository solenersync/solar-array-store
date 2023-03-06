package com.solenersync.solararraystore.service;

import com.solenersync.solararraystore.model.Mounting;
import com.solenersync.solararraystore.model.SolarArray;
import com.solenersync.solararraystore.model.SolarArrayRequest;
import com.solenersync.solararraystore.model.SolarArrayUpdateRequest;
import com.solenersync.solararraystore.repository.SolarArrayRepository;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class SolarArrayService {
    private final SolarArrayRepository repository;

    public SolarArrayService(SolarArrayRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Optional<SolarArray> findById(Integer id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<SolarArray> findByUserId(Integer userId) {
        return repository.findByUserId(userId);
    };

    public Optional<SolarArray> create(SolarArrayRequest request) {
        Mounting mounting = request.getMounting().equals("Roof Mounted") ? Mounting.BUILDING : Mounting.FREE;
        SolarArray solarArray = new SolarArray();
        solarArray.setUser_id(request.getUser_id());
        solarArray.setAngle(request.getAngle());
        solarArray.setAspect(request.getAspect());
        solarArray.setLat(request.getLat());
        solarArray.setLon(request.getLon());
        solarArray.setLoss(request.getLoss());
        solarArray.setMounting(mounting);
        solarArray.setPeak_power(request.getPeak_power());
        solarArray.setCreated_date(LocalDateTime.now());
        SolarArray newSolarArray = repository.save(solarArray);
        return Optional.ofNullable(newSolarArray);
    }

    public Optional<SolarArray> update(SolarArrayUpdateRequest request) {
        Optional<SolarArray> optionalSolarArray = repository.findById(request.getSolar_array_id());
        if (optionalSolarArray.isPresent()) {
            Mounting mounting = request.getMounting().equals("Roof Mounted") ? Mounting.BUILDING : Mounting.FREE;
            SolarArray solarArray = optionalSolarArray.get();
            solarArray.setAngle(request.getAngle());
            solarArray.setAspect(request.getAspect());
            solarArray.setLat(request.getLat());
            solarArray.setLon(request.getLon());
            solarArray.setLoss(request.getLoss());
            solarArray.setMounting(mounting);
            solarArray.setPeak_power(request.getPeak_power());
            solarArray.setCreated_date(LocalDateTime.now());
            SolarArray newSolarArray = repository.save(solarArray);
            return Optional.ofNullable(newSolarArray);
        } else {
            return Optional.empty();
        }
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    };
}
