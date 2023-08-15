package com.apriori.qms.models.response.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidPackageProjectUserEnablements {
    private Boolean connectAdminEnabled;
    private Boolean highMemEnabled;
    private Boolean previewEnabled;
    private Boolean sandboxEnabled;
    private Boolean userAdminEnabled;
}
