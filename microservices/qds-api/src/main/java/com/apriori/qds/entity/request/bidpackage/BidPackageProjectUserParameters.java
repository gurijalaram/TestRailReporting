package com.apriori.qds.entity.request.bidpackage;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BidPackageProjectUserParameters {
    private String userIdentity;
    private String userEmail;
    private String role;
    private String customerIdentity;
    private String identity;
}
