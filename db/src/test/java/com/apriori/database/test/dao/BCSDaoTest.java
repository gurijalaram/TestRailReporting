package com.apriori.database.test.dao;

import com.apriori.database.dao.BCSDao;
import com.apriori.database.dto.BCSBatchDTO;
import com.apriori.database.dto.BCSPartBenchmarkingDTO;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class BCSDaoTest {

    @Test
    @Disabled("Use to test costing data")
    public void testInsertCostingData() {
        System.setProperty("global_db_connection", "local");

        BCSBatchDTO bcsBatchDTO = BCSBatchDTO.builder()
            .batchId("test")
            .rollupScenarioName("TestScenarioName")
            .externalId("TestExternalId")
            .rollupName("TestRollupName")
            .customerIdentity("TestCustomerId")
            .build();

        BCSPartBenchmarkingDTO bcsPartBenchmarkingDTO = BCSPartBenchmarkingDTO.builder()
            .costingDuration(121L)
            .costingResults("675")
            .errorMessage("")
            .partName("testPart")
            .batchIdentity("test")
            .identity("test1")
            .state("testState")
            .build();

        BCSPartBenchmarkingDTO bcsPartBenchmarkingDTO1 = BCSPartBenchmarkingDTO.builder()
            .costingDuration(323L)
            .costingResults("675")
            .errorMessage("")
            .partName("testPart")
            .batchIdentity("test1")
            .identity("test2")
            .state("testState")
            .build();

        List<BCSPartBenchmarkingDTO> bcsParts = Arrays.asList(bcsPartBenchmarkingDTO, bcsPartBenchmarkingDTO1);

        BCSDao.insertCostingData(bcsBatchDTO, bcsParts);
    }
}
