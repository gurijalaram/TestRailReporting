package com.apriori.cidappapi.entity.request;

import com.apriori.cidappapi.entity.request.operators.Query;
import com.apriori.cidappapi.entity.request.operators.Sorting;
import com.apriori.entity.request.Paging;

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