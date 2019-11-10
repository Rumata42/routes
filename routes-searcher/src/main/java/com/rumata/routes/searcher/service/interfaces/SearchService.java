package com.rumata.routes.searcher.service.interfaces;

import java.math.BigDecimal;

public interface SearchService {

    /**
     * Calculate cost between two stations
     * @param station1 - departure
     * @param station2 - destination
     * @return - sum of the shortest cost between two stations or -1 if there is no path between them
     * @throws IllegalArgumentException - if any of stations does not exist
     */
    BigDecimal calculateCost(String station1, String station2);

}
