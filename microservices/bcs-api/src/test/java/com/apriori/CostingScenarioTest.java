package com.apriori;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.enums.BCSState;
import com.apriori.bcs.models.response.Batch;
import com.apriori.bcs.models.response.Part;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.http.utils.TestUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CostingScenarioTest extends TestUtil {

    @Test
    @TestRail(id = {4278, 4177})
    @Description("Test costing scenario, includes creating a new batch, a new part and waiting for the costing " +
        "process to complete. Then retrieve costing results.")
    public void createBatchPartAndCostPart() {
        // create batchObject
        Batch batchObject;
        ResponseWrapper<Batch> batchResponse;
        batchResponse = BatchResources.createBatch();
        batchObject = batchResponse.getResponseEntity();

        assertThat(batchObject.getState(), is(equalTo(BCSState.CREATED.toString())));

        //create part
        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartByID(batchObject.getIdentity());
        Part part = partResponse.getResponseEntity();

        assertThat(part.getState(), is(equalTo(BCSState.CREATED.toString())));

        //start batchObject costing
        BatchResources.startBatchCosting(batchObject);

        //Wait Until costing process is completed and get part report.
        BatchPartResources.getPartReport(batchObject.getIdentity(), part.getIdentity());
    }
}
