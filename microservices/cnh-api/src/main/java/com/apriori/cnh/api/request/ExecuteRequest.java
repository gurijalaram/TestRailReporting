package com.apriori.cnh.api.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExecuteRequest {
    private List<Params> params;
}
