package com.apriori.qms.api.tests;

import com.apriori.qds.api.enums.DiscussionStatus;
import com.apriori.qms.api.controller.QmsBidPackageResources;
import com.apriori.qms.api.controller.QmsComponentResources;
import com.apriori.qms.api.controller.QmsProjectResources;
import com.apriori.qms.api.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.api.models.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.api.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.api.models.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.api.models.request.bidpackage.BidPackageProjectUserRequest;
import com.apriori.qms.api.models.request.scenariodiscussion.ProjectUserParameters;
import com.apriori.qms.api.models.request.scenariodiscussion.ProjectUserRequest;
import com.apriori.qms.api.models.request.scenariodiscussion.ScenarioDiscussionParameters;
import com.apriori.qms.api.models.request.scenariodiscussion.ScenarioDiscussionRequest;
import com.apriori.qms.api.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.api.models.response.bidpackage.BidPackageProjectUserResponse;
import com.apriori.qms.api.models.response.bidpackage.BidPackageProjectUsersDeleteResponse;
import com.apriori.qms.api.models.response.bidpackage.BidPackageProjectUsersPostResponse;
import com.apriori.qms.api.models.response.bidpackage.BidPackageProjectUsersResponse;
import com.apriori.qms.api.models.response.bidpackage.BidPackageResponse;
import com.apriori.qms.api.models.response.scenariodiscussion.ParticipantsResponse;
import com.apriori.qms.api.models.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.qms.api.models.response.scenariodiscussion.ScenarioDiscussionsResponse;
import com.apriori.qms.api.utils.QmsApiTestUtils;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
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
public class BidPackageProjectUserTest extends TestUtil {

    private static final UserCredentials currentUser = UserUtil.getUser();
    private static final UserCredentials firstUser = UserUtil.getUser();
    private static SoftAssertions softAssertions = new SoftAssertions();
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static BidPackageProjectUsersPostResponse bidPackageProjectUserResponse;
    private static ScenarioItem scenarioItem;

