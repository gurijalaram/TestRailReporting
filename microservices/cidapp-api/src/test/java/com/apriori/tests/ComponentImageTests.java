package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.response.componentiteration.ActiveAxes;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.ImageResponse;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.CidAppTestUtil;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.stream.Collectors;

public class ComponentImageTests {

    private CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();
    private UserCredentials currentUser;

    @Test
    @TestRail(testCaseId = {"5834"})
    @Description("Test bounding values are correct")
    public void boundingBoxValuesTest() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        Item postComponentResponse = cidAppTestUtil.postCssComponents("700-33770-01_A0.stp", scenarioName, ProcessGroupEnum.SHEET_METAL.getProcessGroup(), currentUser);

        String componentIdentity = postComponentResponse.getComponentIdentity();
        String scenarioIdentity = postComponentResponse.getScenarioIdentity();

        ResponseWrapper<ScenarioResponse> preCostState = cidAppTestUtil.getScenarioRepresentation("processing", componentIdentity, scenarioIdentity, currentUser);
        assertThat(preCostState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ScenarioResponse> costResponse = cidAppTestUtil.postCostComponent(componentIdentity, scenarioIdentity);
        assertThat(costResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<ScenarioResponse> costState = cidAppTestUtil.getScenarioRepresentation("processing", componentIdentity, scenarioIdentity, currentUser);
        assertThat(costState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ImageResponse> imageResponse = cidAppTestUtil.getHoopsImage(componentIdentity, scenarioIdentity);
        assertThat(imageResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ComponentIteration> componentIterationResponse = cidAppTestUtil.getComponentIterationLatest(componentIdentity, scenarioIdentity);
        assertThat(componentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(componentIterationResponse.getResponseEntity().getResponse().getScenarioMetadata().getBoundingBox(), hasItems(-1.25, 0.0, 0.0, 1.25, 0.800000011920929, 0.012000000104308128));
    }

    @Test
    @TestRail(testCaseId = {"6639"})
    @Description("Test axes entries values are correct")
    public void axesEntriesValuesTest() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        Item postComponentResponse = cidAppTestUtil.postCssComponents("700-33770-01_A0.stp", scenarioName, ProcessGroupEnum.SHEET_METAL.getProcessGroup(), currentUser);

        String componentIdentity = postComponentResponse.getComponentIdentity();
        String scenarioIdentity = postComponentResponse.getScenarioIdentity();

        ResponseWrapper<ScenarioResponse> preCostState = cidAppTestUtil.getScenarioRepresentation("processing", componentIdentity, scenarioIdentity, currentUser);
        assertThat(preCostState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ScenarioResponse> costResponse = cidAppTestUtil.postCostComponent(componentIdentity, scenarioIdentity);
        assertThat(costResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<ScenarioResponse> costState = cidAppTestUtil.getScenarioRepresentation("processing", componentIdentity, scenarioIdentity, currentUser);
        assertThat(costState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ComponentIteration> componentIterationResponse = cidAppTestUtil.getComponentIterationLatest(componentIdentity, scenarioIdentity);
        assertThat(componentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(componentIterationResponse.getResponseEntity().getResponse().getScenarioMetadata().getAxesEntries().size(), is(equalTo(6)));
    }

    @Test
    @TestRail(testCaseId = {"5851"})
    @Description("Test active axes values are correct")
    public void activeAxesValuesTest() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        Item postComponentResponse = cidAppTestUtil.postCssComponents("700-33770-01_A0.stp", scenarioName, ProcessGroupEnum.SHEET_METAL.getProcessGroup(), currentUser);

        String componentIdentity = postComponentResponse.getComponentIdentity();
        String scenarioIdentity = postComponentResponse.getScenarioIdentity();

        ResponseWrapper<ScenarioResponse> preCostState = cidAppTestUtil.getScenarioRepresentation("processing", componentIdentity, scenarioIdentity, currentUser);
        assertThat(preCostState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ScenarioResponse> costResponse = cidAppTestUtil.postCostComponent(componentIdentity, scenarioIdentity);
        assertThat(costResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<ScenarioResponse> costState = cidAppTestUtil.getScenarioRepresentation("processing", componentIdentity, scenarioIdentity, currentUser);
        assertThat(costState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ComponentIteration> componentIterationResponse = cidAppTestUtil.getComponentIterationLatest(componentIdentity, scenarioIdentity);
        assertThat(componentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(componentIterationResponse.getResponseEntity().getResponse().getScenarioMetadata().getActiveAxes().stream().map(ActiveAxes::getDisplayName).collect(Collectors.toList()), hasItems("SetupAxis:1"));
    }
}
