package com.apriori;

import com.apriori.authorization.response.ApwErrorMessage;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.ScenarioItem;
import com.apriori.properties.PropertiesContext;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsComponentResources;
import com.apriori.qms.controller.QmsProjectResources;
import com.apriori.qms.models.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.models.request.bidpackage.BidPackageProjectItem;
import com.apriori.qms.models.request.bidpackage.BidPackageProjectParameters;
import com.apriori.qms.models.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qms.models.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.models.request.bidpackage.BidPackageProjectUserRequest;
import com.apriori.qms.models.request.scenariodiscussion.ProjectUserParameters;
import com.apriori.qms.models.request.scenariodiscussion.ProjectUserRequest;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectItemsBulkResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectItemsResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectUserResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectUsersPostResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectUsersResponse;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class QmsProjectUserPermissionsTest extends TestUtil {
    private static final UserCredentials nonProjectUser = UserUtil.getUser();
    private static final UserCredentials currentOwnerUser = UserUtil.getUser();
    private static final String currentOwnerUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentOwnerUser.getEmail());
    private static final String nonProjectUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(nonProjectUser.getEmail());
    private static SoftAssertions softAssertions = new SoftAssertions();
    private static ScenarioItem scenarioItem;
    private static BidPackageProjectResponse projectResponse;
    private static String currentProjectUserIdentity;
    private static UserCredentials firstUser;
    private static UserCredentials thirdUser;
    private static String firstUserIdentity;
    private static String secondUserIdentity;
    private static String thirdUserIdentity;
    private static String firstProjectUserIdentity;

    @BeforeAll
    public static void beforeClass() {
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentOwnerUser);
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        List<BidPackageProjectUserParameters> bidPackageProjectUsersList = new ArrayList<>();
        firstUser = UserUtil.getUser();
        firstUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(firstUser.getEmail());
        bidPackageProjectUsersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(firstUserIdentity)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        UserCredentials secondUser = UserUtil.getUser();
        secondUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(secondUser.getEmail());
        bidPackageProjectUsersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(secondUserIdentity)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        thirdUser = UserUtil.getUser();
        thirdUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(secondUser.getEmail());
        bidPackageProjectUsersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(thirdUserIdentity)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        projectResponse = QmsProjectResources.createProject(new HashMap<>(),
            itemsList,
            bidPackageProjectUsersList,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentOwnerUser);

        softAssertions.assertThat(projectResponse.getUsers().size()).isEqualTo(3);
        softAssertions.assertThat(projectResponse.getUsers().stream()
                .anyMatch(u -> u.getRole().equals("ADMIN") &&
                    u.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(currentOwnerUser.getEmail()))))
            .isTrue();

        if (softAssertions.wasSuccess()) {
            currentProjectUserIdentity = projectResponse.getUsers().stream()
                .filter(pu -> pu.getUserIdentity().equals(currentOwnerUserIdentity))
                .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);
            firstProjectUserIdentity = projectResponse.getUsers().stream()
                .filter(pu -> pu.getUserIdentity().equals(firstUserIdentity))
                .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);
        }
    }

    @AfterAll
    public static void afterClass() {
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentOwnerUser);
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
    @TestRail(id = {26629})
    @Description("Owner: Update project details")
    public void updateProjectAttributesByOwner() {
        //Update Description
        String descriptionNew = new GenerateStringUtil().getRandomStringSpecLength(12);
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .description(descriptionNew).build())
            .build();
        BidPackageProjectResponse bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), currentOwnerUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(bppUpdateResponse.getDescription()).isEqualTo(descriptionNew);

        //Update Name
        String nameNew = new GenerateStringUtil().getRandomStringSpecLength(12);
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(nameNew).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), currentOwnerUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(bppUpdateResponse.getDescription()).isEqualTo(descriptionNew);
        softAssertions.assertThat(bppUpdateResponse.getName()).isEqualTo(nameNew);

        //Update Status
        String statusNew = "PURCHASED";
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status(statusNew).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), currentOwnerUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(bppUpdateResponse.getStatus()).isEqualTo(statusNew);

        //Update DueAt
        String dueAtNew = DateUtil.getDateDaysAfter(10, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .dueAt(dueAtNew).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), currentOwnerUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(bppUpdateResponse.getDueAt().format(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ)).isEqualTo(dueAtNew);

        //Update Owner
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .owner(firstUserIdentity).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), currentOwnerUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(bppUpdateResponse.getOwner()).isEqualTo(firstUserIdentity);
        softAssertions.assertThat(bppUpdateResponse.getUsers().stream()
                .anyMatch(u -> u.getRole().equals("ADMIN") &&
                    u.getUserIdentity().equals(firstUserIdentity)))
            .isTrue();

        //Update Owner to currentUser
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .owner(currentOwnerUserIdentity).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), firstUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(bppUpdateResponse.getOwner()).isEqualTo(currentOwnerUserIdentity);
        softAssertions.assertThat(bppUpdateResponse.getUsers().stream()
                .anyMatch(u -> u.getRole().equals("ADMIN") &&
                    u.getUserIdentity().equals(currentOwnerUserIdentity)))
            .isTrue();
    }

    @Test
    @TestRail(id = {26661})
    @Description("Project Participant: Update project details")
    public void updateProjectAttributesByNonOwner() {
        //Update Description
        String descriptionNew = new GenerateStringUtil().getRandomStringSpecLength(12);
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .description(descriptionNew).build())
            .build();
        ApwErrorMessage bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), firstUser, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(bppUpdateResponse.getMessage()).contains("User does not have rights to update the project attributes");

        //Update Name
        String nameNew = new GenerateStringUtil().getRandomStringSpecLength(12);
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(nameNew).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), firstUser, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(bppUpdateResponse.getMessage()).contains("User does not have rights to update the project attributes");

        //Update Status
        String statusNew = "PURCHASED";
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status(statusNew).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), firstUser, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(bppUpdateResponse.getMessage()).contains("User does not have rights to update the project attributes");

        //Update DueAt
        String dueAtNew = DateUtil.getDateDaysAfter(10, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .dueAt(dueAtNew).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), firstUser, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(bppUpdateResponse.getMessage()).contains("User does not have rights to update the project attributes");

        //Update Owner
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .owner(secondUserIdentity).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), firstUser, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(bppUpdateResponse.getMessage()).contains("User does not have rights to update the project attributes");
    }

    @Test
    @TestRail(id = {26821})
    @Description("NOT Project participant: Update Project details")
    public void updateProjectAttributesByNonParticipant() {
        //Update Description
        String descriptionNew = new GenerateStringUtil().getRandomStringSpecLength(12);
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .description(descriptionNew).build())
            .build();
        ApwErrorMessage bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), nonProjectUser, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(bppUpdateResponse.getMessage()).contains("User does not have rights to update the project attributes");

        //Update Name
        String nameNew = new GenerateStringUtil().getRandomStringSpecLength(12);
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(nameNew).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), nonProjectUser, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(bppUpdateResponse.getMessage()).contains("User does not have rights to update the project attributes");

        //Update Status
        String statusNew = "PURCHASED";
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status(statusNew).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), nonProjectUser, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(bppUpdateResponse.getMessage()).contains("User does not have rights to update the project attributes");

        //Update DueAt
        String dueAtNew = DateUtil.getDateDaysAfter(10, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .dueAt(dueAtNew).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), nonProjectUser, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(bppUpdateResponse.getMessage()).contains("User does not have rights to update the project attributes");

        //Update Owner
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .owner(secondUserIdentity).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), nonProjectUser, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(bppUpdateResponse.getMessage()).contains("User does not have rights to update the project attributes");
    }

    @Test
    @TestRail(id = {26630, 26631})
    @Description("Users permission validation: Add & Delete Parts & Assemblies (PI) to the project")
    public void addDeleteProjectItemsToProjectAsDifferentUsers() {
        //Add Project Item
        List<BidPackageProjectItem> bidPackageItemList = new ArrayList<>();
        ScenarioItem addScenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentOwnerUser);
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(addScenarioItem.getScenarioIdentity())
                .componentIdentity(addScenarioItem.getComponentIdentity())
                .iterationIdentity(addScenarioItem.getIterationIdentity())
                .build())
            .build());

        //Validate project user (Not Owner) CANNOT add project item to the project
        ApwErrorMessage bulkBidPackageProjectItemsErrorResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(),
            bidPackageItemList,
            ApwErrorMessage.class,
            firstUser,
            HttpStatus.SC_FORBIDDEN
        );
        softAssertions.assertThat(bulkBidPackageProjectItemsErrorResponse.getMessage()).contains("The User: UserIdentity  does not have permission to add more Parts & Assemblies into the project");

        //Validate non project user CANNOT add project item to the project
        bulkBidPackageProjectItemsErrorResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(),
            bidPackageItemList,
            ApwErrorMessage.class,
            nonProjectUser,
            HttpStatus.SC_FORBIDDEN
        );
        softAssertions.assertThat(bulkBidPackageProjectItemsErrorResponse.getMessage())
            .contains("The User: UserIdentity doesn't have permission to interact with the project");

        //Validate project owner CAN add project item to the project
        BidPackageProjectItemsBulkResponse bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentOwnerUser,
            HttpStatus.SC_OK
        );
        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getProjectItem().stream()
                .anyMatch(pi -> pi.getBidPackageItem().getScenarioIdentity().equals(addScenarioItem.getScenarioIdentity()) &&
                    pi.getBidPackageItem().getComponentIdentity().equals(addScenarioItem.getComponentIdentity()) &&
                    pi.getBidPackageItem().getIterationIdentity().equals(addScenarioItem.getIterationIdentity())))
            .isTrue();

        //Validate project user (Not Owner) CANNOT delete project item from the project
        List<BidPackageProjectItem> prjItemIdentiesList = new ArrayList<>();
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity(bulkBidPackageProjectItemsResponse.getProjectItem().get(0).getIdentity())
            .build());
        bulkBidPackageProjectItemsErrorResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(),
            prjItemIdentiesList,
            ApwErrorMessage.class,
            HttpStatus.SC_FORBIDDEN,
            firstUser
        );
        softAssertions.assertThat(bulkBidPackageProjectItemsErrorResponse.getMessage()).contains("The User: UserIdentity does not have permission to delete Parts & Assemblies from the project");

        //Validate non project user CANNOT delete project item from the project
        bulkBidPackageProjectItemsErrorResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(),
            prjItemIdentiesList,
            ApwErrorMessage.class,
            HttpStatus.SC_FORBIDDEN,
            nonProjectUser
        );
        softAssertions.assertThat(bulkBidPackageProjectItemsErrorResponse.getMessage())
            .contains("The User: UserIdentity doesn't have permission to interact with the project");

        //Validate project owner CAN delete project item from the project
        bulkBidPackageProjectItemsResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(),
            prjItemIdentiesList,
            BidPackageProjectItemsBulkResponse.class,
            HttpStatus.SC_OK,
            currentOwnerUser
        );
        BidPackageProjectItemsBulkResponse finalBulkBidPackageProjectItemsResponse = bulkBidPackageProjectItemsResponse;
        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getProjectItem().stream()
                .anyMatch(pi -> pi.getIdentity().equals(finalBulkBidPackageProjectItemsResponse.getProjectItem().get(0).getIdentity())))
            .isTrue();

        QmsApiTestUtils.deleteScenarioViaCidApp(addScenarioItem, currentOwnerUser);
    }

    @Test
    @TestRail(id = {26657, 26658})
    @Description("Users permission validation: Add & Delete Project Users to the project.")
    public void addDeleteProjectUsersToProjectAsDifferentUsers() {
        //Validate project user (Not Owner) CAN add another project user to the project
        UserCredentials thirdUser = UserUtil.getUser();
        String thirdUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(thirdUser.getEmail());
        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(Collections.singletonList(BidPackageProjectUserParameters.builder().userEmail(thirdUserIdentity).role("DEFAULT")
                .build())).build();

        BidPackageProjectUsersPostResponse bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(bidPackageProjectUserRequestBuilder,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, firstUser);
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(thirdUserIdentity))).isTrue();

        String thirdProjectUserIdentity = bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .filter(pu -> pu.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(thirdUser.getEmail())))
            .findFirst().map(BidPackageProjectUserResponse::getIdentity).orElse(null);

        //Validate non project user CANNOT add another project user to the project
        UserCredentials fourthUser = UserUtil.getUser();
        String fourthUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(fourthUser.getEmail());
        bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(Collections.singletonList(BidPackageProjectUserParameters.builder().userEmail(fourthUserIdentity).role("DEFAULT")
                .build())).build();

        BidPackageProjectUsersPostResponse bulkProjectUserErrorResponse = QmsBidPackageResources.createBidPackageProjectUser(bidPackageProjectUserRequestBuilder,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, nonProjectUser);
        softAssertions.assertThat(bulkProjectUserErrorResponse.getProjectUsers().getFailures().stream()
            .anyMatch(f -> f.getError().contains(String.format("Found 0 items with email %s", nonProjectUserIdentity)) &&
                f.getUserEmail().equals(nonProjectUserIdentity))).isTrue();

        //Validate project owner CAN add another project user to the project
        bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(Collections.singletonList(BidPackageProjectUserParameters.builder().userEmail(fourthUserIdentity).role("DEFAULT")
                .build())).build();

        bulkProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser(bidPackageProjectUserRequestBuilder,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentOwnerUser);
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(fourthUserIdentity))).isTrue();

        //Validate project user (Not Owner) CANNOT delete another project user from the project
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(thirdProjectUserIdentity).build());
        ApwErrorMessage deleteErrorResponse = QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN, firstUser);
        softAssertions.assertThat(deleteErrorResponse.getMessage()).contains("The User: UserIdentity does not have permission to delete Parts & Assemblies from the project");

        //Validate non project user CANNOT delete another project user from the project
        deleteErrorResponse = QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN, nonProjectUser);
        softAssertions.assertThat(deleteErrorResponse.getMessage()).contains("The User: UserIdentity does not have permission to delete Parts & Assemblies from the project");

        //Validate project owner CAN delete another project user from the project
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), currentOwnerUser);
    }

    @Test
    @TestRail(id = {26659})
    @Description("Users permission validation: Delete Project")
    public void deleteProjectAsDifferentUsers() {
        //Validate project user (Not Owner) CANNOT delete project
        ApwErrorMessage deleteErrorResponse = QmsBidPackageResources.deleteBidPackageProject(projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(),
            ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN, QmsProjectUserPermissionsTest.firstUser);
        softAssertions.assertThat(deleteErrorResponse.getMessage())
            .contains("The User: UserIdentity does not have permission to delete the project");

        //Validate non project user CANNOT delete project
        deleteErrorResponse = QmsBidPackageResources.deleteBidPackageProject(projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(),
            ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN, nonProjectUser);
        softAssertions.assertThat(deleteErrorResponse.getMessage())
            .contains("The User: UserIdentity doesn't have permission to interact with the project");

        //Validate project owner CAN delete project
        QmsBidPackageResources.deleteBidPackageProject(projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), null,
            HttpStatus.SC_NO_CONTENT, currentOwnerUser);
    }

    @Test
    @TestRail(id = {26660, 26674})
    @Description("Owner: Remove yourself from the project.\n" +
        "Project Participant: Remove yourself from the project")
    public void deleteProjectUserSelfAsOwnerAndParticipant() {
        //Validate project owner CANNOT delete self from the project
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(currentProjectUserIdentity).build());
        ApwErrorMessage deleteErrorResponse = QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(),
            ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN, currentOwnerUser);
        softAssertions.assertThat(deleteErrorResponse.getMessage()).contains("Admin can delete himself only after reassigning the Ownership");

        //Validate project user (not owner) CAN delete self from the project
        userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(firstProjectUserIdentity).build());
        QmsBidPackageResources.deleteBidPackageProjectUser(userIdentityList, projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), firstUser);
    }

    @Test
    @TestRail(id = {26677})
    @Description("Owner: Share the scenario (Deleting Yourself from the Project)")
    public void deleteScenarioProjectUserSelfAsOwner() {
        //Update owner
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .owner(thirdUserIdentity).build())
            .build();
        BidPackageProjectResponse bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), currentOwnerUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(bppUpdateResponse.getOwner()).isEqualTo(thirdUserIdentity);
        softAssertions.assertThat(bppUpdateResponse.getUsers().stream()
                .anyMatch(u -> u.getRole().equals("ADMIN") &&
                    u.getUserIdentity().equals(thirdUserIdentity)))
            .isTrue();

        //Validate project owner CANNOT delete self from the project
        ProjectUserRequest deleteProjectUserRequest = ProjectUserRequest.builder()
            .users(Collections.singletonList(ProjectUserParameters.builder()
                .email(thirdUser.getEmail())
                .build()))
            .build();
        ApwErrorMessage deleteErrorResponse = QmsComponentResources.deleteComponentScenarioUser(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(),
            deleteProjectUserRequest, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN, thirdUser);
        softAssertions.assertThat(deleteErrorResponse.getMessage()).contains("Admin can delete himself only after reassigning the Ownership");

        //Update owner
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .owner(currentOwnerUserIdentity).build())
            .build();
        bppUpdateResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            projectResponse.getBidPackageIdentity(), projectResponse.getIdentity(), thirdUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(bppUpdateResponse.getOwner()).isEqualTo(currentOwnerUserIdentity);
        softAssertions.assertThat(bppUpdateResponse.getUsers().stream()
                .anyMatch(u -> u.getRole().equals("ADMIN") &&
                    u.getUserIdentity().equals(currentOwnerUserIdentity)))
            .isTrue();

        //Validate project user (previous owner) CAN delete self from the project after he assigns Ownership to another user.
        deleteProjectUserRequest = ProjectUserRequest.builder()
            .users(Collections.singletonList(ProjectUserParameters.builder()
                .email(thirdUser.getEmail())
                .build()))
            .build();
        QmsComponentResources.deleteComponentScenarioUser(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(),
            deleteProjectUserRequest, thirdUser);
    }

    @Test
    @TestRail(id = {26820})
    @Description("Project Participant: Share the scenario (Deleting Yourself from the Project)")
    public void deleteScenarioProjectUserSelfAsParticipant() {
        //Validate project user (not owner) CAN delete self from the project
        ProjectUserRequest deleteProjectUserRequest = ProjectUserRequest.builder()
            .users(Collections.singletonList(ProjectUserParameters.builder()
                .email(firstUser.getEmail())
                .build()))
            .build();
        QmsComponentResources.deleteComponentScenarioUser(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), deleteProjectUserRequest, firstUser);
    }

    @Test
    @TestRail(id = {26675})
    @Description("Users permission validation: Share the scenario (Adding PU to the Project in the background)")
    public void shareScenarioAsDifferentUsers() {
        //Validate the Project User (NOT Owner) of the project CAN Share the scenario with another user
        UserCredentials anotherFirstUser = UserUtil.getUser();
        ProjectUserParameters projectUserParameters = ProjectUserParameters.builder()
            .email(anotherFirstUser.getEmail()).build();
        ResponseWrapper<ScenarioProjectUserResponse> componentScenariosResponse =
            QmsComponentResources.addComponentScenarioUser(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                projectUserParameters, firstUser);
        softAssertions.assertThat(componentScenariosResponse.getResponseEntity().stream()
            .anyMatch(u -> u.getIdentity()
                .equals(new AuthUserContextUtil().getAuthUserIdentity(anotherFirstUser.getEmail())))).isTrue();

        //Validate the Project User (Owner) of the project CAN Share the scenario with another user
        UserCredentials anotherSecondUser = UserUtil.getUser();
        projectUserParameters = ProjectUserParameters.builder()
            .email(anotherSecondUser.getEmail()).build();
        componentScenariosResponse = QmsComponentResources.addComponentScenarioUser(scenarioItem.getComponentIdentity(),
            scenarioItem.getScenarioIdentity(),
            projectUserParameters, currentOwnerUser);
        softAssertions.assertThat(componentScenariosResponse.getResponseEntity().stream()
            .anyMatch(u -> u.getIdentity()
                .equals(new AuthUserContextUtil().getAuthUserIdentity(anotherSecondUser.getEmail())))).isTrue();

        //Validate the Non Project User of the project CANNOT Share the scenario with another user
        UserCredentials anotherThirdUser = UserUtil.getUser();
        projectUserParameters = ProjectUserParameters.builder()
            .email(anotherThirdUser.getEmail()).build();
        ApwErrorMessage componentScenariosErrorResponse = QmsComponentResources.addComponentScenarioUser(scenarioItem.getComponentIdentity(),
            scenarioItem.getScenarioIdentity(),
            projectUserParameters,
            ApwErrorMessage.class,
            HttpStatus.SC_NOT_FOUND,
            nonProjectUser);
        softAssertions.assertThat(componentScenariosErrorResponse.getMessage()).contains(String.format("Resource 'Project' with identity '%s' was not found", projectResponse.getIdentity()));
    }

    @Test
    @TestRail(id = {26800})
    @Description("Users permission validation: Get project by Identity /project/{projectIdentity}")
    public void getProjectByIdentityAsDifferentUsers() {
        //Validate that the Project User (Owner) of the project is able to Get the project by identity
        BidPackageProjectResponse bppResponse = QmsProjectResources.getProject(
            projectResponse.getIdentity(),
            BidPackageProjectResponse.class,
            HttpStatus.SC_OK,
            currentOwnerUser);
        softAssertions.assertThat(bppResponse.getUsers().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getItems().stream()
                    .anyMatch(p -> p.getIdentity().equals(projectResponse.getIdentity())))
                .isTrue();
        }

        //Validate that the Project User (NOT Owner) of the project is able to Get the project by identity
        bppResponse = QmsProjectResources.getProject(
            projectResponse.getIdentity(),
            BidPackageProjectResponse.class,
            HttpStatus.SC_OK,
            firstUser);
        softAssertions.assertThat(bppResponse.getUsers().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getItems().stream()
                    .anyMatch(p -> p.getIdentity().equals(projectResponse.getIdentity())))
                .isTrue();
        }

        //Validate that the NOT Project User of the project is NOT able to Get the project by identity
        ApwErrorMessage bppErrorResponse = QmsProjectResources.getProject(
            projectResponse.getIdentity(),
            ApwErrorMessage.class,
            HttpStatus.SC_FORBIDDEN,
            nonProjectUser);
        softAssertions.assertThat(bppErrorResponse.getMessage()).contains("The User: UserIdentity doesn't have permission to interact with the project");
    }

    @Test
    @TestRail(id = {26813})
    @Description("Users permission validation: Get project by Identity bid-package/{bid-packageIdentity}/project/{projectIdentity}")
    public void getBidPackageProjectByIdentityAsDifferentUsers() {
        //Validate that the Project User (Owner) of the project is able to Get the bidpackage project by identity
        BidPackageProjectResponse bppResponse = QmsBidPackageResources.getBidPackageProject(
            projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(),
            BidPackageProjectResponse.class,
            HttpStatus.SC_OK,
            currentOwnerUser);
        softAssertions.assertThat(bppResponse.getUsers().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getItems().stream()
                    .anyMatch(p -> p.getIdentity().equals(projectResponse.getIdentity())))
                .isTrue();
        }

        //Validate that the Project User (NOT Owner) of the project is able to Get the bidpackage project by identity
        bppResponse = QmsBidPackageResources.getBidPackageProject(
            projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(),
            BidPackageProjectResponse.class,
            HttpStatus.SC_OK,
            firstUser);
        softAssertions.assertThat(bppResponse.getUsers().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getItems().stream()
                    .anyMatch(p -> p.getIdentity().equals(projectResponse.getIdentity())))
                .isTrue();
        }

        //Validate that the NOT Project User of the project is NOT able to Get the bidpackage project by identity
        ApwErrorMessage bppErrorResponse = QmsBidPackageResources.getBidPackageProject(
            projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(),
            ApwErrorMessage.class,
            HttpStatus.SC_FORBIDDEN,
            nonProjectUser);
        softAssertions.assertThat(bppErrorResponse.getMessage()).contains("The User: UserIdentity doesn't have permission to interact with the project");
    }

    @Test
    @TestRail(id = {26808})
    @Description("Users permission validation: Read the list of Parts & Assemblies (PI) of the project")
    public void getAllProjectItemsAsDifferentUsers() {
        //Validate that the Project User (Owner) of the project is able to Get Project Items of the project.
        BidPackageProjectItemsResponse bpPItemsResponse = QmsBidPackageResources.getBidPackageProjectItems(
            projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(),
            currentOwnerUser,
            BidPackageProjectItemsResponse.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(bpPItemsResponse.getItems().size()).isGreaterThan(0);

        //Validate that the Project User (NOT Owner) of the project is able to Get Project Items of the project.
        bpPItemsResponse = QmsBidPackageResources.getBidPackageProjectItems(
            projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(),
            firstUser,
            BidPackageProjectItemsResponse.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(bpPItemsResponse.getItems().size()).isGreaterThan(0);

        //Validate that the NOT Project User of the project is NOT able to Get Project Items of the project.
        ApwErrorMessage bpPItemsErrorResponse = QmsBidPackageResources.getBidPackageProjectItems(
            projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(),
            nonProjectUser,
            ApwErrorMessage.class,
            HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(bpPItemsErrorResponse.getMessage()).contains("The User: UserIdentity doesn't have permission to interact with the project");
    }

    @Test
    @TestRail(id = {26814})
    @Description("Users permission validation: Read Project Users of the project")
    public void getAllProjectUsersAsDifferentUsers() {
        //Validate that the Project User (Owner) of the project is able to Get all Project users of the project.
        BidPackageProjectUsersResponse getBidPackageProjectUserResponse = QmsBidPackageResources.getBidPackageProjectUsers(projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(), currentOwnerUser, BidPackageProjectUsersResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectUserResponse.getItems().size()).isGreaterThan(0);

        //Validate that the Project User (NOT Owner) of the project is able to Get all Project users of the project.
        getBidPackageProjectUserResponse = QmsBidPackageResources.getBidPackageProjectUsers(projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(), firstUser, BidPackageProjectUsersResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectUserResponse.getItems().size()).isGreaterThan(0);

        //Validate that the NOT Project User of the project is NOT able to Get all Project users of the project.
        ApwErrorMessage getBidPackageProjectUserErrorResponse = QmsBidPackageResources.getBidPackageProjectUsers(projectResponse.getBidPackageIdentity(),
            projectResponse.getIdentity(), nonProjectUser, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(getBidPackageProjectUserErrorResponse.getMessage()).contains("The User: UserIdentity doesn't have permission to interact with the project");
    }
}
