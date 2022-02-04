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

public class ScenariosTests {

    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
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
    @TestRail(testCaseId = "10730")
    @Description("Edit an assembly")
    public void testEditAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        String scenarioName = "Hinge asmthur0302v3";
        String assemblyName = "Hinge assembly";
        currentUser = UserUtil.getUser();

        File assembly = FileResourceUtil.getCloudFile(processGroupEnum, assemblyName + ".SLDASM");

        Item postAssemblyResponse = componentsUtil.postComponentQueryCSS(assemblyName, scenarioName, assembly, currentUser);

        ResponseWrapper<ScenarioResponse> assemblyPublishResponse = scenariosUtil.postPublishScenario(postAssemblyResponse,
            postAssemblyResponse.getComponentIdentity(),
            postAssemblyResponse.getScenarioIdentity(),
            currentUser);

        assertThat(assemblyPublishResponse.getResponseEntity().getLastAction(), is("PUBLISH"));
        assertThat(assemblyPublishResponse.getResponseEntity().getPublished(), is(true));

        ResponseWrapper<Scenario> editAssemblyResponse = scenariosUtil.postEditScenario(ComponentInfoBuilder
            .builder()
            .componentId(postAssemblyResponse.getComponentIdentity())
            .scenarioId(postAssemblyResponse.getScenarioIdentity())
            .user(currentUser)
            .build());

        assertThat(editAssemblyResponse.getResponseEntity().getLastAction(), is("FORK"));
    }

    @Test
    @TestRail(testCaseId = {"10810","10823", "10730"})
    @Description("Upload subcomponents and assembly, Publish all subcomponents and assembly, Edit the assembly")
    public void testEditUploadedSubcomponentsAndAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        String firstSubComponentName = "big ring";
        String secondSubComponentName = "Pin";
        String thirdSubComponentName = "small ring";
        String assemblyName = "Hinge assembly";

        File firstSubComponent = FileResourceUtil.getCloudFile(processGroupEnum, firstSubComponentName + ".SLDPRT");
        File secondSubComponent = FileResourceUtil.getCloudFile(processGroupEnum, secondSubComponentName + ".SLDPRT");
        File thirdSubComponent = FileResourceUtil.getCloudFile(processGroupEnum, thirdSubComponentName + ".SLDPRT");
        File assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        Item postFirstComponentResponse = componentsUtil.postComponentQueryCSS(firstSubComponentName, scenarioName, firstSubComponent, currentUser);
        ResponseWrapper<ScenarioResponse> firstComponentPublishResponse =  scenariosUtil.postPublishScenario(postFirstComponentResponse,
            postFirstComponentResponse.getComponentIdentity(),
            postFirstComponentResponse.getScenarioIdentity(),
            currentUser);

        assertThat(firstComponentPublishResponse.getResponseEntity().getLastAction(), is("PUBLISH"));
        assertThat(firstComponentPublishResponse.getResponseEntity().getPublished(), is(true));

        Item postSecondComponentResponse = componentsUtil.postComponentQueryCSS(secondSubComponentName, scenarioName, secondSubComponent, currentUser);
        ResponseWrapper<ScenarioResponse> secondComponentPublishResponse =  scenariosUtil.postPublishScenario(postSecondComponentResponse,
            postSecondComponentResponse.getComponentIdentity(),
            postSecondComponentResponse.getScenarioIdentity(),
            currentUser);

        assertThat(secondComponentPublishResponse.getResponseEntity().getLastAction(), is("PUBLISH"));
        assertThat(secondComponentPublishResponse.getResponseEntity().getPublished(), is(true));

        Item postThirdComponentResponse = componentsUtil.postComponentQueryCSS(thirdSubComponentName, scenarioName, thirdSubComponent, currentUser);
        ResponseWrapper<ScenarioResponse> thirdComponentPublishResponse =  scenariosUtil.postPublishScenario(postThirdComponentResponse,
            postThirdComponentResponse.getComponentIdentity(),
            postThirdComponentResponse.getScenarioIdentity(),
            currentUser);

        assertThat(thirdComponentPublishResponse.getResponseEntity().getLastAction(), is("PUBLISH"));
        assertThat(thirdComponentPublishResponse.getResponseEntity().getPublished(), is(true));

        Item postAssemblyResponse = componentsUtil.postComponentQueryCSS(assemblyName, scenarioName, assembly, currentUser);

        ResponseWrapper<ScenarioResponse> assemblyPublishResponse = scenariosUtil.postPublishScenario(postAssemblyResponse,
            postAssemblyResponse.getComponentIdentity(),
            postAssemblyResponse.getScenarioIdentity(),
            currentUser);

        assertThat(assemblyPublishResponse.getResponseEntity().getLastAction(), is("PUBLISH"));
        assertThat(assemblyPublishResponse.getResponseEntity().getPublished(), is(true));

        ResponseWrapper<Scenario> editAssemblyResponse = scenariosUtil.postEditScenario(ComponentInfoBuilder
            .builder()
            .componentId(postAssemblyResponse.getComponentIdentity())
            .scenarioId(postAssemblyResponse.getScenarioIdentity())
            .user(currentUser)
            .build());

        assertThat(editAssemblyResponse.getResponseEntity().getLastAction(), is("FORK"));
    }
}
