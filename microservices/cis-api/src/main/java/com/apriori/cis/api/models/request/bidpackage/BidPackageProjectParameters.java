package com.apriori.cis.api.models.request.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidPackageProjectParameters {
    private String name;
    private String description;
    private String status;
    private String type;
    private String displayName;
    private BidPackageProjectProfile projectProfile;
    private List<BidPackageItemRequest> items;
    private List<BidPackageProjectUserParameters> users;
}
