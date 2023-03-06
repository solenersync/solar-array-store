package com.solenersync.solararraystore.controller;

import com.solenersync.solararraystore.model.SolarArray;
import com.solenersync.solararraystore.model.SolarArrayRequest;
import com.solenersync.solararraystore.model.SolarArrayUpdateRequest;
import com.solenersync.solararraystore.service.SolarArrayService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequestMapping("api/v1/solar-arrays")
@RestController
public class SolarArrayController {

    private final SolarArrayService solarArrayService;

    public SolarArrayController(SolarArrayService solarArrayService) { this.solarArrayService = solarArrayService; }

    @CrossOrigin
    @GetMapping("/array/{id}")
    public ResponseEntity<SolarArray> getSolarArray(@PathVariable Integer id) {
        log.info("Retrieving solar array id: {} ",id);
            return solarArrayService.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity createSolarArray(@Valid @RequestBody SolarArrayRequest request) {
        log.info("Creating solar array");
        return solarArrayService.create(request).map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @CrossOrigin
    @PostMapping("/update")
    public ResponseEntity updateSolarArray(@Valid @RequestBody SolarArrayUpdateRequest request) {
        log.info("Updating solar array");
        return solarArrayService.update(request).map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @CrossOrigin
    @GetMapping("/array/user/{id}")
    public ResponseEntity<SolarArray> getUserSolarArray(@PathVariable Integer id) {
        log.info("Retrieving solar array for user id: {} ",id);
        return solarArrayService.findByUserId(id).map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @DeleteMapping("/array/{id}")
    public ResponseEntity<Object> deleteSolarArray(@PathVariable Integer id) {
        log.info("Deleting solar array id: {} ",id);
        try {
            solarArrayService.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(null);
    }

}
