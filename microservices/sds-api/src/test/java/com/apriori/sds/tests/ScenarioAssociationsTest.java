package com.apriori.sds.tests;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.ScenarioIterationItemsResponse;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;
import util.SDSTestUtil;

// TODO z: issue with request
public class ScenarioAssociationsTest extends SDSTestUtil {


    @Test
    @TestRail(testCaseId = {"6928"})
    @Description("Find scenario associations for a given scenario matching a specified query.")
    @Ignore
    public void getAssociations() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_ASSOCIATIONS_BY_COMPONENT_SCENARIO_IDS, null)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<Void> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }


    // TODO z: add association identity
    @Test
    @TestRail(testCaseId = {"6929"})
    @Description("Get the current representation of a scenario assocation.")
    public void getAssociationsByIdentity() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_ASSOCIATIONS_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS, null)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<Void> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }
}
