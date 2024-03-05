package com.apriori.cic.api.utils;

import com.apriori.cic.api.enums.CICAPIEnum;
import com.apriori.cic.api.models.request.WorkflowJob;
import com.apriori.cic.api.models.response.ConnectorJobParts;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.ResponseWrapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

@Slf4j
public class ThingworxUtils extends CicApiTestUtil {

    /**
     * Get job parts using job id.
     *
     * @param loginSession JSESSION ID
     * @param jobID - workflow Job ID
     * @return ConnectorJobParts
     */
    public static ConnectorJobParts getJobParts(String loginSession, String jobID) {
        WorkflowJob workflowJob = WorkflowJob.builder().jobUuid(jobID).build();
        RequestEntity requestEntity = requestEntityUtil.init(CICAPIEnum.CIC_UI_GET_CONNECTOR_JOB_PARTS, ConnectorJobParts.class)
            .headers(initHeadersWithJSession(loginSession))
            .body(workflowJob)
            .expectedResponseCode(HttpStatus.SC_OK);
        ResponseWrapper<ConnectorJobParts> connectorJobPartsResponse =  HTTPRequest.build(requestEntity).post();
        return connectorJobPartsResponse.getResponseEntity();
    }
}
