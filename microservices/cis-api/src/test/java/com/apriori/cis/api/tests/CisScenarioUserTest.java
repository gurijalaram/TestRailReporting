package com.apriori.cis.api.tests;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cis.api.controller.CisDiscussionResources;
import com.apriori.cis.api.models.request.discussion.InternalScenarioUser;
import com.apriori.cis.api.models.request.discussion.InternalScenarioUserRequest;
import com.apriori.cis.api.models.response.bidpackage.CisErrorMessage;
import com.apriori.cis.api.models.response.scenariodiscussion.ScenarioUsersResponse;
import com.apriori.cis.api.util.CISTestUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import jdk.jfr.Description;
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
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class CisScenarioUserTest extends CISTestUtil {

    private static ComponentInfoBuilder componentInfoBuilder;
    private SoftAssertions softAssertions;

    @BeforeAll
    public static void beforeClass() {
        componentInfoBuilder = new ScenariosUtil().uploadAndPublishComponent(new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING));
    }

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = {14148, 14149, 14150, 14373})
    @Description("Add, Get, Delete Internal Scenario Users And delete specific user with Identity")
    public void addAndGetInternalScenarioUser() {
        UserCredentials User1 = UserUtil.getUser();
        UserCredentials User2 = UserUtil.getUser();
        List<InternalScenarioUser> userList = new ArrayList<>();
        InternalScenarioUser internalScenarioUser1 = InternalScenarioUser.builder()
            .userIdentity(User1.getUserDetails().getIdentity())
            .customerIdentity(User1.getUserDetails().getCustomerIdentity())
            .email(User1.getEmail())
            .build();
        InternalScenarioUser internalScenarioUser2 = InternalScenarioUser.builder()
            .userIdentity(User2.getUserDetails().getIdentity())
            .customerIdentity(User2.getUserDetails().getCustomerIdentity())
            .email(User2.getEmail())
            .build();

        userList.add(internalScenarioUser1);
        userList.add(internalScenarioUser2);

        InternalScenarioUserRequest internalScenarioUserRequest = InternalScenarioUserRequest.builder()
            .internalProjectUserInfo(userList)
            .build();
        ScenarioUsersResponse scenarioUsersResponse = CisDiscussionResources.addScenarioUser(
            internalScenarioUserRequest,
            componentInfoBuilder,
            ScenarioUsersResponse.class,
            HttpStatus.SC_CREATED);
        softAssertions.assertThat(scenarioUsersResponse.size()).isGreaterThan(0);

        ScenarioUsersResponse getScenarioUsersResponse = CisDiscussionResources.getInternalScenarioUsers(
            componentInfoBuilder.getComponentIdentity(),
            componentInfoBuilder.getScenarioIdentity(),
            ScenarioUsersResponse.class,
            HttpStatus.SC_OK,
            componentInfoBuilder.getUser());
        softAssertions.assertThat(getScenarioUsersResponse.size()).isGreaterThan(0);

        InternalScenarioUserRequest deleteUsersRequest = InternalScenarioUserRequest.builder()
            .internalProjectUserInfo(Collections.singletonList(internalScenarioUser2))
            .build();

        CisDiscussionResources.deleteScenarioUsers(deleteUsersRequest,
            componentInfoBuilder.getComponentIdentity(),
            componentInfoBuilder.getScenarioIdentity(),
            null,
            HttpStatus.SC_NO_CONTENT,
            componentInfoBuilder.getUser());

        CisDiscussionResources.deleteScenarioUser(
            componentInfoBuilder.getComponentIdentity(),
            componentInfoBuilder.getScenarioIdentity(),
            User1.getUserDetails().getIdentity(),
            null,
            HttpStatus.SC_NO_CONTENT,
            componentInfoBuilder.getUser());
    }

    @Test
    @TestRail(id = {22072})
    @Description("Delete last user from internal scenario")
    public void deleteLastUserFromInternalScenario() {
        InternalScenarioUserRequest internalScenarioUserRequest = InternalScenarioUserRequest.builder()
            .internalProjectUserInfo(Collections.singletonList(InternalScenarioUser.builder()
                .userIdentity(componentInfoBuilder.getUser().getUserDetails().getIdentity())
                .customerIdentity(componentInfoBuilder.getUser().getUserDetails().getCustomerIdentity())
                .email(componentInfoBuilder.getUser().getEmail())
                .build()))
            .build();
        ScenarioUsersResponse scenarioUsersResponse = CisDiscussionResources.addScenarioUser(
            internalScenarioUserRequest,
            componentInfoBuilder,
            ScenarioUsersResponse.class,
            HttpStatus.SC_CREATED);
        softAssertions.assertThat(scenarioUsersResponse.size()).isGreaterThan(0);

        CisErrorMessage cisErrorMessage = CisDiscussionResources.deleteScenarioUser(
            componentInfoBuilder.getComponentIdentity(),
            componentInfoBuilder.getScenarioIdentity(),
            componentInfoBuilder.getUser().getUserDetails().getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_CONFLICT,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisErrorMessage.getMessage()).isEqualTo("Conflict");
    }

    @Test
    @TestRail(id = {14374})
    @Description("Add a Project User With Invalid Data- Internal Scenario")
    public void addScenarioUserWithInvalidComponent() {
        InternalScenarioUserRequest internalScenarioUserRequest = InternalScenarioUserRequest.builder()
            .internalProjectUserInfo(Collections.singletonList(InternalScenarioUser.builder()
                .userIdentity(componentInfoBuilder.getUser().getUserDetails().getIdentity())
                .customerIdentity(componentInfoBuilder.getUser().getUserDetails().getCustomerIdentity())
                .email(componentInfoBuilder.getUser().getEmail())
                .build()))
            .build();
        componentInfoBuilder.setComponentIdentity("INVALID");
        CisErrorMessage cisErrorMessage = CisDiscussionResources.addScenarioUser(
            internalScenarioUserRequest,
            componentInfoBuilder,
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(cisErrorMessage.getMessage()).isEqualTo("'componentIdentity' is not a valid identity.");
    }

    @AfterEach
    public void afterTest() {
        softAssertions.assertAll();
    }

    @AfterAll
    public static void afterClass() {
        new ScenariosUtil().deleteScenario(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity(), componentInfoBuilder.getUser());
    }
}
