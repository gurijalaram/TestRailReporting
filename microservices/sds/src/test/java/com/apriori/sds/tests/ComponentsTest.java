package com.apriori.sds.tests;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.Component;
import com.apriori.sds.entity.response.ComponentsItemsResponse;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;
import util.SDSTestUtil;

public class ComponentsTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = "6937")
    @Description("Find components for a customer matching a specified query.")
    public void getComponents() {
        this.receiveComponents();
    }

    @Test
    @TestRail(testCaseId = "6938")
    @Description("Get the current representation of a component.")
    public void getComponentByIdentity() {
        ComponentsItemsResponse componentsListResponse = this.receiveComponents().getResponseEntity();
        ResponseWrapper<Component> response =
            new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_COMPONENT_SINGLE_BY_IDENTITY, Component.class,
                new APIAuthentication().initAuthorizationHeaderContent(token),
                componentsListResponse.getResponse().getItems().get(0).getIdentity()
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    private ResponseWrapper<ComponentsItemsResponse> receiveComponents() {
        ResponseWrapper<ComponentsItemsResponse> response = new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_COMPONENTS, ComponentsItemsResponse.class,
            new APIAuthentication().initAuthorizationHeaderContent(token)
        );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());

        return response;
    }
}
