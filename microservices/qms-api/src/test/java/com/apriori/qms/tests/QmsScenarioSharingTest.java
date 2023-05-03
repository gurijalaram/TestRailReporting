package com.apriori.qms.tests;


import com.apriori.qms.controller.QmsComponentResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.request.scenariodiscussion.ProjectUserParameters;
import com.apriori.qms.entity.request.scenariodiscussion.ProjectUserRequest;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionParameters;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionRequest;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioProjectUserResponse;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.QmsApiTestDataUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QmsScenarioSharingTest extends QmsApiTestDataUtils {
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
    @TestRail(testCaseId = {"15474", "15476"})
    @Description("Verify that user can add and delete the Project User")
    public void addAndDeleteComponentScenarioUser() {
        //Add
        UserCredentials anotherUser = UserUtil.getUser();
        ProjectUserParameters projectUserParameters = ProjectUserParameters.builder()
            .email(anotherUser.getEmail()).build();

        ResponseWrapper<ScenarioProjectUserResponse> componentScenariosResponse =
            QmsComponentResources.addComponentScenarioUser(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                projectUserParameters, currentUser);
        softAssertions.assertThat(componentScenariosResponse.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        softAssertions.assertThat(componentScenariosResponse.getResponseEntity().size()).isEqualTo(2);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(componentScenariosResponse.getResponseEntity().stream()
                .anyMatch(u -> u.getIdentity()
                    .equals(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail())))).isTrue();
            softAssertions.assertThat(componentScenariosResponse.getResponseEntity().stream()
                .anyMatch(u -> u.getIdentity()
                    .equals(new AuthUserContextUtil().getAuthUserIdentity(anotherUser.getEmail())))).isTrue();
            softAssertions.assertThat(componentScenariosResponse.getResponseEntity().stream()
                .allMatch(u -> !u.getAvatarColor().equals(null))).isTrue();
        }

        //Delete
        ProjectUserRequest deleteProjectUserRequest = ProjectUserRequest.builder()
            .users(Collections.singletonList(ProjectUserParameters.builder()
                .email(anotherUser.getEmail())
                .build()))
            .build();

        QmsComponentResources.deleteComponentScenarioUser(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), deleteProjectUserRequest, currentUser);

        //Verify Delete
        ResponseWrapper<ScenarioProjectUserResponse> getComponentScenariosResponse =
            QmsComponentResources.getComponentScenarioUsers(userContext,
                scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity());
        softAssertions.assertThat(getComponentScenariosResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(getComponentScenariosResponse.getResponseEntity().size()).isEqualTo(1);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(getComponentScenariosResponse.getResponseEntity().stream()
                .allMatch(u -> u.getIdentity()
                    .equals(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail())))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"16351"})
    @Description("Verify that more than 10 Users can be added by using SHARE option")
    public void addMoreThan10ComponentScenarioUsers() {
        List<ProjectUserParameters> projectUsersList = new ArrayList<>();
        for (int i = 1; i < 12; i++) {
            projectUsersList.add(ProjectUserParameters.builder()
                .email(UserUtil.getUser().getEmail())
                .build());
        }

        ProjectUserRequest createProjectUserRequest = ProjectUserRequest.builder()
            .users(projectUsersList)
            .build();

        ScenarioProjectUserResponse userResponse = QmsComponentResources.addComponentScenarioUser(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), createProjectUserRequest, currentUser);
        softAssertions.assertThat(userResponse.size()).isEqualTo(12);
        if (softAssertions.wasSuccess()) {
            for (ProjectUserParameters userParams : projectUsersList) {
                softAssertions.assertThat(userResponse.stream()
                        .anyMatch(u -> u.getIdentity()
                            .equals(new AuthUserContextUtil().getAuthUserIdentity(userParams.getEmail()))))
                    .isTrue();
            }
        }

        //GET Users
        ResponseWrapper<ScenarioProjectUserResponse> componentScenariosResponse =
            QmsComponentResources.getComponentScenarioUsers(userContext,
                scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity());
        softAssertions.assertThat(componentScenariosResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(componentScenariosResponse.getResponseEntity().size()).isEqualTo(12);
        if (softAssertions.wasSuccess()) {
            for (ProjectUserParameters userParams : projectUsersList) {
                softAssertions.assertThat(userResponse.stream()
                        .anyMatch(u -> u.getIdentity()
                            .equals(new AuthUserContextUtil().getAuthUserIdentity(userParams.getEmail()))))
                    .isTrue();
            }
        }
    }

    @Test
    @TestRail(testCaseId = {"16352"})
    @Description("Verify that user will not get error messages when adding a new user to a component/scenario pair where one of the discussions is RESOLVED")
    public void addNewUserToResolvedDiscussionNoError() {
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status("RESOLVED").build()).build();
        ScenarioDiscussionResponse updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("RESOLVED");

        //Add New User
        UserCredentials anotherUser = UserUtil.getUser();
        ProjectUserParameters projectUserParameters = ProjectUserParameters.builder()
            .email(anotherUser.getEmail()).build();
        ProjectUserRequest createProjectUserRequest = ProjectUserRequest.builder()
            .users(Collections.singletonList(projectUserParameters))
            .build();

        ScenarioProjectUserResponse userResponse = QmsComponentResources.addComponentScenarioUser(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), createProjectUserRequest, currentUser);
        softAssertions.assertThat(userResponse.size()).isEqualTo(2);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(userResponse.stream()
                    .anyMatch(u -> u.getIdentity()
                        .equals(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))))
                .isTrue();
            softAssertions.assertThat(userResponse.stream()
                    .anyMatch(u -> u.getIdentity()
                        .equals(new AuthUserContextUtil().getAuthUserIdentity(anotherUser.getEmail()))))
                .isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"16531"})
    @Description("Verify that user will not get error messages when adding a new user to a component/scenario pair where one of the discussions is DELETED")
    public void addNewUserToDeletedDiscussionNoError() {
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status("DELETED").build()).build();
        ScenarioDiscussionResponse updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("DELETED");

        //Add New User
        UserCredentials anotherUser = UserUtil.getUser();
        ProjectUserParameters projectUserParameters = ProjectUserParameters.builder()
            .email(anotherUser.getEmail()).build();
        ProjectUserRequest createProjectUserRequest = ProjectUserRequest.builder()
            .users(Collections.singletonList(projectUserParameters))
            .build();

        ScenarioProjectUserResponse userResponse = QmsComponentResources.addComponentScenarioUser(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), createProjectUserRequest, currentUser);
        softAssertions.assertThat(userResponse.size()).isEqualTo(2);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(userResponse.stream()
                    .anyMatch(u -> u.getIdentity()
                        .equals(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))))
                .isTrue();
            softAssertions.assertThat(userResponse.stream()
                    .anyMatch(u -> u.getIdentity()
                        .equals(new AuthUserContextUtil().getAuthUserIdentity(anotherUser.getEmail()))))
                .isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"16353"})
    @Description("Verify that user will not get error messages after deleted himself from User list")
    public void deleteSelfComponentScenarioUserNoError() {
        //Add
        UserCredentials anotherUser = UserUtil.getUser();
        ProjectUserParameters projectUserParameters = ProjectUserParameters.builder()
            .email(anotherUser.getEmail()).build();

        ResponseWrapper<ScenarioProjectUserResponse> componentScenariosResponse =
            QmsComponentResources.addComponentScenarioUser(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                projectUserParameters, currentUser);
        softAssertions.assertThat(componentScenariosResponse.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        softAssertions.assertThat(componentScenariosResponse.getResponseEntity().size()).isEqualTo(2);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(componentScenariosResponse.getResponseEntity().stream()
                .anyMatch(u -> u.getIdentity()
                    .equals(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail())))).isTrue();
            softAssertions.assertThat(componentScenariosResponse.getResponseEntity().stream()
                .anyMatch(u -> u.getIdentity()
                    .equals(new AuthUserContextUtil().getAuthUserIdentity(anotherUser.getEmail())))).isTrue();
        }

        //Delete
        ProjectUserRequest deleteProjectUserRequest = ProjectUserRequest.builder()
            .users(Collections.singletonList(ProjectUserParameters.builder()
                .email(currentUser.getEmail())
                .build()))
            .build();
        QmsComponentResources.deleteComponentScenarioUser(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), deleteProjectUserRequest, currentUser);
    }
}