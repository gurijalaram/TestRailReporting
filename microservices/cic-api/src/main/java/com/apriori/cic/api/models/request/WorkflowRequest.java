package com.apriori.cic.api.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowRequest {
    private String customer;
    private Query query;
    private String plmSystem;
    private String name;
    private String description;
    private String schedule;
    private String selectedEmailTemplate;
    private Boolean isEmailTemplateSelected;
    private EmailConfiguration emailConfiguration;
    private String emailRecipientType;
    private String emailRecipientValue;
    private DefaultValues defaultValues;
    private Boolean isSchedulerEnabled;
    private Boolean isEmailReportNameSelected;
    private String selectedEmailReportName;
    private EmailConfiguration emailReportConfiguration;
    private EmailConfiguration plmWriteReportConfiguration;
    private DefaultValues plmWriteConfiguration;
    private Boolean isPlmWriteReportNameSelected;
    private String selectedPlmWriteReportName;
    private EmailPartFilter emailPartFilter;
    private Boolean applyEmailFilter;
    private Boolean applyEmailReportsFilter;
    private Boolean applyPlmWriteReportsFilter;
    private Boolean applyPlmWritePartFilter;
    private Boolean useLatestRevision;
    private String jobMonitoringRecipients;
    private String partSelectionType;
}
