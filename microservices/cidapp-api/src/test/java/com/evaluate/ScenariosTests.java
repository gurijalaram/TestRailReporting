package com.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.builder.ScenarioRepresentationBuilder;
import com.apriori.cidappapi.entity.request.request.ForkRequest;
import com.apriori.cidappapi.entity.response.Scenario;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ScenariosTests {

    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();

    @Test
    @TestRail(testCaseId = "10620")
    @Description("Copy a scenario")
    public void testCopyScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        String filename = "oldham.asm.1";
        String componentName = "OLDHAM";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String newScenarioName = new GenerateStringUtil().generateScenarioName();
        UserCredentials currentUser = UserUtil.getUser();
        File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, filename);

        ScenarioItem postComponentResponse = componentsUtil.postComponentQueryCSS(ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .user(currentUser)
                .build(),
            resourceFile);

        ResponseWrapper<Scenario> copyScenarioResponse = scenariosUtil.postCopyScenario(ComponentInfoBuilder
            .builder()
            .scenarioName(newScenarioName)
            .componentId(postComponentResponse.getComponentIdentity())
            .scenarioId(postComponentResponse.getScenarioIdentity())
            .user(currentUser)
            .build());

        assertThat(copyScenarioResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(copyScenarioResponse.getResponseEntity().getScenarioName(), is(equalTo(newScenarioName)));
        assertThat(copyScenarioResponse.getResponseEntity().getLastAction(), is(equalTo("COPY")));

        //Rechecking the original scenario has not changed
        ResponseWrapper<ScenarioResponse> scenarioRepresentation = scenariosUtil.getScenarioRepresentation(
            ScenarioRepresentationBuilder.builder()
                .scenarioItem(postComponentResponse)
                .user(currentUser)
                .build());

        assertThat(scenarioRepresentation.getResponseEntity().getScenarioName(), is(equalTo(scenarioName)));
    }

    @Test
    @TestRail(testCaseId = {"10731", "10730", "10810", "10823"})
    @Description("Upload, publish subcomponents and assembly then Edit the Assembly, shallow basis")
    public void testUploadPublishingAndEditAssemblyShallow() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        //Build & process sub component object based on array list of names
        for (String subComponentName : subComponentNames) {
            scenariosUtil.uploadAndPublishComponent(ComponentInfoBuilder.builder()
                .componentName(subComponentName)
                .extension(componentExtension)
                .scenarioName(scenarioName)
                .processGroup(processGroupEnum)
                .user(currentUser)
                .build());
        }

        //Process assembly
        ComponentInfoBuilder myAssembly = ComponentInfoBuilder.builder()
                .componentName(assemblyName)
                .extension(assemblyExtension)
                .scenarioName(scenarioName)
                .processGroup(ProcessGroupEnum.ASSEMBLY)
                .user(currentUser)
                .build();

        ScenarioItem assemblyUploadResponse = scenariosUtil.uploadAndPublishComponent(myAssembly);
        myAssembly.setComponentId(assemblyUploadResponse.getComponentIdentity());
        myAssembly.setScenarioId(assemblyUploadResponse.getScenarioIdentity());

        //Edit Assembly
        Scenario editAssemblyResponse = scenariosUtil.postEditScenario(
                myAssembly,
                ForkRequest.builder()
                    .override(false)
                    .build())
            .getResponseEntity();

        //assertions
        assertThat(editAssemblyResponse.getLastAction(), is("FORK"));
        assertThat(editAssemblyResponse.getPublished(), is(false));
    }

    @Test
    @TestRail(testCaseId = {"10758"})
    @Description("Trigger 409 conflict on trying to publish an assembly that has private sub-components")
    public void testUploadPublishingAssemblyError() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        List<String> subComponentNames = Arrays.asList("big ring",  "Pin", "small ring");
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        //Build & process sub component object based on array list of names
        for (String subComponentName : subComponentNames) {
            uploadComponent(ComponentInfoBuilder.builder()
                .componentName(subComponentName)
                .extension(componentExtension)
                .scenarioName(scenarioName)
                .processGroup(processGroupEnum)
                .user(currentUser)
                .build());
        }

        //Process assembly
        ComponentInfoBuilder myAssembly = ComponentInfoBuilder.builder()
            .componentName(assemblyName)
            .extension(assemblyExtension)
            .scenarioName(scenarioName)
            .processGroup(ProcessGroupEnum.ASSEMBLY)
            .user(currentUser)
            .build();

        ResponseWrapper<ScenarioResponse> assemblyUploadResponse = scenariosUtil.uploadAndPublishComponentError(myAssembly);

        assertThat(assemblyUploadResponse.getStatusCode(), is(HttpStatus.SC_CONFLICT));
        // TODO: 11/02/2022 the scenario name can be passed into the assertion below to capture the full error message
        assertThat(assemblyUploadResponse.getBody(), containsString("scenario can not be published"));
    }

    private Item uploadComponent(ComponentInfoBuilder component) {
        File resourceFile = FileResourceUtil.getCloudFile(component.getProcessGroup(), component.getComponentName() + component.getExtension());

        Item postComponentResponse;
        postComponentResponse = componentsUtil.postComponentQueryCSS(component.getComponentName(), component.getScenarioName(), resourceFile, component.getUser());

        return postComponentResponse;
    }
}
