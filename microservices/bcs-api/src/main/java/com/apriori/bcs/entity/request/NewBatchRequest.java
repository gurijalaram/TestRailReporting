package com.apriori.bcs.entity.request;

public class NewBatchRequest {
    private NewBatchProperties batch;

    public NewBatchProperties getBatch() {
        return this.batch;
    }

    public NewBatchRequest setBatch(NewBatchProperties batch) {
        this.batch = batch;
        return this;
    }
}
