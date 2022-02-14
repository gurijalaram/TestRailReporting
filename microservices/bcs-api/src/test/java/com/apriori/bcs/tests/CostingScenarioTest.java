package com.apriori.bcs.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.enums.BCSState;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;

import org.apache.http.HttpStatus;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CostingScenarioTest extends TestUtil {

    private static final Logger logger = LoggerFactory.getLogger(CostingScenarioTest.class);
    private static Batch batch;
    private static ResponseWrapper<Object> response;

    @Issue("AP-70043")
    @Test
    @TestRail(testCaseId = {"4278", "4177"})
    @Description("Test costing scenario, includes creating a new batch, a new part and waiting for the costing " +
            "process to complete. Then retrieve costing results.")
    public void createBatchPartAndCostPart() {
        // create batch
        response = BatchResources.createBatch();
        batch = (Batch)response.getResponseEntity();
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(batch.getState(), is(equalTo(BCSState.CREATED.toString())));

        //create part
        response =  BatchPartResources.createNewBatchPartByID(batch.getIdentity());
        Part part = (Part) response.getResponseEntity();
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(part.getState(), is(equalTo(BCSState.LOADING.toString())));

        //start batch costing
        BatchResources.startBatchCosting(batch);

        //Wait Until costing process is completed and get part report.
        response = BatchPartResources.getPartReport(batch.getIdentity(), part.getIdentity());
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
