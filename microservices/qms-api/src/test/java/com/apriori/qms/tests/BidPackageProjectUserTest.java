package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsProjectResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserRequest;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionParameters;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionRequest;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUserResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUsersDeleteResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUsersPostResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUsersResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ParticipantsResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionsResponse;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.QmsApiTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BidPackageProjectUserTest extends TestUtil {

    private static final UserCredentials currentUser = UserUtil.getUser();
    private static final UserCredentials firstUser = UserUtil.getUser();
    private static SoftAssertions softAssertions = new SoftAssertions();
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static BidPackageProjectUsersPostResponse bidPackageProjectUserResponse;
    private static ScenarioItem scenarioItem;

    @BeforeClass
    public static void beforeClass() {
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageResponse = QmsApiTestUtils.createTestDataBidPackage(currentUser, softAssertions);
        QmsApiTestUtils.createTestDataBidPackageItem(scenarioItem, bidPackageResponse, currentUser, softAssertions);
        bidPackageProjectResponse = QmsApiTestUtils.createTestDataBidPackageProject(bidPackageResponse, currentUser, softAssertions);
        if (bidPackageResponse != null && bidPackageProjectResponse != null) {
            bidPackageProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser("DEFAULT", bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), firstUser);
        }
    }

    @AfterClass
    public static void afterClass() {
        QmsApiTestUtils.deleteTestData(scenarioItem, bidPackageResponse, currentUser);
        softAssertions.assertAll();
    }

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
    }

    @After
    public void testCleanup() {
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"15485", "13789"})
    @Description("Create and delete DEFAULT ROLE project user")
    public void createAndDeleteBidPackageDefaultProjectUser() {
        UserCredentials defaultRoleUser = UserUtil.getUser();
        String defaultRoleUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(defaultRoleUser.getEmail());
        BidPackageProjectUsersPostResponse bidPackageDefaultProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser("DEFAULT", bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), currentUser);
        softAssertions.assertThat(bidPackageDefaultProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(defaultRoleUserIdentity))).isTrue();

        String defaultRoleProjectUserIdentity = bidPackageDefaultProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(defaultRoleUser.getEmail())))
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
    @TestRail(testCaseId = {"13793"})
    @Description("get bid package project user by identity")
    public void getBidPackageProjectUser() {
        BidPackageProjectUserResponse getBidPackageProjectUserResponse = QmsBidPackageResources.getBidPackageProjectUser(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), bidPackageProjectUserResponse.getProjectUsers()
            .getSuccesses().get(0).getIdentity(), currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getProjectIdentity())
            .isEqualTo(bidPackageProjectResponse.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13790"})
    @Description("find all bid package project users")
    public void getBidPackageProjectUsers() {
        BidPackageProjectUsersResponse getBidPackageProjectUserResponse = QmsBidPackageResources.getBidPackageProjectUsers(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectUsersResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"13786"})
    @Description("Updated user role from default to admin")
    public void updateBidPackageDefaultProjectUser() {
        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUser(BidPackageProjectUserParameters.builder().role("ADMIN").build()).build();

        BidPackageProjectUserResponse updateBidPackageProjectUserResponse = QmsBidPackageResources.updateBidPackageProjectUser(bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), bidPackageProjectUserResponse.getProjectUsers()
            .getSuccesses().get(0).getIdentity(), currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageProjectUserResponse.getRole()).isEqualTo("ADMIN");
    }

    @Test
    @TestRail(testCaseId = {"14224", "14225", "14236"})
    @Description("Get list of possible users, pagination and validate json response schema")
    public void getParticipants() {
        ParticipantsResponse participantsResponse = QmsBidPackageResources.getParticipants(currentUser);

        softAssertions.assertThat(participantsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(participantsResponse.getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(participantsResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"25818"})
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
    @TestRail(testCaseId = {"25819"})
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
                .contains(String.format("Found 0 items with email %s", invalidUserEmail)) && f.getUserEmail()
                .equals(invalidUserEmail))).isTrue();

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserOneIdentity).build());
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserTwoIdentity).build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"25820"})
    @Description("Adding Bulk Project User - One of Project User already exist")
    public void createBidPackageProjectUsersAlreadyPresentUser() {
        UserCredentials newUserOne = UserUtil.getUser();
        UserCredentials newUserTwo = UserUtil.getUser();
        String newUserIdentityOne = new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail());
        String newUserIdentityTwo = new AuthUserContextUtil().getAuthUserIdentity(newUserTwo.getEmail());
        String firstUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(firstUser.getEmail());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(firstUser.getEmail()).role("DEFAULT")
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
                .contains(String.format("User with identity '%s' already exists for project with identity '%s'", firstUserIdentity, bidPackageProjectResponse.getIdentity())) && f.getUserEmail()
                .equals(firstUser.getEmail()))).isTrue();

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserOneIdentity).build());
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserTwoIdentity).build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"25830"})
    @Description("Adding Bulk Project User - UnResolved Discussion of Project after adding Project Users")
    public void createBidPackageProjectUsersUnResolvedDiscussion() {
        ScenarioDiscussionResponse scenarioDiscussionResponse = QmsApiTestUtils.createTestDataScenarioDiscussion(scenarioItem, currentUser, softAssertions);
        QmsApiTestUtils.createTestDataAddCommentToDiscussion(scenarioDiscussionResponse, currentUser, softAssertions);

        //Resolve Discussion
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status("RESOLVED").build()).build();

        if (scenarioDiscussionResponse == null) {
            softAssertions.fail("Could not create scenario discussion. Parameter is null.");
            return;
        }

        ScenarioDiscussionResponse updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(), scenarioDiscussionRequest, ScenarioDiscussionResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("RESOLVED");

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
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status("ACTIVE").build()).build();
        updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(), scenarioDiscussionRequest, ScenarioDiscussionResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("ACTIVE");

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserOneIdentity).build());
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserTwoIdentity).build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"25831"})
    @Description("Adding Bulk Project User - UnDelete Discussion of Project after adding Project Users")
    public void createBidPackageProjectUsersUnDeleteDiscussion() {
        ScenarioDiscussionResponse scenarioDiscussionResponse = QmsApiTestUtils.createTestDataScenarioDiscussion(scenarioItem, currentUser, softAssertions);
        QmsApiTestUtils.createTestDataAddCommentToDiscussion(scenarioDiscussionResponse, currentUser, softAssertions);

        //Delete Discussion
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status("DELETE").build()).build();

        if (scenarioDiscussionResponse == null) {
            softAssertions.fail("Could not create scenario discussion. Parameter is null.");
            return;
        }

        ScenarioDiscussionResponse updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(), scenarioDiscussionRequest, ScenarioDiscussionResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("DELETE");

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
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status("ACTIVE").build()).build();
        updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(), scenarioDiscussionRequest, ScenarioDiscussionResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("ACTIVE");

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserOneIdentity).build());
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserTwoIdentity).build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"25958"})
    @Description("Adding Bulk Project Users - One of user does not exist in database")
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
                .contains(String.format("Found 0 items with email %s", invalidUserEmail)) && f.getUserEmail()
                .equals(invalidUserEmail))).isTrue();

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserOneIdentity).build());
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(projectUserTwoIdentity).build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"26438", "26824"})
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
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        UserCredentials secondUser = UserUtil.getUser();
        String secondUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(secondUser.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(secondUserIdentity)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
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
            softAssertions.assertThat(updateDiscussionResponse.getStatus()).isEqualTo("ACTIVE");
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
    @TestRail(testCaseId = {"24052"})
    @Description("Deleting Bulk PU - deletes Participants from discussions")
    public void deleteBulkProjectUsersWithDiscussions() {
        //Create 10 discussions to the project
        for (int d = 0; d < 10; d++) {
            ScenarioDiscussionResponse csdResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
            softAssertions.assertThat(csdResponse.getStatus()).isEqualTo("ACTIVE");
        }

        //Add 3 Users to the project
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

        //Validate that added bulk PU - adds Participants to Discussions by getting the list of discussions related to this project item
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

        //Remove 3 users by using Project User Identities
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

        //Validate that deleted bulk PU - deletes Participants from Discussions by getting the list of discussions related to this project item
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
}
