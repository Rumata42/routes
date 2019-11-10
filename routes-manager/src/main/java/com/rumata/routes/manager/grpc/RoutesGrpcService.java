package com.rumata.routes.manager.grpc;


import com.rumata.routes.manager.exception.IllegalInputException;
import com.rumata.routes.manager.service.interfaces.RouteService;
import com.rumata.routes.manager.service.interfaces.StationService;
import com.rumata.routes.proto.*;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lognet.springboot.grpc.GRpcService;

import java.math.BigDecimal;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Controller resolves protobuf RoutesService service
 */
@Slf4j
@AllArgsConstructor
@GRpcService
public class RoutesGrpcService extends RoutesServiceGrpc.RoutesServiceImplBase {

    private final StationService stationService;
    private final RouteService routeService;


    @Override
    public void allStations(Empty request, StreamObserver<Stations> responseObserver) {
        withCheckingException(responseObserver,
                () -> {
                    val stationsBuilder = Stations.newBuilder();
                    stationService.allStations().stream()
                            .map(name -> Station.newBuilder().setName(name).build())
                            .forEach(stationsBuilder::addStations);
                    return stationsBuilder.build();
                }, errorMessage -> Stations.newBuilder().build());
    }

    @Override
    public void addStation(Station request, StreamObserver<StatusResponse> responseObserver) {
        withCheckingException(responseObserver, () -> {
            stationService.addStation(nonEmptyName(request.getName()));
            return true;
        });
    }

    @Override
    public void renameStation(RenameStationRequest request, StreamObserver<StatusResponse> responseObserver) {
        withCheckingException(responseObserver, () -> {
            stationService.renameStation(request.getOldName(), nonEmptyName(request.getNewName()));
            return true;
        });
    }

    @Override
    public void removeStation(Station request, StreamObserver<StatusResponse> responseObserver) {
        withCheckingException(responseObserver, () -> {
            stationService.removeStation(request.getName());
            return true;
        });
    }

    @Override
    public void stationRoutes(Station request, StreamObserver<Routes> responseObserver) {
        withCheckingException(responseObserver,
                () -> {
                    val routesBuilder = Routes.newBuilder();
                    routeService.stationRoutes(request.getName())
                            .forEach((name, cost) -> {
                                val route = Route.newBuilder()
                                        .setStation1Name(request.getName())
                                        .setStation2Name(name)
                                        .setCost(cost.doubleValue())
                                        .build();
                                routesBuilder.addRoutes(route);
                            });
                    return routesBuilder.build();
                }, errorMessage -> Routes.newBuilder().build());
    }

    @Override
    public void addRoute(Route request, StreamObserver<StatusResponse> responseObserver) {
        withCheckingException(responseObserver, () -> {
            routeService.addRoute(request.getStation1Name(), request.getStation2Name(), nonNegativeCost(request.getCost()));
            return true;
        });
    }

    @Override
    public void changeRouteCost(Route request, StreamObserver<StatusResponse> responseObserver) {
        withCheckingException(responseObserver, () -> {
            routeService.changeRouteCost(request.getStation1Name(), request.getStation2Name(), nonNegativeCost(request.getCost()));
            return true;
        });
    }

    @Override
    public void removeRoute(Route request, StreamObserver<StatusResponse> responseObserver) {
        withCheckingException(responseObserver, () -> {
            routeService.removeRoute(request.getStation1Name(), request.getStation2Name());
            return true;
        });
    }

    /*-----------------------------PRIVATE-----------------------------*/

    private void withCheckingException(StreamObserver<StatusResponse> responseObserver, BooleanSupplier action) {
        withCheckingException(responseObserver,
                () -> StatusResponse.newBuilder()
                        .setSuccess(action.getAsBoolean())
                        .build(),
                errorMessage -> StatusResponse.newBuilder()
                        .setSuccess(false)
                        .setException(errorMessage)
                        .build()
        );
    }

    private <T> void withCheckingException(StreamObserver<T> responseObserver, Supplier<T> action, Function<String, T> errorResponseBuilder) {
        try {
            val response = action.get();
            responseObserver.onNext(response);
        } catch (IllegalInputException e) {
            responseObserver.onNext(errorResponseBuilder.apply(e.getMessage()));
        } catch (Exception e) {
            log.error("Exception while processing  request: ", e);
            responseObserver.onNext(errorResponseBuilder.apply("Unrecognized exception"));
        }
        responseObserver.onCompleted();
    }

    private String nonEmptyName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalInputException("Name not be non-empty.");
        }
        return name;
    }

    private BigDecimal nonNegativeCost(double cost) {
        if (cost < 0) {
            throw new IllegalInputException("Cost must be greater ot equal zero.");
        }
        return BigDecimal.valueOf(cost);
    }


}
