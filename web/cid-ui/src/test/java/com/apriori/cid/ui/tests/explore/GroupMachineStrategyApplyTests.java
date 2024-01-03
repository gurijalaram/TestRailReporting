package com.apriori.cid.ui.tests.explore;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.cid.ui.pageobjects.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class GroupMachineStrategyApplyTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();
    private ComponentBasicPage componentBasicPage;

    @Test
    @TestRail(id = {15433, 14939})
    @Description("Explore page - Machinable PG can be selected and part can be costed with \"Do not machine this part\" checked")
    public void testMachiningStrategyOptionFromExplorePage() {
        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.FORGING);
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.FORGING);
        componentB.setUser(componentA.getUser());

        loginPage = new CidAppLoginPage(driver);
        componentBasicPage = loginPage.login(componentA.getUser())
            .uploadComponent(componentA)
            .uploadComponent(componentB)
            .refresh()
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName())
            .clickCostButton(ComponentBasicPage.class);

        softAssertions.assertThat(componentBasicPage.isMachineOptionsCheckBoxDisplayed()).isEqualTo(false);

        componentBasicPage.selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING);

        softAssertions.assertThat(componentBasicPage.isMachineOptionsCheckBoxDisplayed()).isEqualTo(true);
        softAssertions.assertThat(componentBasicPage.isMachineOptionsCheckBoxSelected()).isEqualTo(false);

        explorePage = componentBasicPage.selectMachineOptionCheckBox()
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class);

        Arrays.asList(componentA, componentB).forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(componentA.getUser(), COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.COST_INCOMPLETE)).hasSizeGreaterThan(0));

        componentBasicPage = explorePage.refresh()
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName())
            .clickCostButton(ComponentBasicPage.class);

        softAssertions.assertThat(componentBasicPage.isMachineOptionsCheckBoxSelected()).isEqualTo(true);

        explorePage = componentBasicPage.selectMachineOptionCheckBox()
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class);

        Arrays.asList(componentA, componentB).forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(componentA.getUser(), COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.COST_COMPLETE)).hasSizeGreaterThan(0));

        softAssertions.assertAll();
    }
}
