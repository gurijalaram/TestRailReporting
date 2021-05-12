package com.apriori.apibase.services.bcs.objects.requests;

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
