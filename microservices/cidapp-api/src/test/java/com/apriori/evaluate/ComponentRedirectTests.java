package com.apriori.evaluate;

import static com.apriori.testconfig.TestSuiteType.TestSuite.REGRESSION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.request.PublishRequest;
import com.apriori.cidappapi.models.response.PostComponentResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ComponentRedirectTests {

    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14197)
    @Description("Verify receipt of 301 response when getting component details of a file which already exists")
    public void receive301AfterUploadOfExistingComponent() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder existingPart = ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build();

        ResponseWrapper<PostComponentResponse> existingPartResponse = componentsUtil.postComponent(existingPart);

        existingPart.setComponentIdentity(existingPartResponse.getResponseEntity().getSuccesses().get(0).getComponentIdentity());
        existingPart.setScenarioIdentity(existingPartResponse.getResponseEntity().getSuccesses().get(0).getScenarioIdentity());

        assertThat(componentsUtil.getComponentIdentityExpectingStatusCode(existingPart, HttpStatus.SC_MOVED_PERMANENTLY).getBody(), is(emptyString()));
    }

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14440)
    @Description("Verify receipt of 301 response when getting component and scenario details of a file which already exists using new scenario")
    public void receive301AfterUploadOfExistingComponentWithNewScenario() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder existingPart = ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build();

        ResponseWrapper<PostComponentResponse> existingPartResponse = componentsUtil.postComponent(existingPart);

        existingPart.setComponentIdentity(existingPartResponse.getResponseEntity().getSuccesses().get(0).getComponentIdentity());
        existingPart.setScenarioIdentity(existingPartResponse.getResponseEntity().getSuccesses().get(0).getScenarioIdentity());

        assertThat(scenariosUtil.getScenarioExpectingStatusCode(existingPart, HttpStatus.SC_MOVED_PERMANENTLY).getBody(), is(emptyString()));
    }

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14444)
    @Description("Verify receipt of 301 response when getting component and scenario details of a file which already exists using new scenario")
    public void receive301AfterUploadOfExistingComponentWithOverriddenScenario() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder existingPart = ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .publishRequest(PublishRequest.builder().override(true).build())
            .build();

        ResponseWrapper<PostComponentResponse> existingPartScenarioResponse = componentsUtil.postComponent(existingPart);

        existingPart.setComponentIdentity(existingPartScenarioResponse.getResponseEntity().getSuccesses().get(0).getComponentIdentity());
        existingPart.setScenarioIdentity(existingPartScenarioResponse.getResponseEntity().getSuccesses().get(0).getScenarioIdentity());

        assertThat(scenariosUtil.getScenarioExpectingStatusCode(existingPart, HttpStatus.SC_MOVED_PERMANENTLY).getBody(), is(emptyString()));
    }

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14450)
    @Description("Verify receipt of 301 response when getting iteration details of a file which already exists using new scenario")
    public void receive301IterationsEndpoint() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder existingPart = ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build();

        ResponseWrapper<PostComponentResponse> existingPartResponse = componentsUtil.postComponent(existingPart);

        existingPart.setComponentIdentity(existingPartResponse.getResponseEntity().getSuccesses().get(0).getComponentIdentity());
        existingPart.setScenarioIdentity(existingPartResponse.getResponseEntity().getSuccesses().get(0).getScenarioIdentity());

        assertThat(componentsUtil.getComponentIterationLatestExpectingStatusCode(existingPart, HttpStatus.SC_MOVED_PERMANENTLY).getBody(), is(emptyString()));
    }
}
