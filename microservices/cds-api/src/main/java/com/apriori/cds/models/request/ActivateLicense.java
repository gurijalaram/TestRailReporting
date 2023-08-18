package com.apriori.cds.models.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivateLicense {
    private Boolean active;
    private String updatedBy;
}