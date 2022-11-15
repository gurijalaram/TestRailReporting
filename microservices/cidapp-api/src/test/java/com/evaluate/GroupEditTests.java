package com.evaluate;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.ITERATION_EQ;
import static com.apriori.entity.enums.CssSearch.LAST_ACTION_EQ;
import static com.apriori.entity.enums.CssSearch.LATEST_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.request.ForkRequest;
import com.apriori.cidappapi.entity.request.PublishRequest;
import com.apriori.cidappapi.entity.response.Scenario;
import com.apriori.cidappapi.entity.response.ScenarioSuccessesFailures;
import com.apriori.cidappapi.entity.response.User;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.PeopleUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.utils.CssComponent;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class GroupEditTests {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private ComponentInfoBuilder componentAssembly;
    private static UserCredentials currentUser;
    private CssComponent cssComponent = new CssComponent();
    private File resourceFile;
    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private SoftAssertions softAssertions = new SoftAssertions();
    private int PUBLIC_WORKSPACE = 0;


    @Before
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(testCaseId = {"10949"})
    @Description("Edit multiple public sub-components with no Private counterparts (Overwrite)")
    public void testGroupEditPublicSubcomponentsOverride() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String DRIVE = "drive";
        final String JOINT = "joint";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        final List<String> subComponentNames = Arrays.asList(STAND, DRIVE, JOINT);
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

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> groupEditResponse = scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest,
            STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        User user = new PeopleUtil().getCurrentUser(currentUser);

        subComponentNames.forEach(subComponent -> cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName)
            .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(user.getCustomAttributes().getWorkspaceId())));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10950"})
    @Description("Edit multiple public sub-components with no Private counterparts (Create)")
    public void testGroupEditPublicSubcomponentsRename() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String DRIVE = "drive";
        final String JOINT = "joint";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        final List<String> subComponentNames = Arrays.asList(STAND, DRIVE, JOINT);
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

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(false)
            .scenarioName(newScenarioName)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> groupEditResponse = scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest,
            STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        groupEditResponse.getResponseEntity().getSuccesses().forEach(o -> softAssertions.assertThat(o.getScenarioName()).isEqualTo(newScenarioName));

        softAssertions.assertAll();
    }

}


