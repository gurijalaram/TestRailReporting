package com.apriori.bcs.utils;

import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.models.response.Batch;
import com.apriori.bcs.models.response.Part;
import com.apriori.bcs.models.response.Results;
import com.apriori.http.utils.QueryParams;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class BcsTestUtil {

    private static Batch batch;
    private static Part part;
    private static Part assembly;

    /**
     * create batch
     *
     * @return current class object
     */
    public BcsTestUtil createBatch() {
        batch = BatchResources.createBatch().getResponseEntity();
        return this;
    }

    /**
     * add part to batch
     *
     * @param queryParams QueryParams
     * @param partFile        - part file
     * @return current class object
     */
    public BcsTestUtil addPartToBatch(QueryParams queryParams, File partFile) {
        part = BatchPartResources.addPartToBatch(queryParams, partFile, batch.getIdentity()).getResponseEntity();
        return this;
    }

    /**
     * add Assembly to batch
     *
     * @param queryParams QueryParams
     * @param partFile        - part file
     * @return current class object
     */
    public BcsTestUtil addAssemblyToBatch(QueryParams queryParams, File partFile) {
        assembly = BatchPartResources.addPartToBatch(queryParams, partFile, batch.getIdentity()).getResponseEntity();
        return this;
    }

    /**
     * start batch costing
     *
     * @return current class object
     */
    public BcsTestUtil startCosting() {
        BatchResources.startBatchCosting(batch);
        if (!BatchResources.waitUntilBatchCostingReachedExpected(batch.getIdentity())) {
            throw new RuntimeException(String.format("Batch costing with identity >>%s<< stuck at >>%s<<", batch.getIdentity(), batch.getState()));
        }
        return this;
    }

    /**
     * get Part Result
     *
     * @return Results
     */
    public Results getPartResult() {
        return BatchPartResources.getBatchPartResults(batch.getIdentity(), part.getIdentity()).getResponseEntity();
    }

    /**
     * get Part Assembly Result
     *
     * @return Results
     */
    public Results getAssemblyResult() {
        return BatchPartResources.getBatchPartResults(batch.getIdentity(), assembly.getIdentity()).getResponseEntity();
    }

    /**
     * Get Part Information
     *
     * @return Part
     */
    public Part getPartInfo() {
        return BatchPartResources.getBatchPartRepresentation(batch.getIdentity(), part.getIdentity()).getResponseEntity();
    }

    /**
     * get Batch after batch creation
     *
     * @return batch
     */
    public Batch getBatch() {
        return batch;
    }
}
