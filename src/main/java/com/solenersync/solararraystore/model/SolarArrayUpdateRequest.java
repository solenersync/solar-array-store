package com.solenersync.solararraystore.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolarArrayUpdateRequest {

    private Integer solarArrayId;
    private float lat;
    private float lon;
    private float peakPower;
    private float loss;
    private float angle;
    private float aspect;
    private String mounting;

}