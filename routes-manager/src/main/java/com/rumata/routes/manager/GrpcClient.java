package com.rumata.routes.manager;

import com.rumata.routes.proto.Empty;
import com.rumata.routes.proto.Route;
import com.rumata.routes.proto.Station;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import static com.rumata.routes.proto.RoutesServiceGrpc.newBlockingStub;

/*
 * Only to play
 */
@Slf4j
public class GrpcClient {

    public static void main(String[] args) {
        val channel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();
        val stub = newBlockingStub(channel);


//        var response = stub.addStation(Station.newBuilder().setName("York").build());
//        var response = stub.renameStation(RenameStationRequest.newBuilder().setOldName("Ololo").setNewName("123").build());
        val response = stub.addRoute(Route.newBuilder().setStation1Name("York").setStation2Name("Chicago").setCost(1.0).build());

        log.info("----------add response = " + response.getSuccess() + " / " + response.getException());

        log.info("----------list = " + stub.allStations(Empty.newBuilder().build()));

        log.info("----------routes = " + stub.stationRoutes(Station.newBuilder().setName("York").build()));

    }
}
