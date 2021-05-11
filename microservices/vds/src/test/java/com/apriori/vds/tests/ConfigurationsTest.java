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
    @TestRail(testCaseId = {""})
    @Description("Get a list of Access Control Groups for a specific customer.")
    public void getConfigurations() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_CONFIGURATIONS, null);

        HTTP2Request.build(requestEntity).get();
    }

    @Test
    @TestRail(testCaseId = {""})
    @Description("Get a list of Access Control Permissions for a specific customer.")
    public void getConfigurationsByIdentity() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.GET_CONFIGURATIONS_BY_IDENTITY, null)
            .inlineVariables(Collections.singletonList(""));

        HTTP2Request.build(requestEntity).get();
    }

    @Test
    @TestRail(testCaseId = {""})
    @Description("Post synchronize the access controls for this customer. ")
    public void postSynchronize() {

    }
}
