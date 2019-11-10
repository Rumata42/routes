package com.rumata.routes.manager.service.interfaces;

import java.math.BigDecimal;
import java.util.Map;

public interface RouteService {

    Map<String, BigDecimal> stationRoutes(String stationName);

    void addRoute(String station1Name, String station2Name, BigDecimal cost);

    void changeRouteCost(String station1Name, String station2Name, BigDecimal cost);

    void removeRoute(String station1Name, String station2Name);

}