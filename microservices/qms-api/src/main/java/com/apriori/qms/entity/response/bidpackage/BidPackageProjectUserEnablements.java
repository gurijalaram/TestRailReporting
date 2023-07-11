package com.apriori.qms.entity.response.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidPackageProjectUserEnablements {
    private boolean connectAdminEnabled;
    private boolean highMemEnabled;
    private boolean previewEnabled;
    private boolean sandboxEnabled;
    private boolean userAdminEnabled;
}
