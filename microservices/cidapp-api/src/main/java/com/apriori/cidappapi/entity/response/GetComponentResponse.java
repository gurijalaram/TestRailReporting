package com.apriori.cidappapi.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cidapp/GetComponentsResponse.json")
public class GetComponentResponse extends Pagination {
    private GetComponentResponse response;
    private List<GetComponentItems> items;

    public GetComponentResponse getResponse() {
        return response;
    }

    public List<GetComponentItems> getItems() {
        return items;
    }
}
