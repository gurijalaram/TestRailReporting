package com.apriori.qms.tests;


import com.apriori.apibase.utils.TestUtil;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsComponentResources;
import com.apriori.qms.entity.response.bidpackage.ComponentResponse;
import com.apriori.qms.entity.response.bidpackage.ScenariosResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioProjectUserResponse;
import com.apriori.utils.CssComponent;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QmsComponentTest extends TestUtil {

    public static ScenarioItem scenarioItem;
    private static SoftAssertions softAssertions;
    private static String userContext;
    private UserCredentials currentUser;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
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
    @TestRail(testCaseId = {"22094"})
    @Description("Get component Scenarios Users and Verify avatarColor field is present in User object in Share")
    public void getComponentScenarioUsers() {
        ResponseWrapper<ScenarioProjectUserResponse> componentScenariosResponse =
            QmsComponentResources.getComponentScenarioUsers(userContext,
                scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity());
        softAssertions.assertThat(componentScenariosResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(componentScenariosResponse.getResponseEntity().size()).isGreaterThan(0);
        softAssertions.assertThat(componentScenariosResponse.getResponseEntity().get(0).getAvatarColor());
    }

    @After
    public void testCleanup() {
        softAssertions.assertAll();
    }
}