package com.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.builder.ScenarioRepresentationBuilder;
import com.apriori.cidappapi.entity.response.componentiteration.AnalysisOfScenario;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.CidAppTestUtil;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterfaces.SmokeTests;

import java.io.File;

public class ReCostScenarioTests {

    private final CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6101"})
    @Description("Test recosting a cad file - Gear Making")
    public void testRecostGearMaking() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        final String componentName = "Case_011_-_Team_350385";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final UserCredentials currentUser = UserUtil.getUser();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        cidAppTestUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentId(componentResponse.getComponentIdentity())
                .scenarioId(componentResponse.getScenarioIdentity())
                .processGroup(processGroupEnum)
                .digitalFactory(DigitalFactoryEnum.APRIORI_USA)
                .material("Use Default")
                .mode("manual")
                .user(currentUser)
                .build());

        ResponseWrapper<ComponentIteration> componentIterationResponse = cidAppTestUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentId(componentResponse.getComponentIdentity())
                .scenarioId(componentResponse.getScenarioIdentity())
                .user(currentUser)
                .build());

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        assertThat(analysisOfScenario.getProcessRoutingName(), containsString("Material Stock"));

        cidAppTestUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentId(componentResponse.getComponentIdentity())
                .scenarioId(componentResponse.getScenarioIdentity())
                .processGroup(processGroupEnum)
                .digitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL)
                .material("Use Default")
                .mode("manual")
                .user(currentUser)
                .build());

        ResponseWrapper<ScenarioResponse> scenarioRepresentation = cidAppTestUtil.getScenarioRepresentation(
            ScenarioRepresentationBuilder.builder()
                .item(componentResponse)
                .user(currentUser)
                .build());

        assertThat(scenarioRepresentation.getResponseEntity().getScenarioState(), is(equalTo(NewCostingLabelEnum.COST_COMPLETE.name())));
    }

    @Test
    @TestRail(testCaseId = {"6102"})
    @Description("Test recosting a cad file - Machining Contouring")
    public void testRecostMachiningContouring() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        final String componentName = "case_002_00400016-003M10_A";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".STP");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final UserCredentials currentUser = UserUtil.getUser();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        cidAppTestUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentId(componentResponse.getComponentIdentity())
                .scenarioId(componentResponse.getScenarioIdentity())
                .processGroup(processGroupEnum)
                .material("Use Default")
                .mode("manual")
                .user(currentUser)
                .build());

        ResponseWrapper<ComponentIteration> componentIterationResponse = cidAppTestUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentId(componentResponse.getComponentIdentity())
                .scenarioId(componentResponse.getScenarioIdentity())
                .user(currentUser)
                .build());

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        assertThat(analysisOfScenario.getProcessRoutingName(), containsString("4 Axis Mill"));

        cidAppTestUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentId(componentResponse.getComponentIdentity())
                .scenarioId(componentResponse.getScenarioIdentity())
                .processGroup(processGroupEnum)
                .digitalFactory(DigitalFactoryEnum.APRIORI_CHINA)
                .material("Use Default")
                .mode("manual")
                .user(currentUser)
                .build());

        ResponseWrapper<ScenarioResponse> scenarioRepresentation = cidAppTestUtil.getScenarioRepresentation(
            ScenarioRepresentationBuilder.builder()
                .item(componentResponse)
                .user(currentUser)
                .build());

        assertThat(scenarioRepresentation.getResponseEntity().getScenarioState(), is(equalTo(NewCostingLabelEnum.COST_COMPLETE.name())));
    }

//    @Test
//    @TestRail(testCaseId = {"6103"})
//    @Description("Test recosting a cad file - Partially Automated Machining")
//    public void testRecostPartiallyAutomatedMachining() {
//        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
//
//        String componentName = "14100640";
//        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
//        String scenarioName = new GenerateStringUtil().generateScenarioName();
//        currentUser = UserUtil.getUser();
//
//        loginPage = new CidAppLoginPage(driver);
//        evaluatePage = loginPage.login(currentUser)
//            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
//            .selectProcessGroup(processGroupEnum)
//            .selectDigitalFactory(APRIORI_USA)
//            .costScenario();
//
//        MatcherAssert.assertThat(evaluatePage.getProcessRoutingDetails(), containsString("3 Axis Mill"));
//
//        evaluatePage.selectDigitalFactory(APRIORI_BRAZIL)
//            .costScenario();
//
//        MatcherAssert.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
//    }
//
//    @Test
//    @TestRail(testCaseId = {"6104"})
//    @Description("Test recosting a cad file - Pocket Recognition")
//    public void testRecostPocketRecognition() {
//        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
//
//        String componentName = "case_010_lam_15-435508-00";
//        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
//        String scenarioName = new GenerateStringUtil().generateScenarioName();
//        currentUser = UserUtil.getUser();
//
//        loginPage = new CidAppLoginPage(driver);
//        evaluatePage = loginPage.login(currentUser)
//            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
//            .selectProcessGroup(processGroupEnum)
//            .costScenario();
//
//        MatcherAssert.assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Band Saw"));
//
//        evaluatePage.selectDigitalFactory(APRIORI_BRAZIL)
//            .costScenario();
//
//        MatcherAssert.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
//    }
//
//    @Test
//    @TestRail(testCaseId = {"6105"})
//    @Description("Test recosting a cad file - Shared Walls")
//    public void testRecostSharedWalls() {
//        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
//
//        String componentName = "case_066_SpaceX_00128711-001_A";
//        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
//        String scenarioName = new GenerateStringUtil().generateScenarioName();
//        currentUser = UserUtil.getUser();
//
//        loginPage = new CidAppLoginPage(driver);
//        evaluatePage = loginPage.login(currentUser)
//            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
//            .selectProcessGroup(processGroupEnum)
//            .costScenario();
//
//        MatcherAssert.assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Band Saw"));
//
//        evaluatePage.selectDigitalFactory(APRIORI_BRAZIL)
//            .costScenario();
//
//        MatcherAssert.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
//    }
//
//    @Test
//    @TestRail(testCaseId = {"6106"})
//    @Description("Test recosting a cad file - Slot Examples")
//    public void testRecostSlotExamples() {
//        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
//
//        String componentName = "case_007_SpaceX_00088481-001_C";
//        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
//        String scenarioName = new GenerateStringUtil().generateScenarioName();
//        currentUser = UserUtil.getUser();
//
//        loginPage = new CidAppLoginPage(driver);
//        evaluatePage = loginPage.login(currentUser)
//            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
//            .selectProcessGroup(processGroupEnum)
//            .costScenario();
//
//        MatcherAssert.assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Material Stock"));
//
//        evaluatePage.selectDigitalFactory(APRIORI_BRAZIL)
//            .costScenario();
//
//        MatcherAssert.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
//    }
}