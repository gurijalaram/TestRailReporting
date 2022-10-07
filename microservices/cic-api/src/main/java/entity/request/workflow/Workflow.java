package entity.request.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Workflow {
    private String customer;
    private QueryDefinition queryDefinition;
    private String plmSystem;
    private String name;
    private String description;
    private String schedule;
    private String selectedEmailTemplate;
    private boolean isEmailTemplateSelected;
    private CIEmailConfiguration ciEmailConfiguration;
    private String emailRecipientType;
    private boolean isSchedulerEnabled;
    private boolean isEmailReportNameSelected;
    private String selectedEmailReportName;
    private boolean isPlmWriteReportNameSelected;
    private String selectedPlmWriteReportName;
    private boolean applyEmailFilter;
    private boolean applyEmailReportsFilter;
    private boolean applyPlmWriteReportsFilter;
    private boolean applyPlmWritePartFilter;
    private boolean useLatestRevision;
    private String jobMonitoringRecipients;
    private String partSelectionType;
}

