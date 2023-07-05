package com.apriori.cisapi.entity.request.bidpackage;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BidPackageProjectUserRequest {
    private List<BidPackageProjectUserParameters> projectUsers;
    private BidPackageProjectUserParameters projectUser;
}
