package com.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.builder.ScenarioRepresentationBuilder;
import com.apriori.cidappapi.entity.response.Scenario;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.css.entity.response.Item;
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
import java.util.ArrayList;
import java.util.Arrays;

public class ScenariosTests {

    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private String scenarioName = new GenerateStringUtil().generateScenarioName();
    private Item postComponentResponse;
    private UserCredentials currentUser;

    @Test
    @TestRail(testCaseId = "10620")
    @Description("Copy a scenario")
    public void testCopyScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        String filename  = "oldham.asm.1";
        String componentName = "OLDHAM";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String newScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, filename);

        Item postComponentResponse = componentsUtil.postComponentQueryCSS(componentName, scenarioName, resourceFile, currentUser);

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
                .item(postComponentResponse)
                .user(currentUser)
                .build());

        assertThat(scenarioRepresentation.getResponseEntity().getScenarioName(), is(equalTo(scenarioName)));
    }

    @Test
    @TestRail(testCaseId = {"10731", "10730","10810","10823"})
    @Description("Upload, publish subcomponents and assembly then Edit the Assembly, shallow basis")
    public void testUploadPublishingAndEditAssemblyShallow() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final ProcessGroupEnum processGroupEnumAsm = ProcessGroupEnum.ASSEMBLY;
        ArrayList<String> subComponentName = new ArrayList<>(Arrays.asList("big ring", "Pin", "small ring"));
        final String componentExtension = ".SLDPRT";
        final String assemblyExtension = ".SLDASM";
        String assemblyName = "Hinge assembly";

        uploadAndPublishComponent(processGroupEnum, subComponentName.get(0), componentExtension);
        uploadAndPublishComponent(processGroupEnum, subComponentName.get(1), componentExtension);
        uploadAndPublishComponent(processGroupEnum, subComponentName.get(2), componentExtension);

        uploadAndPublishComponent(processGroupEnumAsm, assemblyName, assemblyExtension);

        editAssembly();
    }

    private void uploadAndPublishComponent(final ProcessGroupEnum processGroupEnum, final String componentName, final String extension) {
        currentUser = UserUtil.getUser();
        File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + extension);

        postComponentResponse = componentsUtil.postComponentQueryCSS(componentName, scenarioName, resourceFile, currentUser);

        ResponseWrapper<ScenarioResponse> componentPublishResponse =  scenariosUtil.postPublishScenario(postComponentResponse,
            postComponentResponse.getComponentIdentity(),
            postComponentResponse.getScenarioIdentity(),
            currentUser);

        assertThat(componentPublishResponse.getResponseEntity().getLastAction(), is("PUBLISH"));
        assertThat(componentPublishResponse.getResponseEntity().getPublished(), is(true));
    }

    private void editAssembly() {
        ResponseWrapper<Scenario> editAssemblyResponse = scenariosUtil.postEditScenario(ComponentInfoBuilder
            .builder()
            .componentId(postComponentResponse.getComponentIdentity())
            .scenarioId(postComponentResponse.getScenarioIdentity())
            .user(currentUser)
            .build());

        assertThat(editAssemblyResponse.getResponseEntity().getLastAction(), is("FORK"));
    }
}
