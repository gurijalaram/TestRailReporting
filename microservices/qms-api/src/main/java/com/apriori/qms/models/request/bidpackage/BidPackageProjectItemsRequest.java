
package com.apriori.qms.models.request.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@SuppressWarnings("unused")
public class BidPackageProjectItemsRequest {
    private BidPackageProjectItems projectItems;
}
