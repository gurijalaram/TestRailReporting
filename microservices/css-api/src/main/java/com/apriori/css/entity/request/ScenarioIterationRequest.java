package com.apriori.css.entity.request;

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