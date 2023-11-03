package com.apriori.bcs.api.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.bcs.api.controller.BatchPartResources;
import com.apriori.bcs.api.controller.BatchResources;
import com.apriori.bcs.api.enums.BCSState;
import com.apriori.bcs.api.models.response.Batch;
import com.apriori.bcs.api.models.response.Part;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

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
