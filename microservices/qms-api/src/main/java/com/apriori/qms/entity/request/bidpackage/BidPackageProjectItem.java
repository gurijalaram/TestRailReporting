package com.apriori.qms.entity.request.bidpackage;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@SuppressWarnings("unused")
public class BidPackageProjectItem {
    private BidPackageItemParameters bidPackageItem;
    @Builder.Default
    private String identity = "N/A";
}
