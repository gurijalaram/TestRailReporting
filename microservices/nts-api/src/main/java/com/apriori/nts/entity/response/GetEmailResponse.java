package com.apriori.nts.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;


@Schema(location = "NtsGetEmailsResponseSchema.json")
public class GetEmailResponse  extends Pagination {

    private GetEmailResponse response;
    private List<Email> items;

    public GetEmailResponse getResponse() {
        return this.response;
    }

    public GetEmailResponse setResponse(GetEmailResponse response) {
        this.response = response;
        return this;
    }

    public List<Email> getItems() {
        return this.items;
    }

    public GetEmailResponse setItems(List<Email> items) {
        this.items = items;
        return this;
    }
}
