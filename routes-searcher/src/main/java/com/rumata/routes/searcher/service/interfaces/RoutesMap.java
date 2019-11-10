package com.rumata.routes.searcher.service.interfaces;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Map describes stations and routes between them
 */
public interface RoutesMap {

    /**
     * Show where can to go from this station and how much it will cost
     * @param station - departure station
     * @return - possible destination stations
     * @throws IllegalArgumentException - if station does not exist
     */
    Map<String, BigDecimal> routesForStation(String station);
}
