package com.rumata.routes.manager.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Entity
public class Route {

    public Route(Station station1, Station station2, BigDecimal cost) {
        this.cost = cost;
        this.station1 = station1;
        this.station2 = station2;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true)
    private Long id;

    @ManyToOne
    private Station station1;

    @ManyToOne
    private Station station2;

    @Setter
    @Column
    private BigDecimal cost;
}
