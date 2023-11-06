package com.apriori.cic.ui.pagedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationsData {
    private String emailTemplate;
    private String recipientEmailType;
    private String recipientEmailAddress;
    private String reportName;
    private String reportCurrencyCode;
    private String reportCostRounding;
}
