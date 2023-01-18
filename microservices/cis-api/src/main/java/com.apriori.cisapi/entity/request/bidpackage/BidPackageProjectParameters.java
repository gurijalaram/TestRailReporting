package com.apriori.cisapi.entity.request.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidPackageProjectParameters {
    private String name;
    private String description;
    private String status;
    private String type;
    public BidPackageProjectProfile projectProfile;
}
