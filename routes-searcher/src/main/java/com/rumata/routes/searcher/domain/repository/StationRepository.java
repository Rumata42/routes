package com.rumata.routes.searcher.domain.repository;

import com.rumata.routes.searcher.domain.entity.Route;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends org.springframework.data.repository.Repository<Route, Long> {


    /**
     * List of all station names
     */
    @Query("select name from station ")
    List<String> allRoutes();
}
