package com.solenersync.solararraystore.repository;

import com.solenersync.solararraystore.model.SolarArray;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public interface SolarArrayRepository extends CrudRepository <SolarArray, Integer>{

    Optional<SolarArray> findById(Integer id);

    List<SolarArray> findAll();

    SolarArray save(SolarArray solarArray);

    void deleteById(Integer id);

    @Query(
        value = "SELECT * FROM solar_arrays s WHERE s.user_id = ?1 ORDER BY s.solar_array_id",
        nativeQuery = true)
    Optional<SolarArray> findByUserId(Integer user_id);
}
