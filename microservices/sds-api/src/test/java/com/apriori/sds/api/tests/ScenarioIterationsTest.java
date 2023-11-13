package com.apriori.sds.api.tests;

import com.apriori.sds.api.enums.SDSAPIEnum;
import com.apriori.sds.api.util.SDSTestUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ScenarioIterationsTest extends SDSTestUtil {

    @Test
    @TestRail(id = {8425})
    @Description("Import a scenario iteration resulting in the creation/modification of components, scenarios and/or iterations.")
    @Disabled
    public void testCreateScenarioIterations() {
        final RequestEntity requestEntity =
            requestEntityUtil.init(SDSAPIEnum.POST_SCENARIO_ITERATIONS, null)
                .expectedResponseCode(HttpStatus.SC_CREATED);

        HTTPRequest.build(requestEntity).post();
    }
}
