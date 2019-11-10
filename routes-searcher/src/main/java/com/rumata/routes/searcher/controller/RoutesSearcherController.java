package com.rumata.routes.searcher.controller;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
public class RoutesSearcherController {


    @GetMapping(path = "/routes/calculate_cost")
    public BigDecimal calculateCost(
            @Valid @RequestParam @NonNull String station1,
            @Valid @RequestParam @NonNull String station2
    ) {
        //TODO
        return BigDecimal.valueOf(station1.length() + station2.length());
    }

}
