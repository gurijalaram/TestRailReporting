package com.apriori.cds.models.request;

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
public class License {
    private String description;
    private String apVersion;
    private String createdBy;
    private String active;
    private String license;
    private String licenseTemplate;
}
