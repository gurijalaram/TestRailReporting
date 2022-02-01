package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.configuration.Configuration;
import com.apriori.vds.entity.response.configuration.ConfigurationsItems;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

public class ConfigurationTest extends VDSTestUtil {

    @Test
    @TestRail(testCaseId = {"7929"})
    @Description("Returns a list of CustomerConfigurations for a customer.")
    public void getConfigurations() {
        this.getConfigurationsItems();
    }

    @Test
    @TestRail(testCaseId = {"7930"})
    @Description("Get a specific CustomerConfiguration.")
    public void getConfigurationsByIdentity() {
        ConfigurationsItems configurationsItems = this.getConfigurationsItems();

        Assert.assertNotEquals("To get Configuration, response should contain it.", 0, configurationsItems.getItems().size());

        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_CONFIGURATIONS_BY_IDENTITY, Configuration.class)
            .inlineVariables(configurationsItems.getItems().get(0).getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTPRequest.build(requestEntity).get().getStatusCode()
        );
    }

    @Test
    @TestRail(testCaseId = {"7931"})
    @Description("Replaces a CustomerConfiguration for a customer. Creates it if it is missing.")
    @Ignore
    public void putConfiguration() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.PUT_CONFIGURATION, null)
            .headers(new HashMap<String, String>() {{
                    put("Content-Type", "application/json");
                }
            })
            .customBody(
                "{ \"customerConfiguration\": { \"configurationType\": \"ACCESS_CONTROL\", \"serializationType\": \"COMPRESSED_BINARY\", \"customerIdentity\" : \"8GFDIG229629\" }}"
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED,
            HTTPRequest.build(requestEntity).put().getStatusCode()
        );
    }

    private ConfigurationsItems getConfigurationsItems() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_CONFIGURATIONS, ConfigurationsItems.class);

        ResponseWrapper<ConfigurationsItems> configurationsItemsResponse = HTTPRequest.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            configurationsItemsResponse.getStatusCode()
        );

        return configurationsItemsResponse.getResponseEntity();
    }
}

