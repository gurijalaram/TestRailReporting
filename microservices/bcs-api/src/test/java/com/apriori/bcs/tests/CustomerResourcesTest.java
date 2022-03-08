package com.apriori.bcs.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotEquals;

import com.apriori.bcs.controller.CustomerResources;
import com.apriori.bcs.entity.response.CostingPreferences;
import com.apriori.bcs.entity.response.CustomAttributes;
import com.apriori.bcs.entity.response.CustomerVPE;
import com.apriori.bcs.entity.response.DigitalFactories;
import com.apriori.bcs.entity.response.ProcessGroups;
import com.apriori.bcs.entity.response.UserDefinedAttributes;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class CustomerResourcesTest {

    @Test
    @TestRail(testCaseId = {"4169"})
    @Description("API returns a list of tolerances associated with a specific CIS customer")
    public void getCostingPreferences() {
        ResponseWrapper<CostingPreferences> costingPreferencesResponse = HTTPRequest.build(RequestEntityUtil
                .init(BCSAPIEnum.CUSTOMER_COSTING_PREFERENCES, CostingPreferences.class))
            .get();

        assertThat(costingPreferencesResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }

    @Test
    @TestRail(testCaseId = {"4171"})
    @Description("API returns a list of process groups associated with a specific CIS customer")
    public void getProcessGroups() {
        ResponseWrapper<ProcessGroups> processGroupsResponse = HTTPRequest.build(RequestEntityUtil
                .init(BCSAPIEnum.CUSTOMER_PROCESS_GROUPS, ProcessGroups.class))
            .get();
        assertThat(processGroupsResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertNotEquals(processGroupsResponse.getResponseEntity().getItems().size(), 0);
    }

    @Test
    @TestRail(testCaseId = {"4172"})
    @Description("API returns a list of user defined attributes associated with a specific CIS customer")
    public void getUserDefinedAttributes() {
        ResponseWrapper<UserDefinedAttributes> userDefinedAttributesResponse = HTTPRequest.build(RequestEntityUtil
                .init(BCSAPIEnum.CUSTOMER_USER_DEFINED_ATTRIBUTES, UserDefinedAttributes.class))
            .get();
        assertThat(userDefinedAttributesResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertNotEquals(userDefinedAttributesResponse.getResponseEntity().getItems().size(), 0);
    }

    @Test
    @TestRail(testCaseId = {"4173"})
    @Description("API returns a list of VPEs associated with a specific CIS customer")
    public void getVirtualProductionEnvironments() {
        ResponseWrapper<CustomerVPE> customerVPEResponse = HTTPRequest.build(RequestEntityUtil
                .init(BCSAPIEnum.CUSTOMER_VPEs, CustomerVPE.class))
            .get();
        assertThat(customerVPEResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertNotEquals(customerVPEResponse.getResponseEntity().getItems().size(), 0);
    }

    @Test
    @TestRail(testCaseId = {"4170"})
    @Description("Update a customer's costing preferences")
    public void patchCostingPreferences() {
        ResponseWrapper<CostingPreferences> costingPreferencesResponse = CustomerResources.patchCostingPreferences();
        assertThat(costingPreferencesResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }

    @Test
    @TestRail(testCaseId = {"7955"})
    @Description("Return a list of digital factories")
    public void getDigitalFactories() {
        ResponseWrapper<DigitalFactories> digitalFactoriesResponse = HTTPRequest.build(RequestEntityUtil
                .init(BCSAPIEnum.CUSTOMER_DIGITAL_FACTORIES, DigitalFactories.class))
            .get();
        assertThat(digitalFactoriesResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertNotEquals(digitalFactoriesResponse.getResponseEntity().getItems().size(), 0);
    }

    @Test
    @TestRail(testCaseId = {"7956"})
    @Description("Return a customer's custom attributes")
    public void getCustomAttributes() {
        ResponseWrapper<CustomAttributes> customAttributesResponse = HTTPRequest.build(RequestEntityUtil
                .init(BCSAPIEnum.CUSTOMER_CUSTOM_ATTRIBUTES, CustomAttributes.class))
            .get();
        assertThat(customAttributesResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertNotEquals(customAttributesResponse.getResponseEntity().getItems().length, 0);
    }
}