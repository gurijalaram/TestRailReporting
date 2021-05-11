package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.configuration.Configuration;
import com.apriori.vds.entity.response.configuration.ConfigurationsItems;
import com.apriori.vds.tests.util.VDSRequestEntityUtil;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Collections;

public class ConfigurationTest extends VDSTestUtil {

    @Test
    @TestRail(testCaseId = {"7929"})
    @Description("Returns a list of CustomerConfigurations for a customer.")
    public void getConfigurations() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_CONFIGURATIONS, ConfigurationsItems.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTP2Request.build(requestEntity).get().getStatusCode()
        );
    }

    @Test
    @TestRail(testCaseId = {"7930"})
    @Description("Get a specific CustomerConfiguration.")
    public void getConfigurationsByIdentity() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_CONFIGURATIONS_BY_IDENTITY, Configuration.class)
            .inlineVariables(Collections.singletonList(""));

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTP2Request.build(requestEntity).get().getStatusCode()
        );
    }

    @Test
    @TestRail(testCaseId = {"7931"})
    @Description("Replaces a CustomerConfiguration for a customer. Creates it if it is missing.")
    public void putConfiguration() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.PUT_CONFIGURATION, null)
            .customBody(
            " 'customerConfiguration':{ 'configurationType':'UDAS' }");


        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED,
            HTTP2Request.build(requestEntity).put().getStatusCode()
        );
    }
}
