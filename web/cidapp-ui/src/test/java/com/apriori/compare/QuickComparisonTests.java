package com.apriori.compare;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.compare.ComparePage;
import com.apriori.pageobjects.compare.CreateComparePage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import com.google.common.collect.Ordering;
import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuickComparisonTests extends TestBaseUI {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private CreateComparePage createComparePage;
    private ComparePage comparePage;
    private ComponentInfoBuilder cidComponentItemA;
    private ComponentInfoBuilder cidComponentItemB;

    private ScenariosUtil scenarioUtil = new ScenariosUtil();
    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private File resourceFile;
    private SoftAssertions softAssertions = new SoftAssertions();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();

    @AfterEach
    public void deleteScenarios() {

        if (cidComponentItemA != null) {
            scenarioUtil.deleteScenario(cidComponentItemA.getComponentIdentity(), cidComponentItemA.getScenarioIdentity(), currentUser);
            cidComponentItemA = null;

            if (cidComponentItemB != null) {
                scenarioUtil.deleteScenario(cidComponentItemB.getComponentIdentity(), cidComponentItemB.getScenarioIdentity(), currentUser);
                cidComponentItemB = null;
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

        ComponentInfoBuilder part1 = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName1)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ComponentInfoBuilder part2 = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
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

        ComponentInfoBuilder basePart = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
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
            scenarioUtil.getScenario(comparison).getResponseEntity().getUpdatedAt()).collect(Collectors.toList());

        softAssertions.assertThat(Ordering.natural().reverse().isOrdered(comparisonScenarioTimes)).as("Verify Quick Compare scenarios in Date/Time order")
                .isTrue();

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

        ComponentInfoBuilder basePart = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
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
            scenarioUtil.getScenario(comparison).getResponseEntity().getUpdatedAt()).collect(Collectors.toList());

        softAssertions.assertThat(Ordering.natural().reverse().isOrdered(comparisonScenarioTimes)).as("Verify Quick Compare scenarios in Date/Time order")
            .isTrue();

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

        ComponentInfoBuilder basePart = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(baseScenarioName)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ComponentInfoBuilder scenario1 = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName1)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ComponentInfoBuilder scenario2 = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName2)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ComponentInfoBuilder scenario3 = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
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
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String componentName = "auto pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItemA = loginPage.login(currentUser).uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        new ExplorePage(driver).logout();

        cidComponentItemB = loginPage.login(currentUser).uploadComponent(componentName, scenarioName2, resourceFile, currentUser);

        comparePage = new ExplorePage(driver)
            .selectFilter("Recent")
            .multiSelectScenarios("" + componentName + ", " + scenarioName)
            .createComparison()
            .selectQuickComparison();

        softAssertions.assertThat(comparePage.getScenariosInComparison()).contains(componentName.toUpperCase() + "  / " + scenarioName);

        comparePage.modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(componentName, scenarioName)
            .clickScenarioCheckbox(componentName, scenarioName2)
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getListOfBasis()).isEqualTo(0);

        softAssertions.assertAll();
    }
}
