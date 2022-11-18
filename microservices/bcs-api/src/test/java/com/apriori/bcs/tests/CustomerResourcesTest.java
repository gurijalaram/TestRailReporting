package com.apriori.bcs.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.apriori.bcs.controller.CustomerResources;
import com.apriori.bcs.entity.response.CustomAttributes;
import com.apriori.bcs.entity.response.CustomerVPE;
import com.apriori.bcs.entity.response.DigitalFactories;
import com.apriori.bcs.entity.response.ProcessGroups;
import com.apriori.bcs.entity.response.UserDefinedAttributes;
import com.apriori.bcs.entity.response.UserPreferences;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.junit.Test;

public class CustomerResourcesTest {

    @Test
    @TestRail(testCaseId = {"4169"})
    @Description("API returns a list of tolerances associated with a specific CIS customer")
    public void getUserPreferences() {
        HTTPRequest.build(
            CustomerResources.getCustomerRequestEntity(
                BCSAPIEnum.CUSTOMER_USER_PREFERENCES,
                UserPreferences.class)
        ).get();
    }

    @Test
    @TestRail(testCaseId = {"4171"})
    @Description("API returns a list of process groups associated with a specific CIS customer")
    public void getProcessGroups() {
        ResponseWrapper<ProcessGroups> processGroupsResponse = HTTPRequest.build(
            CustomerResources.getCustomerRequestEntity(
                BCSAPIEnum.CUSTOMER_PROCESS_GROUPS,
                ProcessGroups.class)
        ).get();

        assertNotEquals(processGroupsResponse.getResponseEntity().getItems().size(), 0);
    }

    @Test
    @TestRail(testCaseId = {"4172"})
    @Description("API returns a list of user defined attributes associated with a specific CIS customer")
    public void getUserDefinedAttributes() {
        ResponseWrapper<UserDefinedAttributes> userDefinedAttributesResponse = HTTPRequest.build(
            CustomerResources.getCustomerRequestEntity(
                BCSAPIEnum.CUSTOMER_USER_DEFINED_ATTRIBUTES,
                UserDefinedAttributes.class)
        ).get();

        assertEquals(userDefinedAttributesResponse.getResponseEntity().getItems().size(), 0);
    }

    @Test
    @TestRail(testCaseId = {"4173"})
    @Description("API returns a list of VPEs associated with a specific CIS customer")
    public void getVirtualProductionEnvironments() {
        ResponseWrapper<CustomerVPE> customerVPEResponse = HTTPRequest.build(
            CustomerResources.getCustomerRequestEntity(
                BCSAPIEnum.CUSTOMER_VPEs,
                CustomerVPE.class)
        ).get();

        assertNotEquals(customerVPEResponse.getResponseEntity().getItems().size(), 0);
    }

    @Test
    @TestRail(testCaseId = {"4170"})
    @Description("Update a customer's costing preferences")
    public void patchUserPreferences() {
        CustomerResources.patchCostingPreferences();
    }

    @Test
    @TestRail(testCaseId = {"7955"})
    @Description("Return a list of digital factories")
    public void getDigitalFactories() {
        ResponseWrapper<DigitalFactories> digitalFactoriesResponse = HTTPRequest.build(
            CustomerResources.getCustomerRequestEntity(
                BCSAPIEnum.CUSTOMER_DIGITAL_FACTORIES,
                DigitalFactories.class)
        ).get();

        assertNotEquals(digitalFactoriesResponse.getResponseEntity().getItems().size(), 0);
    }

    @Test
    @TestRail(testCaseId = {"7956"})
    @Description("Return a customer's custom attributes")
    public void getCustomAttributes() {
        ResponseWrapper<CustomAttributes> customAttributesResponse = HTTPRequest.build(
            CustomerResources.getCustomerRequestEntity(
                BCSAPIEnum.CUSTOMER_CUSTOM_ATTRIBUTES,
                CustomAttributes.class)
        ).get();

        assertEquals(customAttributesResponse.getResponseEntity().getItems().length, 0);
    }
}