package com.apriori.cnh.entity.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExecuteRequest {
    private List<Params> params;
}
