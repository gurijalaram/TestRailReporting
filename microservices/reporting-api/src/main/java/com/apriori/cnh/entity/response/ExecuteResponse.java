package com.apriori.cnh.entity.response;

import com.apriori.utils.http.enums.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(location = "ExecuteResponse.json")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteResponse {
    private String executionId;
    private String status;
}
