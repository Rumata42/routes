package com.rumata.routes.manager.service.impl;

import com.rumata.routes.manager.domain.entity.Route;
import com.rumata.routes.manager.domain.repository.RouteRepository;
import com.rumata.routes.manager.domain.repository.StationRepository;
import com.rumata.routes.manager.exception.IllegalInputException;
import com.rumata.routes.manager.service.interfaces.RouteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class RouteServiceImpl implements RouteService {

    private final StationRepository stationRepository;
    private final RouteRepository routeRepository;


    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> stationRoutes(String stationName) {
        val station = stationRepository.requiredByName(stationName);
        return routeRepository.findAllByStation(station).stream()
                .collect(Collectors.toMap(
                        (Route route) -> station.equals(route.getStation1()) ? route.getStation2().getName() : route.getStation1().getName(),
                        Route::getCost
                ));
    }

    @Override
    @Transactional
    public void addRoute(String station1Name, String station2Name, BigDecimal cost) {
        if (station1Name.equals(station2Name)) {
            throw new IllegalInputException("Stations name must be different.");
        }
        val station1 = stationRepository.requiredByName(station1Name);
        val station2 = stationRepository.requiredByName(station2Name);
        if (routeRepository.countByStations(station1, station2) > 0) {
            throw new IllegalInputException("Routes between '" + station1Name + "' and '" + station2Name + "' already exists.");
        }
        routeRepository.save(new Route(station1, station2, cost));
        log.debug("New route between '{}' and '{}' with cost {} has been saved.", station1Name, station2, cost);
    }

    @Override
    @Transactional
    public void changeRouteCost(String station1Name, String station2Name, BigDecimal cost) {
        val station1 = stationRepository.requiredByName(station1Name);
        val station2 = stationRepository.requiredByName(station2Name);
        val route = routeRepository.requiredByStations(station1, station2);
        route.setCost(cost);
        routeRepository.save(route);
        log.debug("Route cost between '{}' and '{}' has been changed to {}.", station1Name, station2, cost);
    }

    @Override
    @Transactional
    public void removeRoute(String station1Name, String station2Name) {
        val station1 = stationRepository.requiredByName(station1Name);
        val station2 = stationRepository.requiredByName(station2Name);
        long deleted = routeRepository.deleteByStations(station1, station2);
        if (deleted < 1) {
            throw new IllegalInputException("Routes between '" + station1.getName() + "' and '" + station2.getName() + "' doesn't exist.");
        }
        log.debug("Route between '{}' and '{}' has been deleted.", station1Name, station2);
    }
}
