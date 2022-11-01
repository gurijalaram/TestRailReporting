package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.componentiteration.ActiveAxes;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.ImageResponse;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
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
import java.util.stream.Collectors;

public class ComponentImageTests {

    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private UserCredentials currentUser;

    @Test
    @TestRail(testCaseId = {"5834"})
    @Description("Test bounding values are correct")
    public void boundingBoxValuesTest() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.SHEET_METAL;
        final String componentName = "700-33770-01_A0";
        File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ComponentInfoBuilder postComponentResponse = componentsUtil.postComponentQueryCSSUncosted(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile)
                .user(currentUser)
                .build());

        ResponseWrapper<ScenarioResponse> preCostState = scenariosUtil.getScenarioRepresentation(postComponentResponse);
        assertThat(preCostState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ScenarioResponse> costResponse = scenariosUtil.postCostComponent(postComponentResponse);
        assertThat(costResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<ScenarioResponse> costState = scenariosUtil.getScenarioRepresentation(postComponentResponse);
        assertThat(costState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ImageResponse> imageResponse = scenariosUtil.getHoopsImage(postComponentResponse.getComponentIdentity(), postComponentResponse.getScenarioIdentity());
        assertThat(imageResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ComponentIteration> componentIterationResponse = componentsUtil.getComponentIterationLatest(postComponentResponse);
        assertThat(componentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(componentIterationResponse.getResponseEntity().getScenarioMetadata().getBoundingBox(), hasItems(-1.25, 0.0, 0.0, 1.25, 0.800000011920929, 0.012000000104308128));
    }

    @Test
    @TestRail(testCaseId = {"6639"})
    @Description("Test axes entries values are correct")
    public void axesEntriesValuesTest() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.SHEET_METAL;
        final String componentName = "700-33770-01_A0";
        File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ComponentInfoBuilder postComponentResponse = componentsUtil.postComponentQueryCSSUncosted(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .user(currentUser)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ResponseWrapper<ScenarioResponse> preCostState = scenariosUtil.getScenarioRepresentation(postComponentResponse);
        assertThat(preCostState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ScenarioResponse> costResponse = scenariosUtil.postCostComponent(postComponentResponse);
        assertThat(costResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<ScenarioResponse> costState = scenariosUtil.getScenarioRepresentation(postComponentResponse);
        assertThat(costState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ComponentIteration> componentIterationResponse = componentsUtil.getComponentIterationLatest(postComponentResponse);
        assertThat(componentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(componentIterationResponse.getResponseEntity().getScenarioMetadata().getAxesEntries().size(), is(equalTo(6)));
    }

    @Test
    @TestRail(testCaseId = {"5851"})
    @Description("Test active axes values are correct")
    public void activeAxesValuesTest() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.SHEET_METAL;
        final String componentName = "700-33770-01_A0";
        File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ComponentInfoBuilder postComponentResponse = componentsUtil.postComponentQueryCSSUncosted(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .user(currentUser)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ResponseWrapper<ScenarioResponse> preCostState = scenariosUtil.getScenarioRepresentation(postComponentResponse);
        assertThat(preCostState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ScenarioResponse> costResponse = scenariosUtil.postCostComponent(postComponentResponse);
        assertThat(costResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<ScenarioResponse> costState = scenariosUtil.getScenarioRepresentation(postComponentResponse);
        assertThat(costState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ComponentIteration> componentIterationResponse = componentsUtil.getComponentIterationLatest(postComponentResponse);
        assertThat(componentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(componentIterationResponse.getResponseEntity().getScenarioMetadata().getActiveAxes().stream().map(ActiveAxes::getDisplayName).collect(Collectors.toList()), hasItems("SetupAxis:1"));
    }
}
