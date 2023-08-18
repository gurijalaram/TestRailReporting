package com.apriori.evaluate;

import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.response.CostingTemplate;
import com.apriori.cidappapi.models.response.componentiteration.AnalysisOfScenario;
import com.apriori.cidappapi.models.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.models.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.IterationsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.NewCostingLabelEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ReCostScenarioTests {

    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final IterationsUtil iterationsUtil = new IterationsUtil();
    private SoftAssertions softAssertions;

    @Test
    @Tag(SMOKE)
    @TestRail(id = 6101)
    @Description("Test recosting a cad file - Gear Making")
    public void testRecostGearMaking() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        final String componentName = "Case_011_-_Team_350385";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final UserCredentials currentUser = UserUtil.getUser();

        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile)
                .user(currentUser)
                .build());

        postCostScenario(processGroupEnum, componentName, scenarioName, currentUser, componentResponse, DigitalFactoryEnum.APRIORI_USA);

        ResponseWrapper<ComponentIteration> componentIterationResponse = getComponentIterationResponseWrapper(componentResponse);

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(analysisOfScenario.getProcessRoutingName()).contains("Material Stock");

        postCostScenario(processGroupEnum, componentName, scenarioName, currentUser, componentResponse, DigitalFactoryEnum.APRIORI_BRAZIL);

        ScenarioResponse scenarioRepresentation = getScenarioResponseResponseWrapper(componentResponse);

        softAssertions.assertThat(scenarioRepresentation.getScenarioState()).isEqualTo(NewCostingLabelEnum.COST_COMPLETE.name());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 6102)
    @Description("Test recosting a cad file - Machining Contouring")
    public void testRecostMachiningContouring() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "case_002_00400016-003M10_A";
        final String componentExtension = ".STP";

        uploadCostScenarioAndAssert(processGroupEnum, componentName, componentExtension, "4 Axis Mill", DigitalFactoryEnum.APRIORI_CHINA);
    }

    @Test
    @TestRail(id = {6103})
    @Description("Test recosting a cad file - Partially Automated Machining")
    public void testRecostPartiallyAutomatedMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "14100640";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final UserCredentials currentUser = UserUtil.getUser();

        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile)
                .user(currentUser)
                .build());

        postCostScenario(processGroupEnum, componentName, scenarioName, currentUser, componentResponse, DigitalFactoryEnum.APRIORI_USA);

        ResponseWrapper<ComponentIteration> componentIterationResponse = getComponentIterationResponseWrapper(componentResponse);

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(analysisOfScenario.getProcessRoutingName()).contains("3 Axis Mill");

        postCostScenario(processGroupEnum, componentName, scenarioName, currentUser, componentResponse, DigitalFactoryEnum.APRIORI_BRAZIL);

        ScenarioResponse scenarioRepresentation = getScenarioResponseResponseWrapper(componentResponse);

        softAssertions.assertThat(scenarioRepresentation.getScenarioState()).isEqualTo(NewCostingLabelEnum.COST_COMPLETE.name());
    }

    @Test
    @TestRail(id = {6104})
    @Description("Test recosting a cad file - Pocket Recognition")
    public void testRecostPocketRecognition() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "case_010_lam_15-435508-00";
        final String componentExtension = ".prt.1";

        uploadCostScenarioAndAssert(processGroupEnum, componentName, componentExtension, "Band Saw", DigitalFactoryEnum.APRIORI_BRAZIL);
    }

    @Test
    @TestRail(id = {6105})
    @Description("Test recosting a cad file - Shared Walls")
    public void testRecostSharedWalls() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "case_066_SpaceX_00128711-001_A";
        final String componentExtension = ".stp";

        uploadCostScenarioAndAssert(processGroupEnum, componentName, componentExtension, "Band Saw", DigitalFactoryEnum.APRIORI_BRAZIL);
    }

    @Test
    @TestRail(id = {6106})
    @Description("Test recosting a cad file - Slot Examples")
    public void testRecostSlotExamples() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "case_007_SpaceX_00088481-001_C";
        final String componentExtension = ".stp";

        uploadCostScenarioAndAssert(processGroupEnum, componentName, componentExtension, "Material Stock", DigitalFactoryEnum.APRIORI_BRAZIL);
    }

    private void uploadCostScenarioAndAssert(final ProcessGroupEnum processGroupEnum, final String componentName, final String extension, final String processRoutingName, final DigitalFactoryEnum digitalFactoryEnum) {

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + extension);

        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile)
                .user(currentUser)
                .build());

        scenariosUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .costingTemplate(CostingTemplate.builder()
                    .processGroupName(processGroupEnum.getProcessGroup())
                    .build())
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentIdentity(componentResponse.getComponentIdentity())
                .scenarioIdentity(componentResponse.getScenarioIdentity())
                .processGroup(processGroupEnum)
                .user(currentUser)
                .build());

        ResponseWrapper<ComponentIteration> componentIterationResponse = getComponentIterationResponseWrapper(componentResponse);

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(analysisOfScenario.getProcessRoutingName()).contains(processRoutingName);

        postCostScenario(processGroupEnum, componentName, scenarioName, currentUser, componentResponse, digitalFactoryEnum);

        ScenarioResponse scenarioRepresentation = getScenarioResponseResponseWrapper(componentResponse);

        softAssertions.assertThat(scenarioRepresentation.getScenarioState()).isEqualTo(NewCostingLabelEnum.COST_COMPLETE.name());

        softAssertions.assertAll();
    }

    private void postCostScenario(ProcessGroupEnum processGroupEnum, String componentName, String scenarioName, UserCredentials currentUser, ComponentInfoBuilder componentInfo, DigitalFactoryEnum digitalFactory) {
        scenariosUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentIdentity(componentInfo.getComponentIdentity())
                .scenarioIdentity(componentInfo.getScenarioIdentity())
                .costingTemplate(CostingTemplate.builder()
                    .processGroupName(processGroupEnum.getProcessGroup())
                    .vpeName(digitalFactory.getDigitalFactory())
                    .build())
                .user(currentUser)
                .build());
    }

    private ScenarioResponse getScenarioResponseResponseWrapper(ComponentInfoBuilder componentInfo) {
        return scenariosUtil.getScenarioCompleted(componentInfo);
    }

    private ResponseWrapper<ComponentIteration> getComponentIterationResponseWrapper(ComponentInfoBuilder componentInfo) {
        return iterationsUtil.getComponentIterationLatest(componentInfo);
    }
}