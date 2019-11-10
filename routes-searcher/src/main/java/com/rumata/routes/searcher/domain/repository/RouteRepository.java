package com.rumata.routes.searcher.domain.repository;

import com.rumata.routes.searcher.domain.entity.Route;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends org.springframework.data.repository.Repository<Route, Long> {


    /**
     * Load all stations and routes between them from database
     * route.station1 always not null
     * route.station2, id and cost can be null if there are no outgoing routes from this station
     */
    @Query("select r.id, s1.name as station1, s2.name as station2, r.cost as cost from station s1 \n" +
            " left outer join route r on r.station1_id = s1.id \n" +
            " left outer join station s2 on r.station2_id = s2.id; ")
    List<Route> allRoutes();
}
