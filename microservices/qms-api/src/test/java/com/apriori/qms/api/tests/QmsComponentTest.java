package com.apriori.qms.api.tests;

import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.cis.api.controller.CisBidPackageProjectResources;
import com.apriori.css.api.enums.CssSearch;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.qms.api.controller.QmsComponentResources;
import com.apriori.qms.api.controller.QmsProjectResources;
import com.apriori.qms.api.models.request.bidpackage.AssignedComponentRequest;
import com.apriori.qms.api.models.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.api.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.api.models.request.scenariodiscussion.ProjectUserParameters;
import com.apriori.qms.api.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.api.models.response.component.ComponentAssignedParameters;
import com.apriori.qms.api.models.response.component.ComponentResponse;
import com.apriori.qms.api.models.response.component.ComponentsAssignedResponse;
import com.apriori.qms.api.models.response.scenario.ScenariosResponse;
import com.apriori.qms.api.models.response.scenariodiscussion.ScenarioProjectUserResponse;
import com.apriori.serialization.util.DateFormattingUtils;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.DateUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.models.response.component.componentiteration.ComponentIteration;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class QmsComponentTest extends TestUtil {
    private static UserCredentials currentUser;
    private static String userContext;
    private static SoftAssertions softAssertions = new SoftAssertions();
    private static ScenarioItem scenarioItem;

    @BeforeAll
    public static void beforeClass() {
        currentUser = UserUtil.getUser();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser, CssSearch.SCENARIO_CREATED_AT_GT.getKey() + DateUtil.getDateDaysBefore(90, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ), CssSearch.COMPONENT_TYPE_EQ.getKey() + "PART").get(0);
    }

    @AfterAll
    public static void afterClass() {
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
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new HashMap<>(),
            itemsList,
            null,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getUsers().size()).isEqualTo(1);

        ComponentAssignedParameters componentParameters = ComponentAssignedParameters.builder()
            .componentIdentity(scenarioItem.getComponentIdentity())
            .scenarioIdentity(scenarioItem.getScenarioIdentity())
            .iterationIdentity(scenarioItem.getIterationIdentity())
            .build();
        AssignedComponentRequest componentModel = AssignedComponentRequest.builder().components(Collections.singletonList(componentParameters)).build();

        ComponentsAssignedResponse componentsAssignedResponse = QmsComponentResources.getComponentsAssigned(componentModel,
            ComponentsAssignedResponse.class,
            HttpStatus.SC_OK,
            currentUser);
        softAssertions.assertThat(componentsAssignedResponse.size()).isGreaterThan(0);
        CisBidPackageProjectResources.deleteBidPackageProject(bppResponse.getBidPackageIdentity(), bppResponse.getIdentity(), HttpStatus.SC_NO_CONTENT, currentUser);
    }
}