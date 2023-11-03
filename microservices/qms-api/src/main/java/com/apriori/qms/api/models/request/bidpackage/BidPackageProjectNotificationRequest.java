package com.apriori.qms.api.models.request.bidpackage;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BidPackageProjectNotificationRequest {
    private List<String> projectIdentities;
}
