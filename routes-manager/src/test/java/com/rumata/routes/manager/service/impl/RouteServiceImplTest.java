package com.rumata.routes.manager.service.impl;

import com.rumata.routes.manager.MariaDBContainerInitializer;
import com.rumata.routes.manager.domain.entity.Route;
import com.rumata.routes.manager.domain.entity.Station;
import com.rumata.routes.manager.domain.repository.RouteRepository;
import com.rumata.routes.manager.domain.repository.StationRepository;
import com.rumata.routes.manager.exception.IllegalInputException;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = { MariaDBContainerInitializer.class })
public class RouteServiceImplTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private RouteRepository routeRepository;

    private RouteServiceImpl routeService;


    @BeforeEach
    public void init() {
        routeRepository.deleteAll();
        stationRepository.deleteAll();
        routeService = new RouteServiceImpl(stationRepository, routeRepository);
    }

    @Test
    public void testAddRoute() {
        val station1 = "name1";
        val station2 = "name2";
        val cost = BigDecimal.valueOf(42);
        stationRepository.save(new Station(station1));
        stationRepository.save(new Station(station2));

        routeService.addRoute(station1, station2, cost);
        assertEquals(Collections.singletonMap(station2, cost), routeService.stationRoutes(station1));
        assertEquals(Collections.singletonMap(station1, cost), routeService.stationRoutes(station2));
    }

    @Test
    public void testAddRouteBetweenMissingStations() {
        assertThrows(IllegalInputException.class, () -> {
            routeService.addRoute("one", "two", BigDecimal.ZERO);
        });
    }
}
