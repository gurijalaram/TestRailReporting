package com.apriori.cic.ui.enums;

public enum WorkflowJobState {

    PROCESSING_PENDING("Processing(Pending)"),
    PROCESSING_COSTING("Processing(Costing)"),
    PROCESSING_COSTED("Processing(Costed)"),
    PROCESSING_EXPORTING("Processing(Exporting)"),
    REPORT_ACTION_IN_PROGRESS("Report Action In Progress"),
    PLM_WRITE_ACTION_IN_PROGRESS("PLM Write Action In Progress"),
    FINISHED("Finished"),
    BATCH_COSTING_FAILED("Batch Costing Failed"),
    CANCELLED("Cancelled"),
    ERRORED("Errored"),
    QUERY_IN_PROGRESS("Query In Progress");

    private final String jobState;

    WorkflowJobState(String st) {
        jobState = st;
    }

    public String getJobState() {
        return jobState;
    }
}
