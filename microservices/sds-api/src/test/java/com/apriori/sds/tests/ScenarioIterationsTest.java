package com.apriori.sds.tests;

import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;

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
            RequestEntityUtil.init(SDSAPIEnum.POST_SCENARIO_ITERATIONS, null)
                .expectedResponseCode(HttpStatus.SC_CREATED);

        HTTPRequest.build(requestEntity).post();
    }
}
