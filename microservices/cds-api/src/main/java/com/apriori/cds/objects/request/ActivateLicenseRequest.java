package com.apriori.cds.objects.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivateLicenseRequest {
    private ActivateLicense license;
}