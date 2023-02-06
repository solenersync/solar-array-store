package com.solenersync.solararraystore.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "solar_arrays")
public class SolarArray {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "solar-arrays_solar_array_id_seq", allocationSize = 1)
    @Column(name = "solar_array_id")
    public Integer solar_array_id;

    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "lat")
    private float lat;

    @Column(name = "lon")
    private float lon;

    @Column(name = "peakPower")
    private float peakPower;

    @Column(name = "loss")
    private float loss;

    @Column(name = "angle")
    private float angle;

    @Column(name = "aspect")
    private float aspect;

    @Column(name = "mounting")
    private Mounting mounting;

    @Column(name = "created_date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_date;

    @Override
    public String toString() {
        return "SolarArray{" +
            "solar_array_id=" + solar_array_id +
            ", lat=" + lat +
            ", lon=" + lon +
            ", peakPower=" + peakPower +
            ", loss=" + loss +
            ", angle=" + angle +
            ", aspect=" + aspect +
            ", mounting=" + mounting +
            ", created_date=" + created_date +
            '}';
    }
}
