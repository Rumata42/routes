package com.rumata.routes.searcher.controller;

import com.rumata.routes.searcher.domain.repository.StationRepository;
import com.rumata.routes.searcher.service.interfaces.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RestController
public class RoutesSearcherController {

    private final SearchService searchService;
    private final StationRepository stationRepository;


    @GetMapping(path = "/routes/calculate_cost")
    public BigDecimal calculateCost(
            @Valid @RequestParam @NonNull String station1,
            @Valid @RequestParam @NonNull String station2
    ) {
        return searchService.calculateCost(station1, station2);
    }

    @GetMapping(path = "/station/all")
    public List<String> allStations() {
        return stationRepository.allRoutes();
    }

}
