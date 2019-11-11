package com.rumata.routes.manager.service.impl;

import com.rumata.routes.manager.domain.entity.Station;
import com.rumata.routes.manager.domain.repository.StationRepository;
import com.rumata.routes.manager.exception.IllegalInputException;
import com.rumata.routes.manager.service.interfaces.StationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

//TODO messages through bundles
@Slf4j
@AllArgsConstructor
@Service
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;


    @Override
    @Transactional(readOnly = true)
    public Set<String> allStations() {
        return stationRepository.findAll().stream()
                .map(Station::getName)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void addStation(String name) {
        if (stationRepository.countByName(name) > 0) {
            throw new IllegalInputException("Station '" + name + "' already exists.");
        }

        stationRepository.save(new Station(name));
        log.debug("New station '{}' has been saved.", name);
    }

    @Override
    @Transactional
    public void renameStation(String oldName, String newName) {
        val station = stationRepository.requiredByName(oldName);
        if (stationRepository.countByName(newName) > 0) {
            throw new IllegalInputException("Station '" + newName + "' already exists.");
        }
        station.setName(newName);
        stationRepository.save(station);
        log.debug("Station '{}' has been renamed to '{}'.", oldName, newName);
    }

    @Override
    @Transactional
    public void removeStation(String name) {
        long deleted = stationRepository.deleteByName(name);
        if (deleted < 1) {
            throw new IllegalInputException("Station '" + name + "' doesn't exist.");
        }
        log.debug("Station '{}' has been deleted.", name);
    }
}
