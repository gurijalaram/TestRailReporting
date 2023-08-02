package com.apriori;

import com.apriori.cidappapi.models.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.models.response.scenarios.ScenarioResponse;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.ScenarioItem;
import com.apriori.qms.controller.QmsComponentResources;
import com.apriori.qms.models.request.scenariodiscussion.ProjectUserParameters;
import com.apriori.qms.models.response.component.ComponentResponse;
import com.apriori.qms.models.response.component.ComponentsAssignedResponse;
import com.apriori.qms.models.response.scenario.ScenariosResponse;
import com.apriori.qms.models.response.scenariodiscussion.ScenarioProjectUserResponse;
import com.apriori.qms.utils.QmsApiTestUtils;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QmsComponentTest extends TestUtil {
    private static final UserCredentials currentUser = UserUtil.getUser();
    private static String userContext;
    private static SoftAssertions softAssertions = new SoftAssertions();
    private static ScenarioItem scenarioItem;

    @BeforeAll
    public static void beforeClass() {
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
    }

    @AfterAll
    public static void afterClass() {
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);
        softAssertions.assertAll();
    }

    @BeforeEach
    public void beforeTest() {
        softAssertions = new SoftAssertions();
    }

    @AfterEach
    public void afterTest() {
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12479})
    @Description("get component Details by identity")
    public void getComponentDetails() {
        ResponseWrapper<ComponentResponse> componentResponse = QmsComponentResources.getComponent(userContext,
            scenarioItem.getComponentIdentity());
        softAssertions.assertThat(componentResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(componentResponse.getResponseEntity().getComponentName()).isNotNull();
    }

    @Test
    @TestRail(id = {12496})
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
    @TestRail(id = {12983})
    @Description("Get component Scenario")
    public void getComponentScenario() {
        ResponseWrapper<ScenarioResponse> componentScenarioResponse = QmsComponentResources.getComponentScenario(userContext,
            scenarioItem.getComponentIdentity(),
            scenarioItem.getScenarioIdentity());
        softAssertions.assertThat(componentScenarioResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(componentScenarioResponse.getResponseEntity().getScenarioName()).isNotNull();
    }

    @Test
    @TestRail(id = {12979})
    @Description("Get component Scenarios")
    public void getComponentScenarios() {
        ResponseWrapper<ScenariosResponse> componentScenariosResponse = QmsComponentResources.getComponentScenarios(userContext,
            scenarioItem.getComponentIdentity());
        softAssertions.assertThat(componentScenariosResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(componentScenariosResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {22094, 15475})
    @Description("Get component Scenarios Users and Verify avatarColor field is present in User object in Share")
    public void getComponentScenarioUsers() {
        UserCredentials user = UserUtil.getUser();
        ProjectUserParameters projectUserParameters = ProjectUserParameters.builder()
            .email(user.getEmail()).build();

        ResponseWrapper<ScenarioProjectUserResponse> componentScenariosResponse =
            QmsComponentResources.addComponentScenarioUser(
                scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                projectUserParameters, currentUser);
        softAssertions.assertThat(componentScenariosResponse.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);

        componentScenariosResponse = QmsComponentResources.getComponentScenarioUsers(userContext,
            scenarioItem.getComponentIdentity(),
            scenarioItem.getScenarioIdentity());
        softAssertions.assertThat(componentScenariosResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(componentScenariosResponse.getResponseEntity().size()).isEqualTo(2);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(componentScenariosResponse.getResponseEntity().stream()
                .allMatch(u -> u.getAvatarColor() != null)).isTrue();
        }
    }

    @Test
    @TestRail(id = {26878})
    @Description("Verify that user can find list of components, scenarios, and iterations")
    public void getComponentsAssigned() {
        ComponentsAssignedResponse componentsAssignedResponse = QmsComponentResources.getComponentsAssigned(
            ComponentsAssignedResponse.class,
            HttpStatus.SC_OK,
            currentUser);
        softAssertions.assertThat(componentsAssignedResponse.size()).isGreaterThan(0);
    }
}