package com.apriori;

import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.ScenarioItem;
import com.apriori.rules.TestRulesApi;
import com.apriori.sds.enums.SDSAPIEnum;
import com.apriori.sds.models.response.SecondaryProcessesItems;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesApi.class)
public class SecondaryProcessesTest extends SDSTestUtil {

    @Test
    @TestRail(id = {8435})
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