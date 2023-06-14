package com.apriori.qms.entity.response.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BidPackageProjectNotificationParameters {
    private String projectIdentity;
    private long unreadNotificationsCount;
}
