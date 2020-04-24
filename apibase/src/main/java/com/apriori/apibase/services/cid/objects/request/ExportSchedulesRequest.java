package com.apriori.apibase.services.cid.objects.request;

import java.util.ArrayList;
import java.util.List;

/**
 *  Request data example
 *  {
 *    "type" : "exportSetRequestBean",
 *    "disabled" : false,
 *    "name" : "All Delta.Default Scenarios",
 *    "schedules" : [ {
 *       "type" : "SIMPLE",
 *       "startTime" : "1578921720000",
 *       "repeatCount" : 0,
 *       "repeatInterval" : 0
 *    } ],
 *    "description" : "",
 *    "exportDynamicRollups" : false,
 *    "exportScope" : "All Delta",
 *    "sourceSchema" : "Default Scenarios"
 * }
 */
public class ExportSchedulesRequest {

    private String type;
    private boolean disabled;
    private String name;
    private List<SchedulesConfigurationRequest> schedules = new ArrayList<>();
    private String description;
    private boolean exportDynamicRollups;
    private String exportScope;
    private String sourceSchema;


    public String getType() {
        return type;
    }

    public ExportSchedulesRequest setType(String type) {
        this.type = type;
        return this;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public ExportSchedulesRequest setDisabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public String getName() {
        return name;
    }

    public ExportSchedulesRequest setName(String name) {
        this.name = name;
        return this;
    }

    public List<SchedulesConfigurationRequest> getSchedules() {
        return schedules;
    }

    public ExportSchedulesRequest setSchedules(List<SchedulesConfigurationRequest> schedules) {
        this.schedules = schedules;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ExportSchedulesRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isExportDynamicRollups() {
        return exportDynamicRollups;
    }

    public ExportSchedulesRequest setExportDynamicRollups(boolean exportDynamicRollups) {
        this.exportDynamicRollups = exportDynamicRollups;
        return this;
    }

    public String getExportScope() {
        return exportScope;
    }

    public ExportSchedulesRequest setExportScope(String exportScope) {
        this.exportScope = exportScope;
        return this;
    }

    public String getSourceSchema() {
        return sourceSchema;
    }

    public ExportSchedulesRequest setSourceSchema(String sourceSchema) {
        this.sourceSchema = sourceSchema;
        return this;
    }
}
