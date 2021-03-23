package com.apriori.entity.response.upload;

import com.apriori.utils.http.enums.Schema;

@Schema
public class WorkorderStatusResponse {

    public String workorderStatus;

    public String getWorkorderStatus() {
        return this.workorderStatus;
    }

    public WorkorderStatusResponse setWorkorderStatusResponse(String workorderStatus) {
        this.workorderStatus = workorderStatus;
        return this;
    }

}
