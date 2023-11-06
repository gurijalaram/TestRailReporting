package com.apriori.cid.api.models.request;

import com.apriori.cid.api.models.request.operators.Query;
import com.apriori.cid.api.models.request.operators.Sorting;
import com.apriori.css.api.request.Paging;

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