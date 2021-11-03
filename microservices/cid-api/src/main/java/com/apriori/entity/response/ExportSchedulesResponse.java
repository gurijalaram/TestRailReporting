package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;

import java.sql.Timestamp;

@Schema(location = "ExportSchedulesResponseSchema.json")
public class ExportSchedulesResponse {

    private String type;
    private Long duration;
    private Long id;
    private String jobId;
    private String status;
    private Boolean userActive;
    private String userId;
    private Timestamp exportTimestamp;
    private String exportSetName;
    private Integer numOperationCustomOutputsExported;
    private Integer numOperationsExported;
    private Integer numProcessCustomOutputsExported;
    private Integer numProcessesExported;
    private Integer numScenariosExported;
    private Integer numScenariosVisited;
    private Integer numUserDefinedAttributesExported;
    private StatusDetailsResponse statusDetail;

    public String getType() {
        return type;
    }

    public ExportSchedulesResponse setType(String type) {
        this.type = type;
        return this;
    }

    public Long getDuration() {
        return duration;
    }

    public ExportSchedulesResponse setDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public Long getId() {
        return id;
    }

    public ExportSchedulesResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public String getJobId() {
        return jobId;
    }

    public ExportSchedulesResponse setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ExportSchedulesResponse setStatus(String status) {
        this.status = status;
        return this;
    }

    public Boolean getUserActive() {
        return userActive;
    }

    public ExportSchedulesResponse setUserActive(Boolean userActive) {
        this.userActive = userActive;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public ExportSchedulesResponse setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public Timestamp getExportTimestamp() {
        return exportTimestamp;
    }

    public ExportSchedulesResponse setExportTimestamp(Timestamp exportTimestamp) {
        this.exportTimestamp = exportTimestamp;
        return this;
    }

    public String getExportSetName() {
        return exportSetName;
    }

    public ExportSchedulesResponse setExportSetName(String exportSetName) {
        this.exportSetName = exportSetName;
        return this;
    }

    public Integer getNumOperationCustomOutputsExported() {
        return numOperationCustomOutputsExported;
    }

    public ExportSchedulesResponse setNumOperationCustomOutputsExported(Integer numOperationCustomOutputsExported) {
        this.numOperationCustomOutputsExported = numOperationCustomOutputsExported;
        return this;
    }

    public Integer getNumOperationsExported() {
        return numOperationsExported;
    }

    public ExportSchedulesResponse setNumOperationsExported(Integer numOperationsExported) {
        this.numOperationsExported = numOperationsExported;
        return this;
    }

    public Integer getNumProcessCustomOutputsExported() {
        return numProcessCustomOutputsExported;
    }

    public ExportSchedulesResponse setNumProcessCustomOutputsExported(Integer numProcessCustomOutputsExported) {
        this.numProcessCustomOutputsExported = numProcessCustomOutputsExported;
        return this;
    }

    public Integer getNumProcessesExported() {
        return numProcessesExported;
    }

    public ExportSchedulesResponse setNumProcessesExported(Integer numProcessesExported) {
        this.numProcessesExported = numProcessesExported;
        return this;
    }

    public Integer getNumScenariosExported() {
        return numScenariosExported;
    }

    public ExportSchedulesResponse setNumScenariosExported(Integer numScenariosExported) {
        this.numScenariosExported = numScenariosExported;
        return this;
    }

    public Integer getNumScenariosVisited() {
        return numScenariosVisited;
    }

    public ExportSchedulesResponse setNumScenariosVisited(Integer numScenariosVisited) {
        this.numScenariosVisited = numScenariosVisited;
        return this;
    }

    public Integer getNumUserDefinedAttributesExported() {
        return numUserDefinedAttributesExported;
    }

    public ExportSchedulesResponse setNumUserDefinedAttributesExported(Integer numUserDefinedAttributesExported) {
        this.numUserDefinedAttributesExported = numUserDefinedAttributesExported;
        return this;
    }

    public StatusDetailsResponse getStatusDetail() {
        return statusDetail;
    }

    public ExportSchedulesResponse setStatusDetail(StatusDetailsResponse statusDetail) {
        this.statusDetail = statusDetail;
        return this;
    }
}
