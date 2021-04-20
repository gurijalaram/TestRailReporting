package com.apriori.sds.tests;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;
import util.SDSTestUtil;

// TODO z: issue with request
public class ScenarioAssociationsTest extends SDSTestUtil {


    @Test
    @TestRail(testCaseId = {"6928"})
    @Description("Find scenario associations for a given scenario matching a specified query.")
    public void getAssociations() {

        ResponseWrapper<Object> response =
            new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_ASSOCIATIONS_BY_COMPONENT_SCENARIO_IDS, null,
                new APIAuthentication().initAuthorizationHeaderContent(token),
                getComponentId(), getScenarioId()
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }


    @Test
    @TestRail(testCaseId = {"6929"})
    @Description("Get the current representation of a scenario assocation.")
    public void getAssociationsByIdentity() {
        ResponseWrapper<Object> response =
            new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_ASSOCIATIONS_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS, null,
                new APIAuthentication().initAuthorizationHeaderContent(token),
                getComponentId(), getScenarioId()
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }
}
