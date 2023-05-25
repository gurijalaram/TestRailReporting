
package com.apriori.qms.entity.request.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@SuppressWarnings("unused")
public class BidPackageProjectItem {
    private BidPackageItemParameters bidPackageItem;
}
