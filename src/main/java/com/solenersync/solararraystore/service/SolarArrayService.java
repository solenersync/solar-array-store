package com.solenersync.solararraystore.service;

import com.solenersync.solararraystore.model.SolarArray;
import com.solenersync.solararraystore.model.SolarArrayRequest;
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
    public Iterable findAll() {
        return repository.findAll();
    }

    public SolarArray create(SolarArrayRequest request) {
        SolarArray solarArray = new SolarArray();
        solarArray.setUser_id(request.getUser_id());
        solarArray.setAngle(request.getAngle());
        solarArray.setAspect(request.getAspect());
        solarArray.setLat(request.getLat());
        solarArray.setLon(request.getLon());
        solarArray.setLoss(request.getLoss());
        solarArray.setMounting(request.getMounting());
        solarArray.setPeakPower(request.getPeakPower());
        solarArray.setCreated_date(LocalDateTime.now());
        SolarArray newSolarArray = (SolarArray) repository.save(solarArray);
        return newSolarArray;
    }
}
