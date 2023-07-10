package com.apriori.qms.entity.response.bidpackage;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(location = "BidPackageProjectNotificationResponseSchema.json")
@JsonRootName("response")
public class BidPackageProjectNotificationResponse {
    List<BidPackageProjectNotificationParameters> notificationsCount;
}