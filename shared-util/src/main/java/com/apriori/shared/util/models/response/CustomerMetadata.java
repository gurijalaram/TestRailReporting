package com.apriori.shared.util.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerMetadata {
    private Integer maxCadFileSize;
    private String salesforceId;
    private String customerName;
}