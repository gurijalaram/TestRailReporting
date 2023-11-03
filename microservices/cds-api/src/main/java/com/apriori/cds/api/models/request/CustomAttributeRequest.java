package com.apriori.cds.api.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomAttributeRequest {
    private String key;
    private String name;
    private String value;
    private String type;
    private String createdBy;
    private String updatedBy;
}