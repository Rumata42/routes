package com.rumata.routes.manager.domain.repository;

import com.rumata.routes.manager.exception.IllegalInputException;
import com.rumata.routes.manager.domain.entity.Station;
import lombok.val;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long>, JpaSpecificationExecutor<Station> {

    long countByName(String name);

    long deleteByName(String name);

    Optional<Station> findByName(String name);

    default Station requiredByName(String name) {
        val station = findByName(name);
        if (station.isEmpty()) {
            throw new IllegalInputException("Station '" + name + "' doesn't exist.");
        }
        return station.get();
    }

}