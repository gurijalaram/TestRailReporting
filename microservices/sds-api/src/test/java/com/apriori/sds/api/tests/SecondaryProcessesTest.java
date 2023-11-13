package com.apriori.sds.api.tests;

import com.apriori.sds.api.enums.SDSAPIEnum;
import com.apriori.sds.api.models.response.SecondaryProcessesItems;
import com.apriori.sds.api.util.SDSTestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class SecondaryProcessesTest extends SDSTestUtil {

    @Test
    @TestRail(id = {8435})
    @Description("GET the applicable secondary processes for the VPE/Process Group. ")
    public void testGetSecondaryProcesses() {
        final ScenarioItem testingComponent = getTestingComponent();
        final String vpeName = "aPriori USA";
        final String pgName = ProcessGroupEnum.CASTING_DIE.getProcessGroup();


        final RequestEntity requestEntity =
            requestEntityUtil.init(SDSAPIEnum.GET_SECONDARY_PROCESS_BY_COMPONENT_SCENARIO_IDS_VPE_PG_NAMES, SecondaryProcessesItems.class)
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