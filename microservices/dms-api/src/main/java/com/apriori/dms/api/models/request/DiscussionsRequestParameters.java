package com.apriori.dms.api.models.request;

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
public class DiscussionsRequestParameters {
    private String status;
    private String description;
    private String assigneeEmail;
    private DmsAttributesRequest attributes;
}