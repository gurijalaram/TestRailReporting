package com.apriori.cnh.entity.response;



import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String error;
    @JsonProperty("Message")
    private String message;
}
