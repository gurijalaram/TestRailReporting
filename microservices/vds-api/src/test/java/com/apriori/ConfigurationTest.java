package com.apriori;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;
import com.apriori.util.VDSTestUtil;
import com.apriori.vds.enums.VDSAPIEnum;
import com.apriori.vds.models.response.configuration.Configuration;
import com.apriori.vds.models.response.configuration.ConfigurationsItems;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class ConfigurationTest extends VDSTestUtil {

    @Test
    @TestRail(id = {7929})
    @Description("Returns a list of CustomerConfigurations for a customer.")
    public void getConfigurations() {
        this.getConfigurationsItems();
    }

    @Test
    @TestRail(id = {7930})
    @Description("Get a specific CustomerConfiguration.")
    public void getConfigurationsByIdentity() {
        ConfigurationsItems configurationsItems = this.getConfigurationsItems();

        assertNotEquals(0, configurationsItems.getItems().size(), "To get Configuration, response should contain it.");

        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_CONFIGURATIONS_BY_IDENTITY, Configuration.class)
            .inlineVariables(configurationsItems.getItems().get(0).getIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = {7931})
    @Description("Replaces a CustomerConfiguration for a customer. Creates it if it is missing.")
    @Disabled
    public void putConfiguration() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.PUT_CONFIGURATION, null)
            .headers(new HashMap<String, String>() {
                {
                    put("Content-Type", "application/json");
                }
            })
            .customBody(
                "{ \"customerConfiguration\": { \"configurationType\": \"ACCESS_CONTROL\", \"serializationType\": \"COMPRESSED_BINARY\", \"customerIdentity\" : \"8GFDIG229629\" }}"
            )
            .expectedResponseCode(HttpStatus.SC_CREATED);

        HTTPRequest.build(requestEntity).put();
    }

    private ConfigurationsItems getConfigurationsItems() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_CONFIGURATIONS, ConfigurationsItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ConfigurationsItems> configurationsItemsResponse = HTTPRequest.build(requestEntity).get();

        return configurationsItemsResponse.getResponseEntity();
    }
}

