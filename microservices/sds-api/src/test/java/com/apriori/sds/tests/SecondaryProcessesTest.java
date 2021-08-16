package com.apriori.sds.tests;

import com.apriori.css.entity.response.Item;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.SecondaryProcessesItems;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class SecondaryProcessesTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"8435"})
    @Description("GET the applicable secondary processes for the VPE/Process Group. ")
    public void testGetSecondaryProcesses() {

        final Item item = getTestingComponent();

        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SECONDARY_PROCESS_BY_COMPONENT_SCENARIO_IDS_VPE_PG_NAMES, SecondaryProcessesItems.class)
                .inlineVariables(
                    "14H1JJK4L138",
                    "5CJ2AM87A8KB",
                    "aPriori%20USA",
                    "Casting%20-%20Die"

//                    item.getComponentIdentity(),
//                    item.getScenarioIdentity(),
//                    item.getComponentName(),
//                    ProcessGroupEnum.FORGING.getProcessGroup()
                );

//
//https://cid-api.na-1.qa-cid-perf.apriori.net/components/14H1JJK4L138/scenarios/5CJ2AM87A8KB/digital-factories/aPriori%20USA/process-groups/Casting%20-%20Die/secondary-processes
        ResponseWrapper<SecondaryProcessesItems> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }
}