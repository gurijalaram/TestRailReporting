package com.apriori.qms.tests;


import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.qms.controller.QmsComponentResources;
import com.apriori.qms.entity.response.bidpackage.ComponentResponse;
import com.apriori.qms.entity.response.bidpackage.ScenariosResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioProjectUserResponse;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.QmsApiTestDataUtils;

public class QmsComponentTest extends QmsApiTestDataUtils {
    private static String userContext;

    @Before
    public void beforeTest() {
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        createTestData();
    }

    @After
    public void afterTest() {
        deleteTestData();
    }

    @Test
    @TestRail(testCaseId = {"12479"})
    @Description("get component Details by identity")
    public void getComponentDetails() {
        ResponseWrapper<ComponentResponse> componentResponse = QmsComponentResources.getComponent(userContext,
            scenarioItem.getComponentIdentity());
        softAssertions.assertThat(componentResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(componentResponse.getResponseEntity().getComponentName()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"12496"})
    @Description("Get the component scenario latest iteration")
    public void getComponentsIterationsLatest() {
        ResponseWrapper<ComponentIteration> componentIterationResponse = QmsComponentResources.getLatestIteration(userContext,
            scenarioItem.getComponentIdentity(),
            scenarioItem.getScenarioIdentity(),
            scenarioItem.getIterationIdentity());
        softAssertions.assertThat(componentIterationResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(componentIterationResponse.getResponseEntity().getIdentity()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"12983"})
    @Description("Get component Scenario")
    public void getComponentScenario() {
        ResponseWrapper<ScenarioResponse> componentScenarioResponse = QmsComponentResources.getComponentScenario(userContext,
            scenarioItem.getComponentIdentity(),
            scenarioItem.getScenarioIdentity());
        softAssertions.assertThat(componentScenarioResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(componentScenarioResponse.getResponseEntity().getScenarioName()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"12979"})
    @Description("Get component Scenarios")
    public void getComponentScenarios() {
        ResponseWrapper<ScenariosResponse> componentScenariosResponse = QmsComponentResources.getComponentScenarios(userContext,
            scenarioItem.getComponentIdentity());
        softAssertions.assertThat(componentScenariosResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(componentScenariosResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"22094", "15475"})
    @Description("Get component Scenarios Users and Verify avatarColor field is present in User object in Share")
    public void getComponentScenarioUsers() {
        ResponseWrapper<ScenarioProjectUserResponse> componentScenariosResponse =
            QmsComponentResources.getComponentScenarioUsers(userContext,
                scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity());
        softAssertions.assertThat(componentScenariosResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(componentScenariosResponse.getResponseEntity().size()).isEqualTo(1);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(componentScenariosResponse.getResponseEntity().stream()
                .allMatch(u -> !u.getAvatarColor().equals(null))).isTrue();
        }
    }
}