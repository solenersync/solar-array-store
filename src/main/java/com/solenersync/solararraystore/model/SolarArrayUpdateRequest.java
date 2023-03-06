package com.solenersync.solararraystore.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolarArrayUpdateRequest {

    private Integer solar_array_id;
    private float lat;
    private float lon;
    private float peak_power;
    private float loss;
    private float angle;
    private float aspect;
    private String mounting;

}