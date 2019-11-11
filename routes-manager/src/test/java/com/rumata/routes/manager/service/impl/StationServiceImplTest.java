package com.rumata.routes.manager.service.impl;

import com.rumata.routes.manager.MariaDBContainerInitializer;
import com.rumata.routes.manager.domain.entity.Route;
import com.rumata.routes.manager.domain.repository.RouteRepository;
import com.rumata.routes.manager.domain.repository.StationRepository;
import com.rumata.routes.manager.exception.IllegalInputException;
import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = { MariaDBContainerInitializer.class })
public class StationServiceImplTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private RouteRepository routeRepository;

    private StationServiceImpl stationService;


    @BeforeEach
    public void init() {
        routeRepository.deleteAll();
        stationRepository.deleteAll();
        stationService = new StationServiceImpl(stationRepository);
    }

    @Test
    public void testEmptyStationList() {
        assertTrue(stationService.allStations().isEmpty());
    }

    @Test
    public void testAddOneStation() {
        val name = "new station";
        stationService.addStation(name);
        assertEquals(Collections.singleton(name), stationService.allStations());
    }

    @Test
    public void testAddDupleOfStation() {
        assertThrows(IllegalInputException.class, () -> {
            val station = "new station";
            stationService.addStation(station);
            stationService.addStation(station);
        });
    }

    @Test
    public void testRenameStation() {
        val oldName = "old name";
        val newName = "new name";
        stationService.addStation(oldName);
        stationService.renameStation(oldName, newName);
        assertEquals(Collections.singleton(newName), stationService.allStations());
    }

    @Test
    public void testRenameMissingStation() {
        assertThrows(IllegalInputException.class, () -> {
            stationService.addStation("station");
            stationService.renameStation("missing", "new");
        });
    }

    @Test
    public void testRenameStationToExistingName() {
        assertThrows(IllegalInputException.class, () -> {
            stationService.addStation("station1");
            stationService.addStation("station2");
            stationService.renameStation("station1", "station2");
        });
    }

    @Test
    public void testRemoveStation() {
        val name = "any station";
        stationService.addStation(name);
        stationService.removeStation(name);
        assertTrue(stationService.allStations().isEmpty());
    }

    @Test
    public void testRemoveMissingStation() {
        assertThrows(IllegalInputException.class, () -> {
            stationService.removeStation("missing station");
            assertTrue(stationService.allStations().isEmpty());
        });
    }

    @Test
    public void testRemoveStationWithRoutes() {
        val stations1 = "name1";
        val stations2 = "name2";
        stationService.addStation(stations1);
        stationService.addStation(stations2);
        routeRepository.save(new Route(stationRepository.requiredByName(stations1), stationRepository.requiredByName(stations2), BigDecimal.ONE));
        stationService.removeStation(stations1);
        assertEquals(Collections.singleton(stations2), stationService.allStations());
    }

}
