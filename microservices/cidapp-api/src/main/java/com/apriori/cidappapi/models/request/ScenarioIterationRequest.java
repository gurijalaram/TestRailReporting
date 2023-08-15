package com.apriori.cidappapi.models.request;

import com.apriori.cidappapi.models.request.operators.Query;
import com.apriori.cidappapi.models.request.operators.Sorting;
import com.apriori.models.request.Paging;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScenarioIterationRequest {
    private Query query;
    private Paging paging;
    private List<Sorting> sorting;

}