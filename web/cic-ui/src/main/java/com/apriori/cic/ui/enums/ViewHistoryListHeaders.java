package com.apriori.cic.ui.enums;

/**
 * enum Connector List table header field names
 */
public enum ViewHistoryListHeaders {
    ID("ID"),
    WORKFLOW_NAME("Workflow Name"),
    STARTED_AT("Started At"),
    STARTED_BY("Started By"),
    DURATION("Duration (hh:mm:ss)"),
    JOB_STATUS("Job Status"),
    STATUS_DETAILS("Status Details"),
    NO_OF_COMPONENTS("Number of Components"),
    COMPONENTS_PROCESSED("Components Processed"),
    COMPONENTS_FAILED("Components Failed");

    private final String columnName;

    ViewHistoryListHeaders(String colName) {
        columnName = colName;
    }

    public String getColumnName() {
        return columnName;
    }
}
