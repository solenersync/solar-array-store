package com.solenersync.solararraystore.controller;

import com.solenersync.solararraystore.model.SolarArray;
import com.solenersync.solararraystore.model.SolarArrayRequest;
import com.solenersync.solararraystore.service.SolarArrayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("api/v1/solar-arrays")
@RestController
public class SolarArrayController {

    private final SolarArrayService solarArrayService;

    public SolarArrayController(SolarArrayService solarArrayService) { this.solarArrayService = solarArrayService; }

    @GetMapping("/array/{id}")
    public Optional<SolarArray> getSolarArray(@PathVariable Integer id) {
        log.debug("Retrieving solar array id: {} ",id);
        System.out.println("Retrieving solar array id: " + id);
        return solarArrayService.findById(id);
    }

    @PostMapping("/create")
    public Integer createSolarArray(@RequestBody SolarArrayRequest request) {
        SolarArray solarArray = solarArrayService.create(request);
        log.debug("Create solar array id: {}", solarArray.getSolar_array_id());
        System.out.println("Create solar array id: " + solarArray.getSolar_array_id());
        return solarArray.getSolar_array_id();
    }

    @GetMapping("/array/user/{id}")
    public List<SolarArray> getSolarArrayList(@PathVariable Integer id) {
        log.debug("Retrieving solar array for user id: {} ",id);
        System.out.println("Retrieving solar array for user id: " + id);
        return solarArrayService.findByUserId(id);
    }

    @DeleteMapping("/array/{id}")
    public void deleteSolarArray(@PathVariable Integer id) {
        log.debug("Deleting solar array id: {} ",id);
        System.out.println("Deleting solar array id: " + id);
        solarArrayService.deleteById(id);
    }

}
