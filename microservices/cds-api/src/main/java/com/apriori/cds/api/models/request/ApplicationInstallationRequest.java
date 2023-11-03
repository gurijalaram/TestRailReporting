package com.apriori.cds.api.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ApplicationInstallationRequest {
    private String applicationIdentity;
    private String siteIdentity;
}