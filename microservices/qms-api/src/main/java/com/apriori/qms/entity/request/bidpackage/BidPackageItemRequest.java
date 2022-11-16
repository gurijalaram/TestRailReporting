package com.apriori.qms.entity.request.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidPackageItemRequest {
    BidPackageItemParameters bidPackageItem;
}
