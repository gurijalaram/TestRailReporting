package com.apriori.bcs.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.bcs.enums.BCSState;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BcsTestUtils extends TestUtil {

    private static ResponseWrapper<Batch> batchResponse;
    private static RequestEntity requestEntity = null;
    private static BcsTestUtils bcsTestUtils;
    private static final long WAIT_TIME = 300;

    static {
        bcsTestUtils = new BcsTestUtils();
    }

    public BcsTestUtils() {
        super();
    }

    /**
     * Checks an wait until the batch part status is completed
     *
     * @param batchIdentity - Batch ID to send
     * @param partIdentity - Part ID to send
     * @return BCSState
     */
    public static BCSState waitUntilPartStateIsCompleted(String batchIdentity, String partIdentity) {
        long initialTime = System.currentTimeMillis() / 1000;

        Part part;
        do {
            requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PART_BY_BATCH_PART_IDS, Part.class)
                    .inlineVariables(batchIdentity, partIdentity);
            part = (Part) HTTPRequest.build(requestEntity).get().getResponseEntity();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        } while (!part.getState().equals(BCSState.COMPLETED.toString())
                && ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);
        return BCSState.valueOf(part.getState());
    }

    /**
     * Cancel any batch in a non-terminal state
     * @param batch
     * @return
     */
    public static void checkAndCancelBatch(Batch batch) {
        List<String> batchState = new ArrayList<String>();
        batchState.add("CANCELLED");
        batchState.add("ERRORED");
        batchState.add("REJECTED");
        batchState.add("COMPLETED");
        for (String state : batchState) {
            if (!state.contains(batch.getState())) {
                BatchResources.cancelBatchProcessing(batch.getIdentity());
                break;
            }
        }
    }
}
