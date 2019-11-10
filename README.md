Service for management of stations and routes between them. And for searching the shortest paths between the stations.

Include:
* proto-service - protobuf service notation, generated java stubs
* routes-management - server implementation of protobuf services, serves CRUD operations with stations and routes
* routes-searcher - REST server for searching paths between stations

#### Build

##### Manager
At first need to generate protobuf stubs classes in proto-service lib:
```
cd proto-service
./gradlew install
```
Than build the routes-manager project:
```
cd routes-manager
./gradlew build
```
Than to build as docker image:
```
sudo docker build -t routes-manager:latest -f Dockerfile .
```

##### Searcher
To build the routes-searcher project:
```
cd routes-searcher
./gradlew build
```
Than to build as docker image:
```
sudo docker build -t routes-search:latest -f Dockerfile .
```

#### Run
To run server from docker images:
```
docker-compose up
```

#### Use searcher
To get all stations list use link [http://localhost:8080/station/all]() or run command:
```
curl "http://localhost:8080/station/all"
```
To calculate a cost between two stations use link [http://localhost:8080/routes/calculate_cost?station1=Boston&station2=Denver]() or run command:
```
curl "http://localhost:8080/routes/calculate_cost?station1=Boston&station2=Denver"
```
Change parameters `station1` and `station2` to calculate different paths

#### Filling the tables
There is a liquibase script `/routes-manager/src/resources/db/changelog/2019-11-10-13-02.xml` with initial data for station and route tables