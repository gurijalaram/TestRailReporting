package com.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.apibase.services.response.objects.ScenarioNameEntity;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.builder.ScenarioRepresentationBuilder;
import com.apriori.cidappapi.entity.response.Scenario;
import com.apriori.cidappapi.entity.response.customizations.Assembly;
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
import net.sf.saxon.expr.instruct.ForEach;
import org.apache.http.HttpStatus;
import org.junit.Test;

import javax.accessibility.AccessibleComponent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ScenariosTests {

    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();

    @Test
    @TestRail(testCaseId = "10620")
    @Description("Copy a scenario")
    public void testCopyScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        String filename  = "oldham.asm.1";
        String componentName = "OLDHAM";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String newScenarioName = new GenerateStringUtil().generateScenarioName();
        UserCredentials currentUser = UserUtil.getUser();
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
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        ArrayList<String> subComponentNames = new ArrayList<>(Arrays.asList("big ring", "Pin", "small ring"));
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();


        //Build & process sub component object based on array list of names
        for (String subComponentName:subComponentNames)
        {
            uploadAndPublishComponent(ComponentInfoBuilder.builder()
                    .componentName(subComponentName)
                    .extension(componentExtension)
                    .scenarioName(scenarioName)
                    .processGroup(processGroupEnum)
                    .user(currentUser)
                    .build());
        }

        //Process assembly
        Item assemblyUploadResponse = uploadAndPublishComponent(ComponentInfoBuilder.builder()
                .componentName(assemblyName)
                .extension(assemblyExtension)
                .scenarioName(scenarioName)
                .processGroup(ProcessGroupEnum.ASSEMBLY)
                .user(currentUser)
                .build());

        //Edit Assembly
        Scenario editAssemblyResponse = scenariosUtil.postEditScenario(ComponentInfoBuilder
                .builder()
                .componentId(assemblyUploadResponse.getComponentIdentity())
                .scenarioId(assemblyUploadResponse.getScenarioIdentity())
                .user(currentUser)
                .build()).getResponseEntity();

        //assertions
        assertThat(editAssemblyResponse.getLastAction(), is("FORK"));

        //possible to check for status as well to confirm forked scenario identity get to 'not costed'
        //or verify its private or other details.


        //OtherTest Ideas
        //option, publish the now temporary scenario using editAssemblyReponse object to pass into the Publis Method
        //choose which option,  overwrite or new scenario, both of these are valid new test cases


        //option, try to edit original a second time
        /*
        Scenario editAssemblyAgainResponse = scenariosUtil.postEditScenario(ComponentInfoBuilder
                .builder()
                .componentId(assemblyUploadResponse.getComponentIdentity())
                .scenarioId(assemblyUploadResponse.getScenarioIdentity())
                .user(currentUser)
                .build(), "NewScenarioName").getResponseEntity();
         */
        //choose which option,  overwrite the temporary one from the original fork, or new scenario, both again are valid new test cases

        //also consider what to do with subcomponets under each situation above.

    }

    private Item uploadAndPublishComponent(ComponentInfoBuilder component) {
        File resourceFile = FileResourceUtil.getCloudFile(component.getProcessGroup(), component.getComponentName() + component.getExtension());

        Item postComponentResponse;
        postComponentResponse = componentsUtil.postComponentQueryCSS(component.getComponentName(), component.getScenarioName(), resourceFile, component.getUser());

        ResponseWrapper<ScenarioResponse> componentPublishResponse = scenariosUtil.postPublishScenario(postComponentResponse,
            postComponentResponse.getComponentIdentity(),
            postComponentResponse.getScenarioIdentity(),
            component.getUser());

        assertThat(componentPublishResponse.getResponseEntity().getLastAction(), is("PUBLISH"));
        assertThat(componentPublishResponse.getResponseEntity().getPublished(), is(true));

        return postComponentResponse;
    }
}
