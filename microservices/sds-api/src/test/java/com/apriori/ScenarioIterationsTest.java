package com.apriori;

import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;

public class ScenarioIterationsTest extends SDSTestUtil {

    @Test
    @TestRail(id = {8425})
    @Description("Import a scenario iteration resulting in the creation/modification of components, scenarios and/or iterations.")
    @Ignore
    public void testCreateScenarioIterations() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.POST_SCENARIO_ITERATIONS, null)
                .expectedResponseCode(HttpStatus.SC_CREATED);

        HTTPRequest.build(requestEntity).post();
    }
}
