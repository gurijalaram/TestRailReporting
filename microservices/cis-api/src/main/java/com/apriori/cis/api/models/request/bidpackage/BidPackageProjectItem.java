
package com.apriori.cis.api.models.request.bidpackage;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@SuppressWarnings("unused")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BidPackageProjectItem {
    private BidPackageItem bidPackageItem;

}
