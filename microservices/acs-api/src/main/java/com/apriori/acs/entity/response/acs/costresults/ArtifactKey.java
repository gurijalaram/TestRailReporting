package com.apriori.acs.entity.response.acs.costresults;

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
public class ArtifactKey {
    private String artifactTypeName;
    private Integer sequenceNumber;
    private String displayName;
}
