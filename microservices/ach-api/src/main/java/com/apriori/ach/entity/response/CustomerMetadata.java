package com.apriori.ach.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CustomerMetadata {
    private String customerName;
    private Integer maxCadFileSize;
    private String salesforceId;
}