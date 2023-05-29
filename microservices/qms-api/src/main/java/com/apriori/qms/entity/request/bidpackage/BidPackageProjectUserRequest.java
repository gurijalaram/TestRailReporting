package com.apriori.qms.entity.request.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidPackageProjectUserRequest {
    private List<BidPackageProjectUserParameters> projectUsers;
}
