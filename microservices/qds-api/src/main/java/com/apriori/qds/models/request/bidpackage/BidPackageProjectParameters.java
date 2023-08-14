package com.apriori.qds.models.request.bidpackage;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class BidPackageProjectParameters {
    private BidPackageProjectProfile projectProfile;
    @Builder.Default
    private String name = "N/A";
    @Builder.Default
    private String description = "N/A";
    @Builder.Default
    private String status = "N/A";
    private String type;
    @Builder.Default
    private String displayName = "N/A";
    @Builder.Default
    private String owner = "N/A";
    @Builder.Default
    private String dueAt = "N/A";
    private List<BidPackageItemRequest> items;
    private List<BidPackageProjectUserParameters> users;
    private String ownerUserIdentity;
}
