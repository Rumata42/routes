package com.rumata.routes.searcher.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class Route {

    @Id
    private Long id;
    private String station1;
    private String station2;
    private BigDecimal cost;

    public Route(String station1) {
        this.station1 = station1;
    }

    public Route(String station1, String station2, BigDecimal cost) {
        this.station1 = station1;
        this.station2 = station2;
        this.cost = cost;
    }

    public Route(String station1, String station2, Double cost) {
        this(station1, station2, BigDecimal.valueOf(cost));
    }
}
