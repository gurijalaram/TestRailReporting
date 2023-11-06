package com.apriori.ach.api.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CustomerMetadata {
    private String customerName;
    private Integer maxCadFileSize;
    private String salesforceId;
}