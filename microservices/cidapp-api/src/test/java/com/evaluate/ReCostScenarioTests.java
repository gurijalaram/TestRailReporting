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
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.IterationsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.css.entity.response.ScenarioItem;
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

    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final IterationsUtil iterationsUtil = new IterationsUtil();

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

        ScenarioItem componentResponse = componentsUtil.postComponentQueryCSS(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .user(currentUser)
                .build(),
            resourceFile);

        postCostScenario(processGroupEnum, componentName, scenarioName, currentUser, componentResponse, DigitalFactoryEnum.APRIORI_USA);

        ResponseWrapper<ComponentIteration> componentIterationResponse = getComponentIterationResponseWrapper(currentUser, componentResponse);

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        assertThat(analysisOfScenario.getProcessRoutingName(), containsString("Material Stock"));

        postCostScenario(processGroupEnum, componentName, scenarioName, currentUser, componentResponse, DigitalFactoryEnum.APRIORI_BRAZIL);

        ResponseWrapper<ScenarioResponse> scenarioRepresentation = getScenarioResponseResponseWrapper(currentUser, componentResponse);

        assertThat(scenarioRepresentation.getResponseEntity().getScenarioState(), is(equalTo(NewCostingLabelEnum.COST_COMPLETE.name())));
    }

    @Test
    @TestRail(testCaseId = {"6102"})
    @Description("Test recosting a cad file - Machining Contouring")
    public void testRecostMachiningContouring() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "case_002_00400016-003M10_A";
        final String componentExtension = ".STP";

        uploadCostScenarioAndAssert(processGroupEnum, componentName, componentExtension, "4 Axis Mill", DigitalFactoryEnum.APRIORI_CHINA);
    }

    @Test
    @TestRail(testCaseId = {"6103"})
    @Description("Test recosting a cad file - Partially Automated Machining")
    public void testRecostPartiallyAutomatedMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "14100640";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final UserCredentials currentUser = UserUtil.getUser();

        ScenarioItem componentResponse = componentsUtil.postComponentQueryCSS(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .user(currentUser)
                .build(),
            resourceFile);

        postCostScenario(processGroupEnum, componentName, scenarioName, currentUser, componentResponse, DigitalFactoryEnum.APRIORI_USA);

        ResponseWrapper<ComponentIteration> componentIterationResponse = getComponentIterationResponseWrapper(currentUser, componentResponse);

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        assertThat(analysisOfScenario.getProcessRoutingName(), containsString("3 Axis Mill"));

        postCostScenario(processGroupEnum, componentName, scenarioName, currentUser, componentResponse, DigitalFactoryEnum.APRIORI_BRAZIL);

        ResponseWrapper<ScenarioResponse> scenarioRepresentation = getScenarioResponseResponseWrapper(currentUser, componentResponse);

        assertThat(scenarioRepresentation.getResponseEntity().getScenarioState(), is(equalTo(NewCostingLabelEnum.COST_COMPLETE.name())));
    }

    @Test
    @TestRail(testCaseId = {"6104"})
    @Description("Test recosting a cad file - Pocket Recognition")
    public void testRecostPocketRecognition() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "case_010_lam_15-435508-00";
        final String componentExtension = ".prt.1";

        uploadCostScenarioAndAssert(processGroupEnum, componentName, componentExtension, "Band Saw", DigitalFactoryEnum.APRIORI_BRAZIL);
    }

    @Test
    @TestRail(testCaseId = {"6105"})
    @Description("Test recosting a cad file - Shared Walls")
    public void testRecostSharedWalls() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "case_066_SpaceX_00128711-001_A";
        final String componentExtension = ".stp";

        uploadCostScenarioAndAssert(processGroupEnum, componentName, componentExtension, "Band Saw", DigitalFactoryEnum.APRIORI_BRAZIL);
    }

    @Test
    @TestRail(testCaseId = {"6106"})
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

        ScenarioItem componentResponse = componentsUtil.postComponentQueryCSS(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .user(currentUser)
                .build(),
            resourceFile);

        scenariosUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentIdentity(componentResponse.getComponentIdentity())
                .scenarioIdentity(componentResponse.getScenarioIdentity())
                .processGroup(processGroupEnum)
                .material("Use Default")
                .mode("manual")
                .user(currentUser)
                .build());

        ResponseWrapper<ComponentIteration> componentIterationResponse = getComponentIterationResponseWrapper(currentUser, componentResponse);

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        assertThat(analysisOfScenario.getProcessRoutingName(), containsString(processRoutingName));

        postCostScenario(processGroupEnum, componentName, scenarioName, currentUser, componentResponse, digitalFactoryEnum);

        ResponseWrapper<ScenarioResponse> scenarioRepresentation = getScenarioResponseResponseWrapper(currentUser, componentResponse);

        assertThat(scenarioRepresentation.getResponseEntity().getScenarioState(), is(equalTo(NewCostingLabelEnum.COST_COMPLETE.name())));
    }

    private void postCostScenario(ProcessGroupEnum processGroupEnum, String componentName, String scenarioName, UserCredentials currentUser, ScenarioItem componentResponse, DigitalFactoryEnum digitalFactory) {
        scenariosUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentIdentity(componentResponse.getComponentIdentity())
                .scenarioIdentity(componentResponse.getScenarioIdentity())
                .processGroup(processGroupEnum)
                .digitalFactory(digitalFactory)
                .material("Use Default")
                .mode("manual")
                .user(currentUser)
                .build());
    }

    private ResponseWrapper<ScenarioResponse> getScenarioResponseResponseWrapper(UserCredentials currentUser, ScenarioItem componentResponse) {
        return scenariosUtil.getScenarioRepresentation(
            ScenarioRepresentationBuilder.builder()
                .scenarioItem(componentResponse)
                .user(currentUser)
                .build());
    }

    private ResponseWrapper<ComponentIteration> getComponentIterationResponseWrapper(UserCredentials currentUser, ScenarioItem componentResponse) {
        return iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(componentResponse.getComponentIdentity())
                .scenarioIdentity(componentResponse.getScenarioIdentity())
                .user(currentUser)
                .build());
    }
}