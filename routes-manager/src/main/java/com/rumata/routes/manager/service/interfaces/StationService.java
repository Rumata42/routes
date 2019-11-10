package com.rumata.routes.manager.service.interfaces;

import java.util.Set;

public interface StationService {

    Set<String> allStations();

    void addStation(String name);

    void renameStation(String oldName, String newName);

    void removeStation(String name);

}
