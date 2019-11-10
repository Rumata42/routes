package com.rumata.routes.searcher.service.impl;

import com.rumata.routes.searcher.domain.entity.Route;
import com.rumata.routes.searcher.service.interfaces.RoutesMapService;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

class RoutesMapServiceImplTest {

    private static final String STATION = "The name of station";


    @Test
    void testSingleStation() {
        val service = serviceWithMockData(new Route(STATION));
        val routes = service.routesMap().routesForStation(STATION);
        Assertions.assertTrue(routes.isEmpty());
    }

    @Test
    void testUnknownStation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            val service = serviceWithMockData(new Route(STATION));
            service.routesMap().routesForStation("unknown station");
        });
    }

    @Test
    void testOutgoingRoute() {
        val destination = "One another station";
        val cost = BigDecimal.valueOf(1.0);
        val service = serviceWithMockData(new Route(STATION, destination, cost));
        val routes = service.routesMap().routesForStation(STATION);
        Assertions.assertEquals(1, routes.size());
        Assertions.assertEquals(cost, routes.get(destination));
    }

    @Test
    void testIncomingRoute() {
        val destination = "One another station";
        val cost = BigDecimal.valueOf(1.0);
        val service = serviceWithMockData(new Route(destination, STATION, cost));
        val routes = service.routesMap().routesForStation(STATION);
        Assertions.assertEquals(1, routes.size());
        Assertions.assertEquals(cost, routes.get(destination));
    }

    /*-------------------------PRIVATE-----------------------*/

    private RoutesMapService serviceWithMockData(Route...routes) {
        return new RoutesMapServiceImpl(() -> Arrays.asList(routes));
    }
}
