package com.evaluate;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.request.ForkRequest;
import com.apriori.cidappapi.entity.request.PublishRequest;
import com.apriori.cidappapi.entity.response.ScenarioSuccessesFailures;
import com.apriori.cidappapi.entity.response.User;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.PeopleUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.CssComponent;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GroupEditTests {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private CssComponent cssComponent = new CssComponent();
    private ComponentInfoBuilder componentAssembly;
    private UserCredentials currentUser;

    @Test
    @TestRail(testCaseId = {"10949", "11853"})
    @Description("Edit multiple public sub-components with no Private counterparts (Overwrite)")
    public void testGroupEditPublicSubcomponents() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String newScenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String DRIVE = "drive";
        final String JOINT = "joint";
        String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        List<String> subComponentNames = Arrays.asList(STAND, DRIVE, JOINT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".prt.1";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        assemblyUtils.costAssembly(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .scenarioName(newScenarioName)
            .override(true)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> groupEditResponse = scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest,
            STAND + "," + scenarioName, DRIVE + "," + scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        groupEditResponse.getResponseEntity().getSuccesses().forEach(o -> softAssertions.assertThat(o.getScenarioName()).isEqualTo(newScenarioName));

        ResponseWrapper<ScenarioSuccessesFailures> singleEditResponse = scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest,
            JOINT + "," + scenarioName);

        softAssertions.assertThat(singleEditResponse.getResponseEntity().getSuccesses()).isEqualTo(1);
        singleEditResponse.getResponseEntity().getSuccesses().forEach(o -> softAssertions.assertThat(o.getScenarioName()).isEqualTo(newScenarioName));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11851", "11852"})
    @Description("Publish a single private sub-component with no public counterpart")
    public void testGroupPublishPrivateSubcomponent() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String newScenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String DRIVE = "drive";
        String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        List<String> subComponentNames = Arrays.asList(STAND, DRIVE);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".prt.1";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        User user = new PeopleUtil().getCurrentUser(currentUser);

        PublishRequest publishRequest = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial".toUpperCase())
            .override(true)
            .status("New".toUpperCase())
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> standResponse = scenariosUtil.postPublishGroupScenarios(publishRequest, user.getCustomAttributes().getWorkspaceId(),
            componentAssembly, STAND + "," + scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        standResponse.getResponseEntity().getSuccesses().forEach(o -> softAssertions.assertThat(o.getScenarioName()).isEqualTo(scenarioName));

        List<ScenarioItem> scenarioItem = cssComponent.getSimpleCssComponent(STAND, scenarioName, currentUser).getResponseEntity().getItems();

        softAssertions.assertThat(scenarioItem.stream().findFirst().get().getScenarioIterationKey().getWorkspaceId()).isEqualTo(0);

        PublishRequest publishRequest2 = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial".toUpperCase())
            .override(false)
            .status("New".toUpperCase())
            .scenarioName(newScenarioName)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> driveResponse = scenariosUtil.postPublishGroupScenarios(publishRequest2, user.getCustomAttributes().getWorkspaceId(),
            componentAssembly, DRIVE + "," + scenarioName);

        driveResponse.getResponseEntity().getSuccesses().forEach(o -> softAssertions.assertThat(o.getScenarioName()).isEqualTo(scenarioName));

        List<ScenarioItem> scenarioItem2 = cssComponent.getSimpleCssComponent(DRIVE, newScenarioName, currentUser).getResponseEntity().getItems();

        softAssertions.assertThat(scenarioItem2.stream().findFirst().get().getScenarioName()).isEqualTo(newScenarioName);
        softAssertions.assertThat(scenarioItem2.stream().findFirst().get().getScenarioIterationKey().getWorkspaceId()).isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11853"})
    @Description("Publish a multiple private sub-components with no public counterpart")
    public void testGroupPublishPrivateSubcomponents() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String DRIVE = "drive";
        final String JOINT = "joint";
        String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        List<String> subComponentNames = Arrays.asList(STAND, DRIVE, JOINT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".prt.1";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        User user = new PeopleUtil().getCurrentUser(currentUser);

        PublishRequest publishRequest = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial".toUpperCase())
            .override(true)
            .status("New".toUpperCase())
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> publishResponse = scenariosUtil.postPublishGroupScenarios(componentAssembly, publishRequest,
            STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        publishResponse.getResponseEntity().getSuccesses().forEach(o -> softAssertions.assertThat(o.getScenarioName()).isEqualTo(scenarioName));

        List<ScenarioItem> driveItem = cssComponent.getSimpleCssComponent(DRIVE, scenarioName, currentUser).getResponseEntity().getItems();
        softAssertions.assertThat(driveItem.stream().findFirst().get().getScenarioIterationKey().getWorkspaceId()).isEqualTo(0);

        List<ScenarioItem> standItem = cssComponent.getSimpleCssComponent(STAND, scenarioName, currentUser).getResponseEntity().getItems();
        softAssertions.assertThat(standItem.stream().findFirst().get().getScenarioIterationKey().getWorkspaceId()).isEqualTo(0);

        List<ScenarioItem> jointItem = cssComponent.getSimpleCssComponent(JOINT, scenarioName, currentUser).getResponseEntity().getItems();
        softAssertions.assertThat(jointItem.stream().findFirst().get().getScenarioIterationKey().getWorkspaceId()).isEqualTo(0);

        softAssertions.assertAll();
    }
}
