package com.solenersync.solararraystore.controller;

import com.solenersync.solararraystore.model.SolarArray;
import com.solenersync.solararraystore.model.SolarArrayRequest;
import com.solenersync.solararraystore.service.SolarArrayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RequestMapping("api/v1/solar-arrays")
@RestController
public class SolarArrayController {

    private final SolarArrayService solarArrayService;

    public SolarArrayController(SolarArrayService solarArrayService) { this.solarArrayService = solarArrayService; }

    @GetMapping("/solar-array/{id}")
    public Optional<SolarArray> getSolarArray(@PathVariable Integer id) {
        log.debug("Retrieving solar array {} ",id);
        System.out.println("Retrieving user " + id);
        return solarArrayService.findById(id);
    }

    @PostMapping("/create")
    public Integer createSolarArray(@RequestBody SolarArrayRequest request) {
        SolarArray solarArray = solarArrayService.create(request);
        log.debug("Created solar array {}");
        return solarArray.getSolar_array_id();
    }

}
