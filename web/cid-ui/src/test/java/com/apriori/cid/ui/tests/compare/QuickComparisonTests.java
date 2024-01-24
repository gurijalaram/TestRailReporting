package com.apriori.cid.ui.tests.compare;

import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cid.ui.pageobjects.compare.ComparePage;
import com.apriori.cid.ui.pageobjects.compare.CreateComparePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.SerializationUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class QuickComparisonTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private CreateComparePage createComparePage;
    private ComparePage comparePage;
    private ComponentInfoBuilder component;
    private ComponentInfoBuilder componentB;
    private ComponentInfoBuilder componentC;
    private ComponentInfoBuilder componentD;

    private ScenariosUtil scenarioUtil = new ScenariosUtil();
    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private SoftAssertions softAssertions = new SoftAssertions();

    @AfterEach
    public void deleteScenarios() {
        Arrays.asList(component, componentB, componentC, componentD).forEach(comp -> {
            if (comp != null) {
                scenarioUtil.deleteScenario(comp.getComponentIdentity(), comp.getScenarioIdentity(), comp.getUser());
            }
        });
    }

    @Test
    @TestRail(id = 24296)
    @Description("Quick Compare option disabled if multiple scenarios selected in Explore view")
    public void validateQuickCompareDisabledForMultiSelect() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);
        componentB = SerializationUtils.clone(component);
        componentB.setScenarioName(new GenerateStringUtil().generateScenarioName());

        loginPage = new CidAppLoginPage(driver);
        createComparePage = loginPage.login(component.getUser())
            .uploadComponent(component)
            .uploadComponent(componentB)
            .selectFilter("Recent")
            .multiSelectScenarios(component.getComponentName() + "," + component.getScenarioName(),
                componentB.getComponentName() + "," + componentB.getScenarioName())
            .createComparison();

        softAssertions.assertThat(createComparePage.quickComparisonButtonEnabled()).as("Verify that Quick comparison is disabled").isFalse();
        softAssertions.assertThat(createComparePage.manualComparisonButtonEnabled()).as("Verify that Manual comparison is enabled").isTrue();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {24294, 24299})
    @Description("User can create a comparison by selection of a single scenario on Evaluate page")
    public void createQuickComparisonFromEvaluate() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .createComparison()
            .selectQuickComparison();

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + component.getScenarioName());

        List<ComponentInfoBuilder> comparisonScenarios = new ArrayList<>();
        comparePage.getScenariosInComparison().forEach(comparison -> {
            List<ScenarioItem> scenarioDetails = componentsUtil.getUnCostedComponent(comparison.split(" / ")[0], comparison.split(" / ")[1], component.getUser());
            ComponentInfoBuilder scenario = ComponentInfoBuilder.builder()
                .scenarioName(comparison.split(" / ")[1])
                .scenarioIdentity(scenarioDetails.get(0).getScenarioIdentity())
                .componentIdentity(scenarioDetails.get(0).getComponentIdentity())
                .componentName(comparison.split(" / ")[0])
                .user(component.getUser())
                .build();
            comparisonScenarios.add(scenario);
        });

        List<String> comparisonScenarioNames = comparePage.getAllScenariosInComparison().stream().skip(1).map(x -> x.split(" / ")[1]).collect(Collectors.toList());
        comparisonScenarioNames.forEach(name -> softAssertions.assertThat(Collections.frequency(comparisonScenarioNames, name))
            .as("Verify Unique Scenarios Compared")
            .isEqualTo(1));

        List<LocalDateTime> comparisonScenarioTimes = comparisonScenarios.stream().map(comparison ->
            scenarioUtil.getScenario(comparison).getUpdatedAt()).collect(Collectors.toList());

        softAssertions.assertThat(comparisonScenarioTimes).isSortedAccordingTo(Comparator.reverseOrder());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {24293, 24298})
    @Description("User can create a comparison by selection of a single scenario on Explore page")
    public void createQuickComparisonFromExplore() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponent(component)
            .selectFilter("Recent")
            .multiSelectScenarios(component.getComponentName() + "," + component.getScenarioName())
            .createComparison()
            .selectQuickComparison();

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + component.getScenarioName());

        List<ComponentInfoBuilder> comparisonScenarios = new ArrayList<ComponentInfoBuilder>();
        comparePage.getScenariosInComparison().forEach(comparison -> {
            List<ScenarioItem> scenarioDetails = componentsUtil.getUnCostedComponent(comparison.split(" / ")[0], comparison.split(" / ")[1], component.getUser());
            ComponentInfoBuilder scenario = ComponentInfoBuilder.builder()
                .scenarioName(comparison.split(" / ")[1])
                .scenarioIdentity(scenarioDetails.get(0).getScenarioIdentity())
                .componentIdentity(scenarioDetails.get(0).getComponentIdentity())
                .componentName(comparison.split(" / ")[0])
                .user(component.getUser())
                .build();
            comparisonScenarios.add(scenario);
        });

        List<String> comparisonScenarioNames = comparePage.getAllScenariosInComparison().stream().skip(1).map(x -> x.split(" / ")[1]).collect(Collectors.toList());
        comparisonScenarioNames.forEach(name -> softAssertions.assertThat(Collections.frequency(comparisonScenarioNames, name))
            .as("Verify Unique Scenarios Compared")
            .isEqualTo(1));

        List<LocalDateTime> comparisonScenarioTimes = comparisonScenarios.stream().map(comparison ->
            scenarioUtil.getScenario(comparison).getUpdatedAt()).collect(Collectors.toList());

        softAssertions.assertThat(comparisonScenarioTimes).isSortedAccordingTo(Comparator.reverseOrder());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {24293, 24294, 24345})
    @Description("Verify that Quick Compare displays most recently edited scenarios for comparison in order")
    public void testQuickCompareScenarioOrdering() {
        component = new ComponentRequestUtil().getComponent();
        componentB = SerializationUtils.clone(component);
        componentB.setScenarioName(new GenerateStringUtil().generateScenarioName());
        componentC = SerializationUtils.clone(component);
        componentC.setScenarioName(new GenerateStringUtil().generateScenarioName());
        componentD = SerializationUtils.clone(component);
        componentD.setScenarioName(new GenerateStringUtil().generateScenarioName());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponent(component)
            .uploadComponent(componentB)
            .uploadComponent(componentC)
            .uploadComponent(componentD)
            .selectFilter("Recent")
            .multiSelectScenarios(component.getComponentName() + "," + component.getScenarioName())
            .createComparison()
            .selectQuickComparison();

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + component.getScenarioName());
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(0)).as("Verify Most Recent Comparison")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + componentD.getScenarioName());
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(1)).as("Verify Most Recent Comparison")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + componentC.getScenarioName());
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(2)).as("Verify Most Recent Comparison")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + componentB.getScenarioName());

        comparePage = comparePage.clickExplore()
            .navigateToScenario(componentC)
            .createComparison()
            .selectQuickComparison();

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + componentC.getScenarioName());
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(0)).as("Verify Most Recent Comparison")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + componentD.getScenarioName());
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(1)).as("Verify Most Recent Comparison")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + componentB.getScenarioName());
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(2)).as("Verify Most Recent Comparison")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + component.getScenarioName());

        componentB.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(componentB.getProcessGroup().getProcessGroup())
                .build()
        );
        scenarioUtil.postCostScenario(componentB);

        comparePage = comparePage.clickExplore()
            .selectFilter("Recent")
            .multiHighlightScenarios(component.getComponentName() + "," + component.getScenarioName())
            .createComparison()
            .selectQuickComparison();

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + component.getScenarioName());
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(0)).as("Verify Most Recent Comparison")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + componentB.getScenarioName());
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(1)).as("Verify Most Recent Comparison")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + componentD.getScenarioName());
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(2)).as("Verify Most Recent Comparison")
            .isEqualTo(component.getComponentName().toUpperCase() + "  / " + componentC.getScenarioName());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {26147})
    @Description("Validate scenarios can be deleted from quick comparison via modify comparison")
    public void testDeleteQuickComparison() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        component = new ComponentRequestUtil().getComponent();
        componentB = SerializationUtils.clone(component);
        componentB.setScenarioName(scenarioName);

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .logout()
            .login(component.getUser())
            .uploadComponentAndOpen(componentB)
            .clickExplore()
            .selectFilter("Recent")
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName())
            .createComparison()
            .selectQuickComparison();

        softAssertions.assertThat(comparePage.getAllScenariosInComparison()).contains(component.getComponentName().toUpperCase() + "  / " + component.getScenarioName());

        comparePage.modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(component.getComponentName(), component.getScenarioName())
            .clickScenarioCheckbox(componentB.getComponentName(), scenarioName)
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getListOfBasis()).isEqualTo(0);

        softAssertions.assertAll();
    }
}
