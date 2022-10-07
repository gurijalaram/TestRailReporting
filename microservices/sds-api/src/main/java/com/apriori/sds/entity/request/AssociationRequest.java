package com.apriori.sds.entity.request;

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
public class AssociationRequest {
    private Integer occurrences;
    private String scenarioIdentity;
    private String createdBy;
    private Boolean excluded;
}
