package com.solenersync.solararraystore.controller;

import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/solar-arrays")
@RestController
public class SolarArrayController {

    @GetMapping("/test")
    public String test() {
        System.out.println("sending test response");
        return "Solar Testing 1...2...";
    }
}
