package com.apriori.shared.util.enums;

import com.apriori.shared.util.interfaces.EdcQaAPI;

public enum CidAdminHttpEnum implements EdcQaAPI {

    GET_EXPORT_JOB_BY_SCHEDULE_ID("export-schedules/%s/history"),
    POST_EXPORT_SCHEDULES("export-schedules"),
    POST_EXPORT_NOW_BY_SCHEDULE_ID("export-schedules/%s/execute-export"),
    DELETE_EXPORT_BY_SCHEDULE_ID("export-schedules/%s");

    private final String endpoint;
    private final String dataMartPrefix = "ws/datamart/";

    CidAdminHttpEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return dataMartPrefix + endpoint;
    }

}
