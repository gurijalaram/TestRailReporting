package com.apriori.bcs.api.tests;

import com.apriori.bcs.api.controller.CustomerResources;
import com.apriori.bcs.api.enums.BCSAPIEnum;
import com.apriori.bcs.api.models.response.CustomAttributes;
import com.apriori.bcs.api.models.response.CustomerVPE;
import com.apriori.bcs.api.models.response.DigitalFactories;
import com.apriori.bcs.api.models.response.ProcessGroups;
import com.apriori.bcs.api.models.response.UserDefinedAttributes;
import com.apriori.bcs.api.models.response.UserPreferences;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CustomerResourcesTest extends TestUtil {

    private static SoftAssertions softAssertions;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = 4169)
    @Description("API returns a list of tolerances associated with a specific CIS customer")
    public void getUserPreferences() {
        HTTPRequest.build(
            CustomerResources.getCustomerRequestEntity(
                BCSAPIEnum.CUSTOMER_USER_PREFERENCES,
                UserPreferences.class)
        ).get();
    }

    @Test
    @TestRail(id = 4171)
    @Description("API returns a list of process groups associated with a specific CIS customer")
    public void getProcessGroups() {
        ResponseWrapper<ProcessGroups> processGroupsResponse = HTTPRequest.build(
            CustomerResources.getCustomerRequestEntity(
                BCSAPIEnum.CUSTOMER_PROCESS_GROUPS,
                ProcessGroups.class)
        ).get();

        softAssertions.assertThat(processGroupsResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = 4172)
    @Description("API returns a list of user defined attributes associated with a specific CIS customer")
    public void getUserDefinedAttributes() {
        ResponseWrapper<UserDefinedAttributes> userDefinedAttributesResponse = HTTPRequest.build(
            CustomerResources.getCustomerRequestEntity(
                BCSAPIEnum.CUSTOMER_USER_DEFINED_ATTRIBUTES,
                UserDefinedAttributes.class)
        ).get();

        softAssertions.assertThat(userDefinedAttributesResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = 4173)
    @Description("API returns a list of VPEs associated with a specific CIS customer")
    public void getVirtualProductionEnvironments() {
        ResponseWrapper<CustomerVPE> customerVPEResponse = HTTPRequest.build(
            CustomerResources.getCustomerRequestEntity(
                BCSAPIEnum.CUSTOMER_VPEs,
                CustomerVPE.class)
        ).get();

        softAssertions.assertThat(customerVPEResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = 4170)
    @Description("Update a customer's costing preferences")
    public void patchUserPreferences() {
        CustomerResources.patchCostingPreferences();
    }

    @Test
    @TestRail(id = 7955)
    @Description("Return a list of digital factories")
    public void getDigitalFactories() {
        ResponseWrapper<DigitalFactories> digitalFactoriesResponse = HTTPRequest.build(
            CustomerResources.getCustomerRequestEntity(
                BCSAPIEnum.CUSTOMER_DIGITAL_FACTORIES,
                DigitalFactories.class)
        ).get();

        softAssertions.assertThat(digitalFactoriesResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = 7956)
    @Description("Return a customer's custom attributes")
    public void getCustomAttributes() {
        ResponseWrapper<CustomAttributes> customAttributesResponse = HTTPRequest.build(
            CustomerResources.getCustomerRequestEntity(
                BCSAPIEnum.CUSTOMER_CUSTOM_ATTRIBUTES,
                CustomAttributes.class)
        ).get();

        softAssertions.assertThat(customAttributesResponse.getResponseEntity().getItems().length).isGreaterThan(0);
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
    }
}