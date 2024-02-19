package com.apriori.qms.api.models.request.bidpackage;

import com.apriori.shared.util.models.response.Pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@SuppressWarnings("unused")
public class BidPackageProjectItems extends Pagination {
    private List<BidPackageProjectItem> projectItem;
}
