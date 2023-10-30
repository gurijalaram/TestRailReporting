package com.apriori.evaluate;

import com.apriori.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.request.ForkRequest;
import com.apriori.cidappapi.models.response.Scenario;
import com.apriori.cidappapi.models.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class ScenariosTests {

    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private SoftAssertions softAssertions;

    @Test
    @TestRail(id = 10620)
    @Description("Copy a scenario")
    public void testCopyScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        String filename = "oldham.asm.1";
        String componentName = "OLDHAM";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String newScenarioName = new GenerateStringUtil().generateScenarioName();
        UserCredentials currentUser = UserUtil.getUser();
        File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, filename);

        ComponentInfoBuilder postComponentResponse = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ResponseWrapper<Scenario> copyScenarioResponse = scenariosUtil.postCopyScenario(ComponentInfoBuilder
            .builder()
            .scenarioName(newScenarioName)
            .componentIdentity(postComponentResponse.getComponentIdentity())
            .scenarioIdentity(postComponentResponse.getScenarioIdentity())
            .user(currentUser)
            .build());

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(copyScenarioResponse.getResponseEntity().getScenarioName()).isEqualTo(newScenarioName);
        softAssertions.assertThat(copyScenarioResponse.getResponseEntity().getLastAction()).isEqualTo("COPY");

        //Rechecking the original scenario has not changed
        ScenarioResponse scenarioRepresentation = scenariosUtil.getScenarioCompleted(postComponentResponse);

        softAssertions.assertThat(scenarioRepresentation.getScenarioName()).isEqualTo(scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10731, 10730, 10810, 10823})
    @Description("Upload, publish subcomponents and assembly then Edit the Assembly, shallow basis")
    public void testUploadPublishingAndEditAssemblyShallow() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        Scenario editAssemblyResponse = scenariosUtil.postEditScenario(
                componentAssembly,
                ForkRequest.builder()
                    .override(false)
                    .build())
            .getResponseEntity();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(editAssemblyResponse.getLastAction()).isEqualTo("FORK");
        softAssertions.assertThat(editAssemblyResponse.getPublished()).isFalse();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10758})
    @Description("Trigger 409 conflict on trying to publish an assembly that has private sub-components")
    public void testUploadPublishingAssemblyError() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        String errorMessage = String.format("All sub-components of scenario '%s' must be published, scenario can not be published", scenarioName);

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        ResponseWrapper<ScenarioResponse> assemblyUploadResponse = assemblyUtils.publishAssemblyExpectError(componentAssembly);

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(assemblyUploadResponse.getBody()).contains(errorMessage);

        softAssertions.assertAll();
    }

    @Test
    @Description("Shallow Edit assembly and scenarios that was cost in CI Design")
    @TestRail(id = {10799, 10768})
    public void testUploadCostPublishAndEditAssembly() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();


        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly);

        assemblyUtils.publishAssembly(componentAssembly);

        Scenario editAssemblyResponse = scenariosUtil.postEditScenario(
                componentAssembly,
                ForkRequest.builder()
                    .build())
            .getResponseEntity();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(editAssemblyResponse.getLastAction()).isEqualTo("FORK");
        softAssertions.assertThat(editAssemblyResponse.getPublished()).isFalse();

        softAssertions.assertAll();
    }
}
