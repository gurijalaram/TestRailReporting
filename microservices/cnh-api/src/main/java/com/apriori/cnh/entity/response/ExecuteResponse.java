package com.apriori.cnh.entity.response;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Schema(location = "CnhResponseSchema.json")
@Data
public class ExecuteResponse {
    private String executionId;
    private String status;
}
