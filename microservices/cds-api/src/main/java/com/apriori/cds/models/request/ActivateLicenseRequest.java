package com.apriori.cds.models.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivateLicenseRequest {
    private ActivateLicense license;
}