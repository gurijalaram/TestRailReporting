package com.apriori.qds.models.request.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidPackageParameters {
    private String description;
    private String name;
    private String status;
    private String assignedTo;
}
