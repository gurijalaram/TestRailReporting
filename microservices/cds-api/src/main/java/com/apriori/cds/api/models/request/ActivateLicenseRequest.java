package com.apriori.cds.api.models.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivateLicenseRequest {
    private ActivateLicense license;
}