    @BeforeAll
    public static void beforeClass() {
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageResponse = QmsApiTestUtils.createTestDataBidPackage(currentUser, softAssertions);
        QmsApiTestUtils.createTestDataBidPackageItem(scenarioItem, bidPackageResponse, currentUser, softAssertions);
        bidPackageProjectResponse = QmsApiTestUtils.createTestDataBidPackageProject(bidPackageResponse, currentUser, softAssertions);
        if (bidPackageResponse != null && bidPackageProjectResponse != null) {
            bidPackageProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser("DEFAULT", bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), firstUser);
        }
    }

    @AfterAll
    public static void afterClass() {
        QmsApiTestUtils.deleteTestData(scenarioItem, bidPackageResponse, currentUser);
        softAssertions.assertAll();
    }

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
    }

    @AfterEach
    public void testCleanup() {
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {15485, 13789})
    @Description("Create and delete DEFAULT ROLE project user")
    public void createAndDeleteBidPackageDefaultProjectUser() {
        String defaultRoleProjectUserIdentity = bidPackageProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(firstUser.getEmail())))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);

        BidPackageProjectUsersDeleteResponse deleteUserResponse = QmsBidPackageResources.deleteBidPackageProjectUser(Collections.singletonList(BidPackageProjectUserParameters.builder()
            .identity(defaultRoleProjectUserIdentity)
            .build()), bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
        softAssertions.assertThat(deleteUserResponse.getProjectUsers().getSuccesses().stream()
                .anyMatch(i -> i.getIdentity()
                    .equals(defaultRoleProjectUserIdentity)))
            .isTrue();
    }

    @Test
    @TestRail(id = {13793})
    @Description("get bid package project user by identity")
    public void getBidPackageProjectUser() {
        BidPackageProjectUserResponse getBidPackageProjectUserResponse = QmsBidPackageResources.getBidPackageProjectUser(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), bidPackageProjectUserResponse.getProjectUsers()
            .getSuccesses().get(0).getIdentity(), currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getProjectIdentity())
            .isEqualTo(bidPackageProjectResponse.getIdentity());
    }

    @Test
    @TestRail(id = {13790})
    @Description("find all bid package project users")
    public void getBidPackageProjectUsers() {
        BidPackageProjectUsersResponse getBidPackageProjectUserResponse = QmsBidPackageResources.getBidPackageProjectUsers(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectUsersResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {13786})
    @Description("Updated user role from default to admin")
    public void updateBidPackageDefaultProjectUser() {
        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUser(BidPackageProjectUserParameters.builder().role("ADMIN").build()).build();

        BidPackageProjectUserResponse updateBidPackageProjectUserResponse = QmsBidPackageResources.updateBidPackageProjectUser(bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), bidPackageProjectUserResponse.getProjectUsers()
            .getSuccesses().get(0).getIdentity(), currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageProjectUserResponse.getRole()).isEqualTo("ADMIN");
    }

    @Test
    @TestRail(id = {14224, 14225, 14236})
    @Description("Get list of possible users, pagination and validate json response schema")
    public void getParticipants() {
        ParticipantsResponse participantsResponse = QmsBidPackageResources.getParticipants(currentUser);

        softAssertions.assertThat(participantsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(participantsResponse.getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(participantsResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {25818})
    @Description("Adding duplicate Bulk project users")
    public void createBidPackageProjectUsersDuplicate() {
        UserCredentials newUserOne = UserUtil.getUser();
        UserCredentials newUserTwo = UserUtil.getUser();
        String newUserIdentityOne = new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail());
        String newUserIdentityTwo = new AuthUserContextUtil().getAuthUserIdentity(newUserTwo.getEmail());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(newUserTwo.getEmail()).role("DEFAULT")
            .build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(usersList).build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);
        String projectUserOneIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail())))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);
        String projectUserTwoIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(newUserTwo.getEmail())))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);

        //Success
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityOne))).isTrue();
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityTwo))).isTrue();

        //Failure
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getFailures().stream()
            .anyMatch(f -> f.getError()
                .contains(String.format("User with identity '%s' already exists for project with identity '%s'", newUserIdentityOne, bidPackageProjectResponse.getIdentity())) && f.getUserEmail()
                .equals(newUserOne.getEmail()))).isTrue();

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserOneIdentity).build());
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserTwoIdentity).build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {25819})
    @Description("Adding Bulk Project Users - One of Project User with an invalid email")
    public void createBidPackageProjectUsersInvalidEmail() {
        String invalidUserEmail = "invalid_email.com";
        UserCredentials newUserOne = UserUtil.getUser();
        UserCredentials newUserTwo = UserUtil.getUser();
        String newUserIdentityOne = new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail());
        String newUserIdentityTwo = new AuthUserContextUtil().getAuthUserIdentity(newUserTwo.getEmail());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(invalidUserEmail).role("DEFAULT").build());
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(newUserTwo.getEmail()).role("DEFAULT")
            .build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(usersList).build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);
        String projectUserOneIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail())))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);
        String projectUserTwoIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(newUserTwo.getEmail())))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);

        //Success
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityOne))).isTrue();
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityTwo))).isTrue();

        //Failure
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getFailures().stream()
            .anyMatch(f -> f.getError()
                .contains(String.format("User with email:%snot found in system", invalidUserEmail)) && f.getUserEmail()
                .equals(invalidUserEmail))).isTrue();

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserOneIdentity).build());
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserTwoIdentity).build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {25820})
    @Description("Adding Bulk Project User - One of Project User already exist")
    public void createBidPackageProjectUsersAlreadyPresentUser() {
        UserCredentials newUserOne = UserUtil.getUser();
        String newUserIdentityOne = new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail());
        String firstUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(firstUser.getEmail());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(firstUser.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(usersList).build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);

        String projectUserOneIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail())))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);

        //Success
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityOne))).isTrue();

        //Failure
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getFailures().stream()
            .anyMatch(f -> f.getError()
                .contains(String.format("User with identity '%s' already exists for project with identity '%s'", firstUserIdentity, bidPackageProjectResponse.getIdentity())) && f.getUserEmail()
                .equals(firstUser.getEmail()))).isTrue();

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserOneIdentity).build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {25830})
    @Description("Adding Bulk Project User - UnResolved Discussion of Project after adding Project Users")
    public void createBidPackageProjectUsersUnResolvedDiscussion() {
        ScenarioDiscussionResponse scenarioDiscussionResponse = QmsApiTestUtils.createTestDataScenarioDiscussion(scenarioItem, currentUser, softAssertions);
        QmsApiTestUtils.createTestDataAddCommentToDiscussion(scenarioDiscussionResponse, currentUser, softAssertions);

        //Resolve Discussion
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status(DiscussionStatus.RESOLVED.getDiscussionStatus()).build()).build();

        if (scenarioDiscussionResponse == null) {
            softAssertions.fail("Could not create scenario discussion. Parameter is null.");
            return;
        }

        ScenarioDiscussionResponse updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(), scenarioDiscussionRequest, ScenarioDiscussionResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo(DiscussionStatus.RESOLVED.getDiscussionStatus());

        //Add Users
        UserCredentials newUserOne = UserUtil.getUser();
        UserCredentials newUserTwo = UserUtil.getUser();
        String newUserIdentityOne = new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail());
        String newUserIdentityTwo = new AuthUserContextUtil().getAuthUserIdentity(newUserTwo.getEmail());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(newUserTwo.getEmail()).role("DEFAULT")
            .build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(usersList).build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);
        String projectUserOneIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail())))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);
        String projectUserTwoIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(newUserTwo.getEmail())))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);

        //Success
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityOne))).isTrue();
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityTwo))).isTrue();

        //Activate Discussion
        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status(DiscussionStatus.ACTIVE.getDiscussionStatus()).build()).build();
        updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(), scenarioDiscussionRequest, ScenarioDiscussionResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo(DiscussionStatus.ACTIVE.getDiscussionStatus());

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserOneIdentity).build());
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserTwoIdentity).build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {25831})
    @Issue("COL-2029")
    @Description("Adding Bulk Project User - UnDelete Discussion of Project after adding Project Users")
    public void createBidPackageProjectUsersUnDeleteDiscussion() {
        ScenarioDiscussionResponse scenarioDiscussionResponse = QmsApiTestUtils.createTestDataScenarioDiscussion(scenarioItem, currentUser, softAssertions);
        QmsApiTestUtils.createTestDataAddCommentToDiscussion(scenarioDiscussionResponse, currentUser, softAssertions);

        //Delete Discussion
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status(DiscussionStatus.DELETED.getDiscussionStatus()).build()).build();

        ScenarioDiscussionResponse updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(), scenarioDiscussionRequest, ScenarioDiscussionResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo(DiscussionStatus.DELETED.getDiscussionStatus());

        //Add Users
        UserCredentials newUserOne = UserUtil.getUser();
        String newUserIdentityOne = new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(usersList).build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);
        String projectUserOneIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail())))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);

        //Success
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityOne))).isTrue();

        //Activate Discussion
        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status(DiscussionStatus.ACTIVE.getDiscussionStatus()).build()).build();
        updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(), scenarioDiscussionRequest, ScenarioDiscussionResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo(DiscussionStatus.ACTIVE.getDiscussionStatus());

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserOneIdentity).build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {25958})
    @Description("Adding Bulk Project Users - One of user doesn’t exist in database")
    public void createBidPackageProjectUsersInvalid() {
        String invalidUserEmail = "qwerty@apriori.com";
        UserCredentials newUserOne = UserUtil.getUser();
        UserCredentials newUserTwo = UserUtil.getUser();
        String newUserIdentityOne = new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail());
        String newUserIdentityTwo = new AuthUserContextUtil().getAuthUserIdentity(newUserTwo.getEmail());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(invalidUserEmail).role("DEFAULT").build());
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(newUserTwo.getEmail()).role("DEFAULT")
            .build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(usersList).build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);
        String projectUserOneIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail())))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);
        String projectUserTwoIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(newUserTwo.getEmail())))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);

        //Success
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityOne))).isTrue();
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityTwo))).isTrue();

        //Failure
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getFailures().stream()
            .anyMatch(f -> f.getError()
                .contains(String.format("User with email:%snot found in system", invalidUserEmail)) && f.getUserEmail()
                .equals(invalidUserEmail))).isTrue();

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserOneIdentity).build());
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserTwoIdentity).build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {26438, 26824})
    @Issue("COL-1900")
    @Description("Verify system is able to delete Project Users.\n" +
        "Verify Admin User is able delete PUs that are Assigned to discussions")
    public void deleteOneProjectUser() {
        ScenarioItem scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        UserCredentials firstUser = UserUtil.getUser();
        String firstUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(firstUser.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(firstUserIdentity)
            .customerIdentity(PropertiesContext.get("${customer}.${env}.customer_identity"))
            .build());

        UserCredentials secondUser = UserUtil.getUser();
        String secondUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(secondUser.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(secondUserIdentity)
            .customerIdentity(PropertiesContext.get("${customer}.${env}.customer_identity"))
            .build());

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new HashMap<>(),
            itemsList,
            usersList,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getUsers().size()).isEqualTo(3);
        if (softAssertions.wasSuccess()) {
            //Verify system can Delete one PU
            String projectFirstUserIdentity = bppResponse.getUsers().stream()
                .filter(u -> u.getUserIdentity().equals(firstUserIdentity))
                .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);
            String projectSecondUserIdentity = bppResponse.getUsers().stream()
                .filter(u -> u.getUserIdentity().equals(secondUserIdentity))
                .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);

            BidPackageProjectUsersDeleteResponse deleteUserResponse = QmsBidPackageResources.deleteBidPackageProjectUser(Collections.singletonList(
                BidPackageProjectUserParameters.builder()
                    .identity(projectFirstUserIdentity)
                    .build()), bppResponse.getBidPackageIdentity(), bppResponse.getIdentity(), currentUser);

            softAssertions.assertThat(deleteUserResponse.getProjectUsers().getSuccesses().stream()
                    .anyMatch(i -> i.getIdentity()
                        .equals(projectFirstUserIdentity)))
                .isTrue();
            softAssertions.assertThat(deleteUserResponse.getProjectUsers().getFailures().size()).isZero();

            //Create discussion and assign it to PU
            ScenarioDiscussionResponse csdResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
            ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
                .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                    .assigneeEmail(secondUser.getEmail()).build()).build();
            ScenarioDiscussionResponse updateDiscussionResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(csdResponse.getIdentity(),
                scenarioDiscussionRequest,
                ScenarioDiscussionResponse.class,
                HttpStatus.SC_OK, currentUser);
            softAssertions.assertThat(updateDiscussionResponse.getStatus()).isEqualTo(DiscussionStatus.ACTIVE.getDiscussionStatus());
            softAssertions.assertThat(updateDiscussionResponse.getAssigneeUserIdentity()).isEqualTo(secondUserIdentity);

            //Verify Admin User is able to delete PU that are Assigned to discussions
            List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
            userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectSecondUserIdentity).build());
            deleteUserResponse = QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bppResponse.getBidPackageIdentity(), bppResponse.getIdentity(), currentUser);
            softAssertions.assertThat(deleteUserResponse.getProjectUsers().getSuccesses().stream()
                    .anyMatch(i -> i.getIdentity()
                        .equals(projectSecondUserIdentity)))
                .isTrue();
        }
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);
    }

    @Test
    @TestRail(id = 24052)
    @Description("Deleting Bulk PU - deletes Participants from discussions")
    public void deleteBulkProjectUsersWithDiscussions() {
        for (int d = 0; d < 10; d++) {
            ScenarioDiscussionResponse csdResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
            softAssertions.assertThat(csdResponse.getStatus()).isEqualTo(DiscussionStatus.ACTIVE.getDiscussionStatus());
        }

        String firstUserEmail = UserUtil.getUser().getEmail();
        String secondUserEmail = UserUtil.getUser().getEmail();
        String thirdUserEmail = UserUtil.getUser().getEmail();
        String firstUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(firstUserEmail);
        String secondUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(secondUserEmail);
        String thirdUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(thirdUserEmail);

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(firstUserEmail).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(secondUserEmail).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(thirdUserEmail).role("DEFAULT")
            .build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(usersList).build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);
        String firstProjectUserIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(firstUserIdentity))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);
        String secondProjectUserIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(secondUserIdentity))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);
        String thirdProjectUserIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(thirdUserIdentity))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);

        String[] params = {"componentIdentity[EQ]," + scenarioItem.getComponentIdentity(), "scenarioIdentity[EQ]," + scenarioItem.getScenarioIdentity(), "pageNumber,1"};
        ScenarioDiscussionsResponse discussionsResponse = QmsScenarioDiscussionResources.getScenarioDiscussionsWithParameters(params, ScenarioDiscussionsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(discussionsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(discussionsResponse.getItems().stream()
                    .allMatch(d -> d.getParticipants().stream()
                        .anyMatch(p -> p.getUserIdentity().equals(firstUserIdentity) ||
                            p.getUserIdentity().equals(secondUserIdentity) ||
                            p.getUserIdentity().equals(thirdUserIdentity))))
                .isTrue();
        }

        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(firstProjectUserIdentity).build());
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(secondProjectUserIdentity).build());
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(thirdProjectUserIdentity).build());
        BidPackageProjectUsersDeleteResponse deleteUserResponse = QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
        softAssertions.assertThat(deleteUserResponse.getProjectUsers().getSuccesses().stream()
                .anyMatch(i -> i.getIdentity().equals(firstProjectUserIdentity) ||
                    i.getIdentity().equals(secondProjectUserIdentity) ||
                    i.getIdentity().equals(thirdProjectUserIdentity)))
            .isTrue();

        discussionsResponse = QmsScenarioDiscussionResources.getScenarioDiscussionsWithParameters(params, ScenarioDiscussionsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(discussionsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(discussionsResponse.getItems().stream()
                    .allMatch(d -> d.getParticipants().stream()
                        .noneMatch(p -> p.getUserIdentity().equals(firstUserIdentity) &&
                            p.getUserIdentity().equals(secondUserIdentity) &&
                            p.getUserIdentity().equals(thirdUserIdentity))))
                .isTrue();
        }
    }

    @Test
    @TestRail(id = {26934, 15476})
    @Description("Verify that using un-sharing mechanism API, user can remove himself as Project User")
    public void unSharingSelfProjectUserWithDiscussions() {
        UserCredentials assigneeUser = UserUtil.getUser();
        String assigneeUserEmail = assigneeUser.getEmail();
        String assigneeUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(assigneeUserEmail);

        ScenarioDiscussionResponse csdResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        softAssertions.assertThat(csdResponse.getStatus()).isEqualTo("ACTIVE");
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .assigneeEmail(assigneeUserEmail).build()).build();
        ScenarioDiscussionResponse updateDiscussionResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(csdResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK,
            currentUser);
        softAssertions.assertThat(updateDiscussionResponse.getStatus()).isEqualTo(DiscussionStatus.ACTIVE.getDiscussionStatus());
        softAssertions.assertThat(updateDiscussionResponse.getAssigneeUserIdentity()).isEqualTo(assigneeUserIdentity);
        softAssertions.assertThat(updateDiscussionResponse.getParticipants().stream()
            .anyMatch(p -> p.getUserIdentity().equals(assigneeUserIdentity))).isTrue();

        ProjectUserRequest deleteProjectUserRequest = ProjectUserRequest.builder()
            .users(Collections.singletonList(ProjectUserParameters.builder()
                .email(assigneeUserEmail)
                .build()))
            .build();
        QmsComponentResources.deleteComponentScenarioUser(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), deleteProjectUserRequest, assigneeUser);

        ScenarioDiscussionResponse getScenarioDiscussionResponse = QmsScenarioDiscussionResources.getScenarioDiscussion(csdResponse.getIdentity(), ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(getScenarioDiscussionResponse.getIdentity()).isEqualTo(csdResponse.getIdentity());
        softAssertions.assertThat(getScenarioDiscussionResponse.getParticipants().stream()
            .noneMatch(p -> p.getUserIdentity().equals(assigneeUserIdentity))).isTrue();
    }
}
