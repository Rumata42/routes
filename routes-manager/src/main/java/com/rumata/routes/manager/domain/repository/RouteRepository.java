package com.rumata.routes.manager.domain.repository;

import com.rumata.routes.manager.domain.entity.Route;
import com.rumata.routes.manager.domain.entity.Station;
import com.rumata.routes.manager.exception.IllegalInputException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long>, JpaSpecificationExecutor<Route> {

    List<Route> findAllByStation1OrStation2(Station station1, Station station2);

    /**
     * Find all routes, one of which stations is equal to this one
     */
    default List<Route> findAllByStation(Station station) {
        return findAllByStation1OrStation2(station, station);
    }

    long countByStation1OrStation2(Station station1, Station station2);

    /**
     * Count of routes between two stations, regardless of the order
     */
    default long countByStations(Station station1, Station station2) {
        return countByStation1OrStation2(station1, station2) + countByStation1OrStation2(station2, station1);
    }

    Optional<Route> findByStation1AndStation2(Station station1, Station station2);

    /**
     * Routes between two stations, regardless of the order
     * If doesn't exist - throw Exception
     */
    default Route requiredByStations(Station station1, Station station2) {
        Optional<Route> route = findByStation1AndStation2(station1, station2);
        if (route.isEmpty()) {
            route = findByStation1AndStation2(station2, station1);
        }
        if (route.isEmpty()) {
            throw new IllegalInputException("Routes between '" + station1.getName() + "' and '" + station2.getName() + "' doesn't exist.");
        }
        return route.get();
    }

    long deleteByStation1AndStation2(Station station1, Station station2);

    /**
     * Delete route between two stations, regardless of the order
     */
    default long deleteByStations(Station station1, Station station2) {
        return deleteByStation1AndStation2(station1, station2) + deleteByStation1AndStation2(station2, station1);
    }

}