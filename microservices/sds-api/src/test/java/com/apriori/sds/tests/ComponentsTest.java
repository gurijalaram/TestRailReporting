package com.apriori.sds.tests;

import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.Component;
import com.apriori.sds.entity.response.ComponentsItemsResponse;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class ComponentsTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"6937"})
    @Description("Find components for a customer matching a specified query.")
    public void getComponents() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_COMPONENTS, ComponentsItemsResponse.class);

        ResponseWrapper<ComponentsItemsResponse> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6938"})
    @Description("Get the current representation of a component.")
    public void getComponentByIdentity() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_COMPONENT_SINGLE_BY_IDENTITY, Component.class)
                .inlineVariables(getComponentId());

        ResponseWrapper<Component> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "7248")
    @Description("Add a new component.")
    public void postComponents() {
        postTestingComponentAndAddToRemoveList();
    }


}
