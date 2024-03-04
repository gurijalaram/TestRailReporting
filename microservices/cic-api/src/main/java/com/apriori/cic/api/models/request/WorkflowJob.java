package com.apriori.cic.api.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowJob {
    @Builder.Default
    private Integer pageSize = 50;
    @Builder.Default
    private Integer pageNumber = 1;
    private String jobUuid;
}
