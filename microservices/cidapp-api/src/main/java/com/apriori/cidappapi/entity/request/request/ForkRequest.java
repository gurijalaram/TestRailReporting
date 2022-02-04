package com.apriori.cidappapi.entity.request.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForkRequest {
    private String assignedTo;
    private String costMaturity;
    private Boolean override;
    private String status;
}
