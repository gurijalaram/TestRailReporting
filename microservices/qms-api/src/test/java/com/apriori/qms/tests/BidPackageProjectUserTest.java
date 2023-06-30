package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
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
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
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
import java.util.List;

public class BidPackageProjectUserTest extends TestUtil {

    private static SoftAssertions softAssertions = new SoftAssertions();
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static BidPackageProjectUsersPostResponse bidPackageProjectUserResponse;
    private static ScenarioItem scenarioItem;
    private static final UserCredentials currentUser = UserUtil.getUser();
    private static final UserCredentials firstUser = UserUtil.getUser();

    @BeforeClass
    public static void beforeClass() {
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting",
            currentUser);
        bidPackageResponse = QmsApiTestUtils.createTestDataBidPackage(currentUser, softAssertions);
        QmsApiTestUtils.createTestDataBidPackageItem(scenarioItem, bidPackageResponse, currentUser, softAssertions);
        bidPackageProjectResponse = QmsApiTestUtils.createTestDataBidPackageProject(bidPackageResponse, currentUser,
            softAssertions);
        if (bidPackageResponse != null && bidPackageProjectResponse != null) {
            bidPackageProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser("DEFAULT",
                bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), firstUser);
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
        UserCredentials defaultUser = UserUtil.getUser();
        BidPackageProjectUsersPostResponse bidPackageDefaultProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(
            "DEFAULT", bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), defaultUser);
        softAssertions.assertThat(
                bidPackageDefaultProjectUserResponse.getProjectUsers().getSuccesses().get(0).getProjectIdentity())
            .isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageProjectUsersDeleteResponse deleteUserResponse = QmsBidPackageResources.deleteBidPackageProjectUser(
            Collections.singletonList(BidPackageProjectUserParameters
                .builder()
                .identity(bidPackageDefaultProjectUserResponse.getProjectUsers().getSuccesses().get(0).getIdentity())
                .build()), bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), defaultUser);

        softAssertions.assertThat(deleteUserResponse.getProjectUsers().getSuccesses().stream().anyMatch(
                i -> i.getIdentity()
                    .equals(bidPackageDefaultProjectUserResponse.getProjectUsers().getSuccesses().get(0).getIdentity())))
            .isTrue();
    }

    @Test
    @TestRail(testCaseId = {"13782", "13788"})
    @Description("Create and delete ADMIN ROLE project user")
    public void createAndDeleteBidPackageAdminProjectUser() {
        UserCredentials adminUser = UserUtil.getUser();
        BidPackageProjectUsersPostResponse bidPackageAdminProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(
            "ADMIN", bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), adminUser);

        softAssertions
            .assertThat(bidPackageAdminProjectUserResponse.getProjectUsers().getSuccesses().get(0).getProjectIdentity())
            .isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageProjectUsersDeleteResponse deleteUserResponse = QmsBidPackageResources.deleteBidPackageProjectUser(
            Collections.singletonList(BidPackageProjectUserParameters
                .builder()
                .identity(bidPackageAdminProjectUserResponse.getProjectUsers().getSuccesses().get(0).getIdentity())
                .build()), bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), adminUser);

        softAssertions.assertThat(deleteUserResponse.getProjectUsers().getSuccesses().stream().anyMatch(
                i -> i.getIdentity()
                    .equals(bidPackageAdminProjectUserResponse.getProjectUsers().getSuccesses().get(0).getIdentity())))
            .isTrue();
    }

    @Test
    @TestRail(testCaseId = {"13793"})
    @Description("get bid package project user by identity")
    public void getBidPackageProjectUser() {
        BidPackageProjectUserResponse getBidPackageProjectUserResponse = QmsBidPackageResources.getBidPackageProjectUser(
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(),
            bidPackageProjectUserResponse.getProjectUsers().getSuccesses().get(0).getIdentity(), currentUser,
            BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getProjectIdentity())
            .isEqualTo(bidPackageProjectResponse.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13790"})
    @Description("find all bid package project users")
    public void getBidPackageProjectUsers() {
        BidPackageProjectUsersResponse getBidPackageProjectUserResponse = QmsBidPackageResources.getBidPackageProjectUsers(
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser,
            BidPackageProjectUsersResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"13786"})
    @Description("Updated user role from default to admin")
    public void updateBidPackageDefaultProjectUser() {
        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest
            .builder().projectUser(BidPackageProjectUserParameters
                .builder().role("ADMIN")
                .build())
            .build();

        BidPackageProjectUserResponse updateBidPackageProjectUserResponse = QmsBidPackageResources.updateBidPackageProjectUser(
            bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectUserResponse.getProjectUsers().getSuccesses().get(0).getIdentity(), currentUser,
            BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

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
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(newUserTwo.getEmail()).role("DEFAULT")
            .build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest
            .builder().projectUsers(usersList)
            .build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(
            bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);

        //Success
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityOne))).isTrue();
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityTwo))).isTrue();

        //Failure
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getFailures().stream().anyMatch(
            f -> f.getError().contains(
                String.format("User with identity '%s' already exists for project with identity '%s'",
                    newUserIdentityOne, bidPackageProjectResponse.getIdentity())) && f.getUserEmail()
                .equals(newUserOne.getEmail()))).isTrue();

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters
            .builder().identity(newUserIdentityOne)
            .build());
        userIdentityList.add(BidPackageProjectUserParameters
            .builder().identity(newUserIdentityTwo)
            .build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), currentUser);
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
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(invalidUserEmail).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(newUserTwo.getEmail()).role("DEFAULT")
            .build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest
            .builder().projectUsers(usersList)
            .build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(
            bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);

        //Success
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityOne))).isTrue();
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityTwo))).isTrue();

        //Failure
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getFailures().stream().anyMatch(
            f -> f.getError().contains(String.format("Found 0 items with email %s", invalidUserEmail)) && f
                .getUserEmail().equals(invalidUserEmail))).isTrue();

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters
            .builder().identity(newUserIdentityOne)
            .build());
        userIdentityList.add(BidPackageProjectUserParameters
            .builder().identity(newUserIdentityTwo)
            .build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), currentUser);
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
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(firstUser.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(newUserTwo.getEmail()).role("DEFAULT")
            .build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest
            .builder().projectUsers(usersList)
            .build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(
            bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);

        //Success
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityOne))).isTrue();
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityTwo))).isTrue();

        //Failure
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getFailures().stream().anyMatch(
            f -> f.getError().contains(
                String.format("User with identity '%s' already exists for project with identity '%s'",
                    firstUserIdentity, bidPackageProjectResponse.getIdentity())) && f.getUserEmail()
                .equals(firstUser.getEmail()))).isTrue();

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters
            .builder().identity(newUserIdentityOne)
            .build());
        userIdentityList.add(BidPackageProjectUserParameters
            .builder().identity(newUserIdentityTwo)
            .build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"25830"})
    @Description("Adding Bulk Project User - UnResolved Discussion of Project after adding Project Users")
    public void createBidPackageProjectUsersUnResolvedDiscussion() {
        ScenarioDiscussionResponse scenarioDiscussionResponse = QmsApiTestUtils.createTestDataScenarioDiscussion(
            scenarioItem, currentUser, softAssertions);
        QmsApiTestUtils.createTestDataAddCommentToDiscussion(scenarioDiscussionResponse, currentUser, softAssertions);

        //Resolve Discussion
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest
            .builder().scenarioDiscussion(ScenarioDiscussionParameters
                .builder().status("RESOLVED")
                .build())
            .build();

        if (scenarioDiscussionResponse == null) {
            softAssertions.fail("Could not create scenario discussion. Parameter is null.");
            return;
        }

        ScenarioDiscussionResponse updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(
            scenarioDiscussionResponse.getIdentity(), scenarioDiscussionRequest, ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("RESOLVED");

        //Add Users
        UserCredentials newUserOne = UserUtil.getUser();
        UserCredentials newUserTwo = UserUtil.getUser();
        String newUserIdentityOne = new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail());
        String newUserIdentityTwo = new AuthUserContextUtil().getAuthUserIdentity(newUserTwo.getEmail());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(newUserTwo.getEmail()).role("DEFAULT")
            .build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest
            .builder().projectUsers(usersList)
            .build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(
            bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);

        //Success
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityOne))).isTrue();
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityTwo))).isTrue();

        //Activate Discussion
        scenarioDiscussionRequest = ScenarioDiscussionRequest
            .builder().scenarioDiscussion(ScenarioDiscussionParameters
                .builder().status("ACTIVE")
                .build())
            .build();
        updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(
            scenarioDiscussionResponse.getIdentity(), scenarioDiscussionRequest, ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("ACTIVE");

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters
            .builder().identity(newUserIdentityOne)
            .build());
        userIdentityList.add(BidPackageProjectUserParameters
            .builder().identity(newUserIdentityTwo)
            .build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"25831"})
    @Description("Adding Bulk Project User - UnDelete Discussion of Project after adding Project Users")
    public void createBidPackageProjectUsersUnDeleteDiscussion() {
        ScenarioDiscussionResponse scenarioDiscussionResponse = QmsApiTestUtils.createTestDataScenarioDiscussion(
            scenarioItem, currentUser, softAssertions);
        QmsApiTestUtils.createTestDataAddCommentToDiscussion(scenarioDiscussionResponse, currentUser, softAssertions);

        //Delete Discussion
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest
            .builder().scenarioDiscussion(ScenarioDiscussionParameters
                .builder().status("DELETE")
                .build())
            .build();

        if (scenarioDiscussionResponse == null) {
            softAssertions.fail("Could not create scenario discussion. Parameter is null.");
            return;
        }

        ScenarioDiscussionResponse updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(
            scenarioDiscussionResponse.getIdentity(), scenarioDiscussionRequest, ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("DELETE");

        //Add Users
        UserCredentials newUserOne = UserUtil.getUser();
        UserCredentials newUserTwo = UserUtil.getUser();
        String newUserIdentityOne = new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail());
        String newUserIdentityTwo = new AuthUserContextUtil().getAuthUserIdentity(newUserTwo.getEmail());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(newUserTwo.getEmail()).role("DEFAULT")
            .build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest
            .builder().projectUsers(usersList)
            .build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(
            bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);

        //Success
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityOne))).isTrue();
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityTwo))).isTrue();

        //Activate Discussion
        scenarioDiscussionRequest = ScenarioDiscussionRequest
            .builder().scenarioDiscussion(ScenarioDiscussionParameters
                .builder().status("ACTIVE")
                .build())
            .build();
        updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(
            scenarioDiscussionResponse.getIdentity(), scenarioDiscussionRequest, ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("ACTIVE");

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters
            .builder().identity(newUserIdentityOne)
            .build());
        userIdentityList.add(BidPackageProjectUserParameters
            .builder().identity(newUserIdentityTwo)
            .build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"25958"})
    @Description("Adding Bulk Project Users - One of user doesn’t exist in database")
    public void createBidPackageProjectUsersInvalid() {
        String invalidUserEmail = "qwerty@apriori.com";
        UserCredentials newUserOne = UserUtil.getUser();
        UserCredentials newUserTwo = UserUtil.getUser();
        String newUserIdentityOne = new AuthUserContextUtil().getAuthUserIdentity(newUserOne.getEmail());
        String newUserIdentityTwo = new AuthUserContextUtil().getAuthUserIdentity(newUserTwo.getEmail());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(invalidUserEmail).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(newUserOne.getEmail()).role("DEFAULT")
            .build());
        usersList.add(BidPackageProjectUserParameters
            .builder().userEmail(newUserTwo.getEmail()).role("DEFAULT")
            .build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest
            .builder().projectUsers(usersList)
            .build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(
            bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);

        //Success
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityOne))).isTrue();
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityTwo))).isTrue();

        //Failure
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getFailures().stream().anyMatch(
            f -> f.getError().contains(String.format("Found 0 items with email %s", invalidUserEmail)) && f
                .getUserEmail().equals(invalidUserEmail))).isTrue();

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters
            .builder().identity(newUserIdentityOne)
            .build());
        userIdentityList.add(BidPackageProjectUserParameters
            .builder().identity(newUserIdentityTwo)
            .build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), currentUser);
    }
}
