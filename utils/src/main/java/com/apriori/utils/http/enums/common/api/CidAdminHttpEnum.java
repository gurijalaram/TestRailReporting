package com.apriori.utils.http.enums.common.api;

import com.apriori.utils.http.enums.common.EdcQaAPI;

public enum CidAdminHttpEnum implements EdcQaAPI {

    GET_EXPORT_JOBS("ws/datamart/export-schedules/%s/history"),
    POST_EXPORT_SCHEDULES("ws/datamart/export-schedules"),
    POST_EXPORT_NOW("ws/datamart/export-schedules/%s/execute-export");

    private final String endpoint;

    CidAdminHttpEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }

}
