package com.solenersync.solararraystore.service;

import com.solenersync.solararraystore.model.Mounting;
import com.solenersync.solararraystore.model.SolarArray;
import com.solenersync.solararraystore.model.SolarArrayRequest;
import com.solenersync.solararraystore.model.SolarArrayUpdateRequest;
import com.solenersync.solararraystore.repository.SolarArrayRepository;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
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
    }

    public Optional<SolarArray> create(SolarArrayRequest request) {

        if (!isValidLat(request.getLat()) ||
            !isValidLon(request.getLon()) ||
            !isValidLoss(request.getLoss()) ||
            !isValidAngle(request.getAngle()))
        {
            log.debug("invalid solar array params entered");
            return Optional.empty();
        }
        Mounting mounting = request.getMounting()
            .equals("Roof Mounted") ? Mounting.BUILDING : Mounting.FREE;
        SolarArray solarArray = new SolarArray();
        solarArray.setUserId(request.getUserId());
        solarArray.setAngle(request.getAngle());
        solarArray.setAspect(request.getAspect());
        solarArray.setLat(request.getLat());
        solarArray.setLon(request.getLon());
        solarArray.setLoss(request.getLoss());
        solarArray.setMounting(mounting);
        solarArray.setPeakPower(request.getPeakPower());
        solarArray.setCreated_date(LocalDateTime.now());
        SolarArray newSolarArray = repository.save(solarArray);
        return Optional.ofNullable(newSolarArray);
    }

    public Optional<SolarArray> update(SolarArrayUpdateRequest request) {
        Optional<SolarArray> optionalSolarArray = repository.findById(request.getSolarArrayId());
        if (optionalSolarArray.isPresent()) {
            Mounting mounting = request.getMounting().equals("Roof Mounted") ? Mounting.BUILDING : Mounting.FREE;
            SolarArray updateSolarArray = optionalSolarArray.get();
            updateSolarArray.setAngle(request.getAngle());
            updateSolarArray.setAspect(request.getAspect());
            updateSolarArray.setLat(request.getLat());
            updateSolarArray.setLon(request.getLon());
            updateSolarArray.setLoss(request.getLoss());
            updateSolarArray.setMounting(mounting);
            updateSolarArray.setPeakPower(request.getPeakPower());
            updateSolarArray.setCreated_date(LocalDateTime.now());
            SolarArray newSolarArray = repository.save(updateSolarArray);
            return Optional.ofNullable(newSolarArray);
        } else {
            return Optional.empty();
        }
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public boolean isValidLat(double lat) {
        return lat >= -90 && lat <= 90;
    }

    public boolean isValidLon(double lon) {
        return lon >= -180 && lon <= 180;
    }

    public boolean isValidLoss(double loss) {
        return loss >= 0 && loss <= 100;
    }

    public boolean isValidAngle(double angle) {
        return angle >= -180 && angle <= 180;
    }
}

