package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidapp.entity.response.PostComponentResponse;
import com.apriori.cidapp.entity.response.componentiteration.ActiveAxes;
import com.apriori.cidapp.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidapp.entity.response.scenarios.CostResponse;
import com.apriori.cidapp.entity.response.scenarios.ImageResponse;
import com.apriori.cidapp.utils.CidAppTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.stream.Collectors;

public class ComponentImageTests {

    private CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();

    @Test
    @TestRail(testCaseId = {"5834"})
    @Description("Test bounding values are correct")
    public void boundingBoxValuesTest() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ResponseWrapper<PostComponentResponse> postComponentResponse = cidAppTestUtil.postComponents(scenarioName, ProcessGroupEnum.SHEET_METAL.getProcessGroup(), "700-33770-01_A0.stp");

        assertThat(postComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String componentIdentity = postComponentResponse.getResponseEntity().getComponentIdentity();
        String scenarioIdentity = postComponentResponse.getResponseEntity().getScenarioIdentity();

        ResponseWrapper<CostResponse> preCostState = cidAppTestUtil.getScenarioRepresentation("processing", componentIdentity, scenarioIdentity);
        assertThat(preCostState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<CostResponse> costResponse = cidAppTestUtil.postCostComponent(componentIdentity, scenarioIdentity);
        assertThat(costResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<CostResponse> costState = cidAppTestUtil.getScenarioRepresentation("processing", componentIdentity, scenarioIdentity);
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

        ResponseWrapper<PostComponentResponse> postComponentResponse = cidAppTestUtil.postComponents(scenarioName, ProcessGroupEnum.SHEET_METAL.getProcessGroup(), "700-33770-01_A0.stp");

        assertThat(postComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String componentIdentity = postComponentResponse.getResponseEntity().getComponentIdentity();
        String scenarioIdentity = postComponentResponse.getResponseEntity().getScenarioIdentity();

        ResponseWrapper<CostResponse> preCostState = cidAppTestUtil.getScenarioRepresentation("processing", componentIdentity, scenarioIdentity);
        assertThat(preCostState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<CostResponse> costResponse = cidAppTestUtil.postCostComponent(componentIdentity, scenarioIdentity);
        assertThat(costResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<CostResponse> costState = cidAppTestUtil.getScenarioRepresentation("processing", componentIdentity, scenarioIdentity);
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

        ResponseWrapper<PostComponentResponse> postComponentResponse = cidAppTestUtil.postComponents(scenarioName, ProcessGroupEnum.SHEET_METAL.getProcessGroup(), "700-33770-01_A0.stp");

        assertThat(postComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String componentIdentity = postComponentResponse.getResponseEntity().getComponentIdentity();
        String scenarioIdentity = postComponentResponse.getResponseEntity().getScenarioIdentity();

        ResponseWrapper<CostResponse> preCostState = cidAppTestUtil.getScenarioRepresentation("processing", componentIdentity, scenarioIdentity);
        assertThat(preCostState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<CostResponse> costResponse = cidAppTestUtil.postCostComponent(componentIdentity, scenarioIdentity);
        assertThat(costResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<CostResponse> costState = cidAppTestUtil.getScenarioRepresentation("processing", componentIdentity, scenarioIdentity);
        assertThat(costState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        ResponseWrapper<ComponentIteration> componentIterationResponse = cidAppTestUtil.getComponentIterationLatest(componentIdentity, scenarioIdentity);
        assertThat(componentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(componentIterationResponse.getResponseEntity().getResponse().getScenarioMetadata().getActiveAxes().stream().map(ActiveAxes::getDisplayName).collect(Collectors.toList()), hasItems("SetupAxis:1"));
    }
}
