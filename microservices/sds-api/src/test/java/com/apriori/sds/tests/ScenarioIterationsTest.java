package com.apriori.sds.tests;

import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.ScenarioHoopsImage;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;

public class ScenarioIterationsTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"8425"})
    @Description("Import a scenario iteration resulting in the creation/modification of components, scenarios and/or iterations.")
    @Ignore
    public void testCreateScenarioIterations() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_SCENARIO_ITERATIONS, null);

        ResponseWrapper<ScenarioHoopsImage> response = HTTP2Request.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, response.getStatusCode());
    }
}
