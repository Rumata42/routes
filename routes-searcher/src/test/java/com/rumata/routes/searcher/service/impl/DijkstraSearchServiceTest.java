package com.rumata.routes.searcher.service.impl;

import com.rumata.routes.searcher.domain.entity.Route;
import com.rumata.routes.searcher.service.interfaces.RoutesMap;
import com.rumata.routes.searcher.service.interfaces.RoutesMapService;
import com.rumata.routes.searcher.service.interfaces.SearchService;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;

class DijkstraSearchServiceTest {


    @Test
    void testSingleStation() {
        val routesMapService = new MockRoutesMapService();
        routesMapService.add("one", emptyMap());

        SearchService service = new DijkstraSearchService(routesMapService);
        Assertions.assertEquals(BigDecimal.ZERO, service.calculateCost("one", "one"));
    }

    @Test
    void testNoStation() {
        val routesMapService = new MockRoutesMapService();
        routesMapService.add("one", emptyMap());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            SearchService service = new DijkstraSearchService(routesMapService);
            Assertions.assertEquals(BigDecimal.ZERO, service.calculateCost("one", "two"));
        });
    }

    @Test
    void testTwoStations() {
        val cost = BigDecimal.valueOf(1.0);
        val routesMapService = new MockRoutesMapService();
        routesMapService.add("one", Collections.singletonMap("two", cost));
        routesMapService.add("two", Collections.singletonMap("one", cost));

        SearchService service = new DijkstraSearchService(routesMapService);
        Assertions.assertEquals(cost, service.calculateCost("one", "two"));
    }

    @Test
    void testTwoIsolatedStations() {
        val routesMapService = new MockRoutesMapService();
        routesMapService.add("one", emptyMap());
        routesMapService.add("two", emptyMap());

        SearchService service = new DijkstraSearchService(routesMapService);
        Assertions.assertEquals(BigDecimal.valueOf(-1), service.calculateCost("one", "two"));
    }

    @Test
    void testSearchByComplexMap() {
        val routesMapService = new RoutesMapServiceImpl(() -> Arrays.asList(
                new Route("A", "C", 5.0),
                new Route("B", "C", 2.0),
                new Route("B", "D", 5.0),
                new Route("D", "G", 6.0),
                new Route("C", "E", 2.0),
                new Route("C", "F", 3.0),
                new Route("E", "G", 5.0),
                new Route("F", "G", 4.0),
                new Route("H")
        ));
        SearchService service = new DijkstraSearchService(routesMapService);

        Assertions.assertEquals(BigDecimal.valueOf(-1), service.calculateCost("H", "G"));
        Assertions.assertEquals(BigDecimal.valueOf(12.0), service.calculateCost("A", "G"));
        Assertions.assertEquals(BigDecimal.valueOf(9.0), service.calculateCost("B", "G"));
        Assertions.assertEquals(BigDecimal.valueOf(5.0), service.calculateCost("E", "F"));
        Assertions.assertEquals(BigDecimal.valueOf(5.0), service.calculateCost("A", "C"));
        Assertions.assertEquals(BigDecimal.ZERO, service.calculateCost("D", "D"));
    }


    /*---------------------PRIVATE-----------------------*/

    private static final class MockRoutesMapService implements RoutesMapService {

        private Map<String, Map<String, BigDecimal>> routes = new HashMap<>();

        public void add(String station, Map<String, BigDecimal> routesForStation) {
            routes.put(station, routesForStation);
        }

        @Override
        public RoutesMap routesMap() {
            return name -> {
                val routesForStation = routes.get(name);
                if (routesForStation == null) throw new IllegalArgumentException();
                return routesForStation;
            };
        }
    }
}
