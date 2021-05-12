package com.apriori.apibase.services.bcs.objects;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "bcs/CisCustomerBatchesSchema.json")
public class Batches extends Pagination {
    private Batches response;
    private List<Batch> items;

    public Batches getResponse() {
        return this.response;
    }

    public Batches setResponse(Batches response) {
        this.response = response;
        return this;
    }

    public List<Batch> getItems() {
        return this.items;
    }

    public Batches setItems(List<Batch> items) {
        this.items = items;
        return this;
    }
}
