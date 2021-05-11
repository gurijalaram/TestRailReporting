package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.junit.Test;

import java.util.Collections;

public class ConfigurationsTest extends VDSTestUtil {

    @Test
    @TestRail(testCaseId = {"7929"})
    @Description("Returns a list of CustomerConfigurations for a customer.")
    public void getConfigurations() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_CONFIGURATIONS, null);

        HTTP2Request.build(requestEntity).get();
    }

    @Test
    @TestRail(testCaseId = {"7930"})
    @Description("Get a specific CustomerConfiguration.")
    public void getConfigurationsByIdentity() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.GET_CONFIGURATIONS_BY_IDENTITY, null)
            .inlineVariables(Collections.singletonList(""));

        HTTP2Request.build(requestEntity).get();
    }

    @Test
    @TestRail(testCaseId = {"7931"})
    @Description("Replaces a CustomerConfiguration for a customer. Creates it if it is missing.")
    public void putConfiguration() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.PUT_CONFIGURATION, null)
                .inlineVariables(Collections.singletonList(""));

        HTTP2Request.build(requestEntity).get();
    }
}
