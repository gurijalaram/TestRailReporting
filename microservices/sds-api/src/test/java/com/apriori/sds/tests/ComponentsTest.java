package com.apriori.sds.tests;

import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.Component;
import com.apriori.sds.entity.response.ComponentsItemsResponse;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class ComponentsTest extends SDSTestUtil {

    @Test
    @TestRail(id = 6937)
    @Description("Find components for a customer matching a specified query.")
    public void getComponents() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_COMPONENTS, ComponentsItemsResponse.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = 6938)
    @Description("Get the current representation of a component.")
    public void getComponentByIdentity() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_COMPONENT_SINGLE_BY_IDENTITY, Component.class)
                .inlineVariables(getComponentId())
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = 7248)
    @Description("Add a new component.")
    public void postComponents() {
        postTestingComponentAndAddToRemoveList();
    }
}
