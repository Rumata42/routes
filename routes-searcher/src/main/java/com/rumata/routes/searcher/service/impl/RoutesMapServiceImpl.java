package com.rumata.routes.searcher.service.impl;

import com.rumata.routes.searcher.domain.entity.Route;
import com.rumata.routes.searcher.domain.repository.RouteRepository;
import com.rumata.routes.searcher.service.interfaces.RoutesMap;
import com.rumata.routes.searcher.service.interfaces.RoutesMapService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@Service
public class RoutesMapServiceImpl implements RoutesMapService {

    private final RouteRepository routeRepository;


    @Override
    @Transactional(readOnly = true)
    public RoutesMap routesMap() {
        //TODO cache database result and observe changes instead of loading each time
        return new RoutesMapImpl(routeRepository.allRoutes());
    }


    @AllArgsConstructor
    private static final class RoutesMapImpl implements RoutesMap {

        private final List<Route> routes;

        @Override
        public Map<String, BigDecimal> routesForStation(String station) {
            val res = new HashMap<String, BigDecimal>();
            boolean exists = false;
            for (Route route: routes) {
                if (station.equals(route.getStation1())) {
                    exists = true;
                    if (route.getStation2() != null) {
                        res.put(route.getStation2(), route.getCost());
                    }
                }
                if (station.equals(route.getStation2())) {
                    exists = true;
                    res.put(route.getStation1(), route.getCost());
                }
            }

            if (!exists) {
                throw new IllegalArgumentException("Station '" + station + "' does not exist");
            }
            return res;
        }
    }


}
