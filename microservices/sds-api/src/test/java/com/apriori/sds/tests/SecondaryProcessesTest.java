package com.apriori.sds.tests;

import com.apriori.entity.response.ScenarioItem;
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
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class SecondaryProcessesTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"8435"})
    @Description("GET the applicable secondary processes for the VPE/Process Group. ")
    public void testGetSecondaryProcesses() {
        final ScenarioItem testingComponent = getTestingComponent();
        final String vpeName = "aPriori USA";
        final String pgName = ProcessGroupEnum.CASTING_DIE.getProcessGroup();


        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_SECONDARY_PROCESS_BY_COMPONENT_SCENARIO_IDS_VPE_PG_NAMES, SecondaryProcessesItems.class)
                .inlineVariables(
                    testingComponent.getComponentIdentity(),
                    testingComponent.getScenarioIdentity(),
                    vpeName,
                    pgName
                )
                .urlEncodingEnabled(true)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<SecondaryProcessesItems> response = HTTPRequest.build(requestEntity).get();

        SoftAssertions softAssertions = new SoftAssertions();
        response.getResponseEntity().getItems().forEach(item -> {
            softAssertions.assertThat(item.getPlantName()).isEqualTo(vpeName);
        });

        softAssertions.assertAll();
    }
}