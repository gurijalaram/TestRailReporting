package com.apriori.cid.api.tests.evaluate;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REGRESSION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;

import com.apriori.cid.api.enums.CidAppAPIEnum;
import com.apriori.cid.api.models.response.ComponentIdentityResponse;
import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.PostComponentResponse;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ComponentRedirectTests {

    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private ComponentInfoBuilder component;
    private ComponentInfoBuilder component2;

    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14197)
    @Description("Verify receipt of 301 response when getting component details of a file which already exists")
    public void receive301AfterUploadOfExistingComponent() {
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        component = new ComponentRequestUtil().getComponent();
        component2 = SerializationUtils.clone(component);
        component2.setScenarioName(scenarioName2);

        componentsUtil.postComponent(component);
        PostComponentResponse existingPartResponse = componentsUtil.postComponentResponse(component2);

        component2.setComponentIdentity(existingPartResponse.getSuccesses().get(0).getComponentIdentity());
        component2.setScenarioIdentity(existingPartResponse.getSuccesses().get(0).getScenarioIdentity());

        ResponseWrapper<Object> redirectResponse = componentsUtil.getComponentIdentityExpectingStatusCode(component2, HttpStatus.SC_MOVED_PERMANENTLY);
        softAssertions.assertThat(redirectResponse.getBody())
            .as("Verify 301 response received and body is empty").isEqualTo("");
        softAssertions.assertThat(redirectResponse.getHeaders().get("Location"))
            .as("Verify referred location differs to original request").isNotEqualTo("Location=/components/" + component2.getComponentIdentity());

        ComponentIdentityResponse response = componentsUtil.getComponentIdentity(component2);
        softAssertions.assertThat("Location=/components/" + response.getIdentity())
            .as("Verify that final component identity matches that given in 301").isEqualTo(redirectResponse.getHeaders().get("Location").toString());

        softAssertions.assertAll();
    }

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14440)
    @Description("Verify receipt of 301 response when getting component and scenario details of a file which already exists using new scenario")
    public void receive301AfterUploadOfExistingComponentWithNewScenario() {
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        component = new ComponentRequestUtil().getComponent();
        component2 = SerializationUtils.clone(component);
        component2.setScenarioName(scenarioName2);

        componentsUtil.postComponent(component);
        PostComponentResponse existingPartResponse = componentsUtil.postComponentResponse(component2);

        component2.setComponentIdentity(existingPartResponse.getSuccesses().get(0).getComponentIdentity());
        component2.setScenarioIdentity(existingPartResponse.getSuccesses().get(0).getScenarioIdentity());

        ResponseWrapper<Object> redirectResponse = scenariosUtil.getScenarioExpectingStatusCode(component2, HttpStatus.SC_MOVED_PERMANENTLY);
        softAssertions.assertThat(redirectResponse.getBody())
            .as("Verify 301 response received and body is empty").isEqualTo("");
        softAssertions.assertThat(redirectResponse.getHeaders().get("Location"))
            .as("Verify referred location differs to original request")
            .isNotEqualTo("Location=" + String.format(
                CidAppAPIEnum.SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS.getEndpointString(),
                component2.getComponentIdentity(), component2.getScenarioIdentity()));

        softAssertions.assertThat("Location=" + String.format(
                CidAppAPIEnum.SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS.getEndpointString(),
                componentsUtil.getComponentIdentity(component2).getIdentity(), component2.getScenarioIdentity()))
            .as("Verify that final component identity matches that given in 301").isEqualTo(redirectResponse.getHeaders().get("Location").toString());

        softAssertions.assertAll();
    }

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14444)
    @Description("Verify receipt of 301 response when getting component and scenario details of a file which already exists using new scenario")
    public void receive301AfterUploadOfExistingComponentWithOverriddenScenario() {
        component = new ComponentRequestUtil().getComponent();
        component2 = SerializationUtils.clone(component);
        component2.setOverrideScenario(true);

        componentsUtil.postComponent(component);
        PostComponentResponse existingPartResponse = componentsUtil.postComponentResponse(component2);

        component2.setComponentIdentity(existingPartResponse.getSuccesses().get(0).getComponentIdentity());
        component2.setScenarioIdentity(existingPartResponse.getSuccesses().get(0).getScenarioIdentity());

        ResponseWrapper<Object> redirectResponse = scenariosUtil.getScenarioExpectingStatusCode(component2, HttpStatus.SC_MOVED_PERMANENTLY);
        softAssertions.assertThat(redirectResponse.getBody())
            .as("Verify 301 response received and body is empty").isEqualTo("");
        softAssertions.assertThat(redirectResponse.getHeaders().get("Location"))
            .as("Verify referred location differs to original request")
            .isNotEqualTo("Location=" + String.format(
                CidAppAPIEnum.SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS.getEndpointString(),
                component2.getComponentIdentity(), component2.getScenarioIdentity()));

        softAssertions.assertThat("Location=" + String.format(
                CidAppAPIEnum.SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS.getEndpointString(),
                componentsUtil.getComponentIdentity(component2).getIdentity(), component.getScenarioIdentity()))
            .as("Verify that final component identity matches that given in 301").isEqualTo(redirectResponse.getHeaders().get("Location").toString());

        softAssertions.assertAll();
    }

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14450)
    @Description("Verify receipt of 301 response when getting iteration details of a file which already exists using new scenario")
    public void receive301IterationsEndpoint() {
        component = new ComponentRequestUtil().getComponent();
        component2 = SerializationUtils.clone(component);
        component2.setOverrideScenario(true);

        componentsUtil.postComponent(component);
        PostComponentResponse existingPartResponse = componentsUtil.postComponentResponse(component2);

        component2.setComponentIdentity(existingPartResponse.getSuccesses().get(0).getComponentIdentity());
        component2.setScenarioIdentity(existingPartResponse.getSuccesses().get(0).getScenarioIdentity());

        ResponseWrapper<Object> redirectResponse = componentsUtil.getComponentIterationLatestExpectingStatusCode(component2, HttpStatus.SC_MOVED_PERMANENTLY);
        softAssertions.assertThat(redirectResponse.getBody())
            .as("Verify 301 response received and body is empty").isEqualTo("");
        softAssertions.assertThat(redirectResponse.getHeaders().get("Location"))
            .as("Verify referred location differs to original request")
            .isNotEqualTo("Location=" + String.format(
                CidAppAPIEnum.COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS.getEndpointString(),
                component2.getComponentIdentity(), component2.getScenarioIdentity()));

        softAssertions.assertThat("Location=" + String.format(
                CidAppAPIEnum.COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS.getEndpointString(),
                componentsUtil.getComponentIdentity(component2).getIdentity(), component.getScenarioIdentity()))
            .as("Verify that final component identity matches that given in 301").isEqualTo(redirectResponse.getHeaders().get("Location").toString());

        softAssertions.assertAll();
    }
}
