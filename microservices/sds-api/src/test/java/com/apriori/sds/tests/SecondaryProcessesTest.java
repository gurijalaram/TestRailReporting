package com.apriori.sds.tests;

import com.apriori.css.entity.response.Item;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.SecondaryProcessesItems;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

public class SecondaryProcessesTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"8435"})
    @Description("GET the applicable secondary processes for the VPE/Process Group. ")
    public void testGetSecondaryProcesses() {
        final Item testingComponent = getTestingComponent();
        final String vpeName = "aPriori USA";
        final String pgName = ProcessGroupEnum.CASTING_DIE.getProcessGroup();


        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SECONDARY_PROCESS_BY_COMPONENT_SCENARIO_IDS_VPE_PG_NAMES, SecondaryProcessesItems.class)
                .inlineVariables(
                    testingComponent.getComponentIdentity(),
                    testingComponent.getScenarioIdentity(),
                    vpeName,
                    pgName
                )
            .urlEncodingEnabled(true);

        ResponseWrapper<SecondaryProcessesItems> response = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());

        response.getResponseEntity().getItems().forEach(item -> {
            Assert.assertEquals("Response Item should be with the requested vpe name", vpeName, item.getPlantName());
        });
    }
}