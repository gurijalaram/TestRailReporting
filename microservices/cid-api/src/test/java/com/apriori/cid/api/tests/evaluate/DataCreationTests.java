package com.apriori.cid.api.tests.evaluate;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.cid.api.utils.DataCreationUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.rules.TestRulesAPI;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class DataCreationTests {

    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    public void dataCreateTest() {
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent();

        component.setCostingTemplate(CostingTemplate.builder().processGroupName(component.getProcessGroup().getProcessGroup()).build());

        ComponentInfoBuilder data = new DataCreationUtil(component)
            .searchCreateComponent();

        softAssertions.assertThat(data.getScenarioIdentity()).isNotEmpty();
        softAssertions.assertThat(data.getComponentIdentity()).isNotEmpty();

        softAssertions.assertAll();
    }

    @Test
    public void dataCreateCostTest() {
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent();

        component.setCostingTemplate(CostingTemplate.builder().processGroupName(component.getProcessGroup().getProcessGroup()).build());

        ScenarioResponse data = new DataCreationUtil(component)
            .createCostComponent();

        softAssertions.assertThat(data.getScenarioState()).isEqualTo(ScenarioStateEnum.COST_COMPLETE.getState());

        softAssertions.assertAll();
    }

    @Test
    public void dataCreatePublishTest() {
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent();

        component.setCostingTemplate(CostingTemplate.builder().processGroupName(component.getProcessGroup().getProcessGroup()).build());

        ScenarioResponse data = new DataCreationUtil(component)
            .createPublishComponent();

        softAssertions.assertThat(data.getPublished()).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @Tag(API_SANITY)
    public void dataCreateCostPublishTest() {
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent("bracket_basic");

        component.setCostingTemplate(CostingTemplate.builder().processGroupName(component.getProcessGroup().getProcessGroup()).build());

        ScenarioResponse data = new DataCreationUtil(component)
            .createCostPublishComponent();

        softAssertions.assertThat(data.getScenarioState()).isEqualTo(ScenarioStateEnum.COST_COMPLETE.getState());
        softAssertions.assertThat(data.getPublished()).isTrue();

        softAssertions.assertAll();
    }
}
