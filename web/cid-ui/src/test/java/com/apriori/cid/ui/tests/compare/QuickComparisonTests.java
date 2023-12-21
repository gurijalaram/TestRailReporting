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
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class QuickComparisonTests extends TestBaseUI {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private CreateComparePage createComparePage;
    private ComparePage comparePage;
    private ComponentInfoBuilder component;
    private ComponentInfoBuilder componentB;

    private ScenariosUtil scenarioUtil = new ScenariosUtil();
    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private File resourceFile;
    private SoftAssertions softAssertions = new SoftAssertions();

    @AfterEach
    public void deleteScenarios() {

        if (component != null) {
            scenarioUtil.deleteScenario(component.getComponentIdentity(), component.getScenarioIdentity(), currentUser);
            component = null;

            if (componentB != null) {
                scenarioUtil.deleteScenario(componentB.getComponentIdentity(), componentB.getScenarioIdentity(), currentUser);
                componentB = null;
            }
        }
    }

    @Test
    @TestRail(id = 24296)
    @Description("Quick Compare option disabled if multiple scenarios selected in Explore view")
    public void validateQuickCompareDisabledForMultiSelect() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName1 = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder part1 = componentsUtil.postComponent(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName1)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ComponentInfoBuilder part2 = componentsUtil.postComponent(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName2)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        loginPage = new CidAppLoginPage(driver);
        createComparePage = loginPage.login(currentUser)
            .multiSelectScenarios(part1.getComponentName() + "," + part1.getScenarioName(),
                part2.getComponentName() + "," + part2.getScenarioName())
            .createComparison();

        softAssertions.assertThat(createComparePage.quickComparisonButtonEnabled()).as("Verify that Quick comparison is disabled").isFalse();
        softAssertions.assertThat(createComparePage.manualComparisonButtonEnabled()).as("Verify that Manual comparison is enabled").isTrue();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {24294, 24299})
    @Description("User can create a comparison by selection of a single scenario on Evaluate page")
    public void createQuickComparisonFromEvaluate() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder basePart = componentsUtil.postComponent(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .navigateToScenario(basePart)
            .createComparison()
            .selectQuickComparison();

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(componentName.toUpperCase() + "  / " + scenarioName);

        List<ComponentInfoBuilder> comparisonScenarios = new ArrayList<>();
        comparePage.getScenariosInComparison().forEach(comparison -> {
            List<ScenarioItem> scenarioDetails = componentsUtil.getUnCostedComponent(comparison.split(" / ")[0], comparison.split(" / ")[1], currentUser);
            ComponentInfoBuilder scenario = ComponentInfoBuilder.builder()
                .scenarioName(comparison.split(" / ")[1])
                .scenarioIdentity(scenarioDetails.get(0).getScenarioIdentity())
                .componentIdentity(scenarioDetails.get(0).getComponentIdentity())
                .componentName(comparison.split(" / ")[0])
                .user(currentUser)
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
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder basePart = componentsUtil.postComponent(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .multiSelectScenarios(basePart.getComponentName() + "," + basePart.getScenarioName())
            .createComparison()
            .selectQuickComparison();

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(componentName.toUpperCase() + "  / " + scenarioName);

        List<ComponentInfoBuilder> comparisonScenarios = new ArrayList<ComponentInfoBuilder>();
        comparePage.getScenariosInComparison().forEach(comparison -> {
            List<ScenarioItem> scenarioDetails = componentsUtil.getUnCostedComponent(comparison.split(" / ")[0], comparison.split(" / ")[1], currentUser);
            ComponentInfoBuilder scenario = ComponentInfoBuilder.builder()
                .scenarioName(comparison.split(" / ")[1])
                .scenarioIdentity(scenarioDetails.get(0).getScenarioIdentity())
                .componentIdentity(scenarioDetails.get(0).getComponentIdentity())
                .componentName(comparison.split(" / ")[0])
                .user(currentUser)
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
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String baseScenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName1 = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String scenarioName3 = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder basePart = componentsUtil.postComponent(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(baseScenarioName)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ComponentInfoBuilder scenario1 = componentsUtil.postComponent(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName1)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ComponentInfoBuilder scenario2 = componentsUtil.postComponent(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName2)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ComponentInfoBuilder scenario3 = componentsUtil.postComponent(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName3)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .multiSelectScenarios(basePart.getComponentName() + "," + basePart.getScenarioName())
            .createComparison()
            .selectQuickComparison();

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(componentName.toUpperCase() + "  / " + baseScenarioName);
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(0)).as("Verify Most Recent Comparison")
            .isEqualTo(componentName.toUpperCase() + "  / " + scenarioName3);
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(1)).as("Verify Most Recent Comparison")
            .isEqualTo(componentName.toUpperCase() + "  / " + scenarioName2);
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(2)).as("Verify Most Recent Comparison")
            .isEqualTo(componentName.toUpperCase() + "  / " + scenarioName1);

        comparePage = comparePage.clickExplore()
            .navigateToScenario(scenario2)
            .createComparison()
            .selectQuickComparison();

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(componentName.toUpperCase() + "  / " + scenarioName2);
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(0)).as("Verify Most Recent Comparison")
            .isEqualTo(componentName.toUpperCase() + "  / " + scenarioName3);
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(1)).as("Verify Most Recent Comparison")
            .isEqualTo(componentName.toUpperCase() + "  / " + scenarioName1);
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(2)).as("Verify Most Recent Comparison")
            .isEqualTo(componentName.toUpperCase() + "  / " + baseScenarioName);

        scenario1.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(scenario1.getProcessGroup().getProcessGroup())
                .build()
        );
        scenarioUtil.postCostScenario(scenario1);

        comparePage = comparePage.clickExplore()
            .multiHighlightScenarios(basePart.getComponentName() + "," + basePart.getScenarioName())
            .createComparison()
            .selectQuickComparison();

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(componentName.toUpperCase() + "  / " + baseScenarioName);
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(0)).as("Verify Most Recent Comparison")
            .isEqualTo(componentName.toUpperCase() + "  / " + scenarioName1);
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(1)).as("Verify Most Recent Comparison")
            .isEqualTo(componentName.toUpperCase() + "  / " + scenarioName3);
        softAssertions.assertThat(comparePage.getScenariosInComparison().get(2)).as("Verify Most Recent Comparison")
            .isEqualTo(componentName.toUpperCase() + "  / " + scenarioName2);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {26147})
    @Description("Validate scenarios can be deleted from quick comparison via modify comparison")
    public void testDeleteQuickComparison() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        component = new ComponentRequestUtil().getComponent();
        componentB = component;
        componentB.setScenarioName(scenarioName);

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component).logout()
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
            .clickScenarioCheckbox(component.getComponentName(), scenarioName)
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getListOfBasis()).isEqualTo(0);

        softAssertions.assertAll();
    }
}
