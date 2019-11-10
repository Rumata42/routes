package com.rumata.routes.manager.domain.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class StationRepositoryTest {

    @Autowired
    StationRepository stationRepository;

    @Test
    public void testRequireByName() {
        stationRepository.requiredByName("station");
    }


}
