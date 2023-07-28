package com.apriori;

import com.apriori.authorization.response.ApwErrorMessage;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsProjectResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.QmsApiTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

public class BidPackageProjectsTest extends TestUtil {
    private static final UserCredentials currentUser = UserUtil.getUser();
    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static String bidPackageName;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = new GenerateStringUtil().getRandomString();
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
    }

    @Test
    @TestRail(id = {13742, 13752, 22955, 14738, 25962})
    @Description("Create and Delete Bid Package Project")
    public void createAndDeleteProject() {
        String ownerIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageProjectResponse bppResponse = QmsBidPackageResources.createBidPackageProject(new HashMap<>(),
            bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);
        softAssertions.assertThat(bppResponse.getItems().size()).isZero();
        softAssertions.assertThat(bppResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
        softAssertions.assertThat(bppResponse.getOwner()).isEqualTo(ownerIdentity);
        softAssertions.assertThat(bppResponse.getOwnerUserIdentity()).isNotNull();
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getUsers().stream()
                    .allMatch(u -> u.getUser().getAvatarColor() != null))
                .isTrue();
            softAssertions.assertThat(bppResponse.getUsers().stream()
                    .anyMatch(u -> u.getRole().equals("ADMIN") &&
                        u.getUserIdentity().equals(ownerIdentity)))
                .isTrue();

            bppResponse.getUsers().stream()
                .filter(u -> u.getUserIdentity().equals(ownerIdentity))
                .findFirst()
                .ifPresent(bidPackageProjectUserResponse ->
                    softAssertions.assertThat(bppResponse.getOwnerFullName())
                        .isEqualTo(bidPackageProjectUserResponse.getUser().getUserProfile().getGivenName().concat(" ")
                            .concat(bidPackageProjectUserResponse.getUser().getUserProfile().getFamilyName())));
        }
        QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bppResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @Test
    @TestRail(id = {13898, 13899, 14671, 23010})
    @Description("Get list of all Bid Package Projects and verify pagination ")
    public void getBidPackageProjects() {
        BidPackageProjectsResponse projectsResponse = QmsBidPackageResources.getBidPackageProjects(bidPackageResponse.getIdentity(), currentUser);
        softAssertions.assertThat(projectsResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(projectsResponse.getIsFirstPage()).isTrue();
        if (softAssertions.wasSuccess()) {
            for (int i = 0; i < projectsResponse.getItems().size(); i++) {
                for (int j = 0; j < projectsResponse.getItems().get(i).getUsers().size(); j++) {
                    softAssertions.assertThat(projectsResponse.getItems().get(i).getUsers().get(j).getUser()
                        .getAvatarColor()).isNotNull();
                }
            }
        }
    }

    @Test
    @TestRail(id = {13750, 14684, 22957})
    @Description("Find Bid Package Project By Identity")
    public void getBidPackageProject() {
        BidPackageProjectResponse getBidPackageProjectResponse = QmsBidPackageResources.getBidPackageProject(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(getBidPackageProjectResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
        softAssertions.assertThat(getBidPackageProjectResponse.getOwnerUserIdentity()).isNotNull();
        if (softAssertions.wasSuccess()) {
            for (int j = 0; j < getBidPackageProjectResponse.getUsers().size(); j++) {
                softAssertions.assertThat(getBidPackageProjectResponse.getUsers().get(j).getUser().getAvatarColor())
                    .isNotNull();
            }
        }
    }

    @Test
    @TestRail(id = {13751, 22958, 24277, 25989})
    @Description("Update Bid Package Project By Identity")
    public void updateBidPackageProject() {
        String projectNameNew = new GenerateStringUtil().getRandomString();
        String projectDescriptionNew = new GenerateStringUtil().getRandomString();
        String displayNameNew = new GenerateStringUtil().getRandomString();
        String ownerEmail = UserUtil.getUser().getEmail();
        String ownerUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(ownerEmail);
        String statusNew = "COMPLETED";
        String dueAtNew = DateUtil.getDateDaysAfter(15, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(projectNameNew)
                .description(projectDescriptionNew)
                .displayName(displayNameNew)
                .status(statusNew)
                .dueAt(dueAtNew)
                .owner(ownerUserIdentity)
                .build())
            .build();
        BidPackageProjectResponse getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
        softAssertions.assertThat(getBidPackageProjectResponse.getName()).isEqualTo(projectNameNew);
        softAssertions.assertThat(getBidPackageProjectResponse.getDescription()).isEqualTo(projectDescriptionNew);
        softAssertions.assertThat(getBidPackageProjectResponse.getDisplayName()).isEqualTo(displayNameNew);
        softAssertions.assertThat(getBidPackageProjectResponse.getStatus()).isEqualTo(statusNew);
        softAssertions.assertThat(getBidPackageProjectResponse.getDueAt())
            .isEqualTo(LocalDateTime.parse(dueAtNew, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ));
        softAssertions.assertThat(getBidPackageProjectResponse.getOwnerUserIdentity()).isEqualTo(ownerUserIdentity);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(getBidPackageProjectResponse.getUsers().stream()
                .allMatch(u -> u.getUser().getAvatarColor() != null)).isTrue();
        }
    }

    @Test
    @TestRail(id = {13744})
    @Description("Create Bid Package project name is greater than 64 characters")
    public void createProjectNameGreaterThan64() {
        HashMap<String, String> prjAttributesMap = new HashMap<>();
        prjAttributesMap.put("projectName", RandomStringUtils.randomAlphabetic(70));
        ApwErrorMessage bppErrorResponse64 = QmsBidPackageResources.createBidPackageProject(prjAttributesMap, bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(bppErrorResponse64.getMessage())
            .contains("'name' should not be more than 64 characters");
    }

    @Test
    @TestRail(id = {13745})
    @Description("Create Bid Package project with empty name")
    public void createProjectWithEmptyName() {
        HashMap<String, String> prjAttributesMap = new HashMap<>();
        prjAttributesMap.put("projectName", "");
        ApwErrorMessage bppErrorResponse64 = QmsBidPackageResources.createBidPackageProject(prjAttributesMap, bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(bppErrorResponse64.getMessage()).contains("'name' should not be null");
    }

    @Test
    @TestRail(id = {13747})
    @Description("Create Bid Package with project description greater than 254 characters")
    public void createProjectDescGreaterThan254() {
        HashMap<String, String> prjAttributesMap = new HashMap<>();
        prjAttributesMap.put("projectDescription", RandomStringUtils.randomAlphabetic(258));
        ApwErrorMessage bppErrorResponse64 = QmsBidPackageResources.createBidPackageProject(prjAttributesMap, bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(bppErrorResponse64.getMessage())
            .contains("'description' should not be more than 254 characters");
    }

    @Test
    @TestRail(id = {13743, 13749})
    @Description("Create Bid Package project name equal to 64 characters, delete the bid package and create project with deleted Bid Package identity")
    public void createProjectNameEqualTo64() {
        HashMap<String, String> prjAttributesMap = new HashMap<>();
        String projectName = RandomStringUtils.randomAlphabetic(64);
        prjAttributesMap.put("projectName", projectName);
        BidPackageProjectResponse bppResponse = QmsBidPackageResources.createBidPackageProject(prjAttributesMap, bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        softAssertions.assertThat(bppResponse.getName()).isEqualTo(projectName);

        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        ApwErrorMessage bppErrorResponse = QmsBidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);

        softAssertions.assertThat(bppErrorResponse.getMessage())
            .contains(String.format("Can't find bidPackage with identity '%s' for customerIdentity '%s'", bidPackageResponse.getIdentity(), bidPackageResponse.getCustomerIdentity()));
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
    }

    @Test
    @TestRail(id = {13746})
    @Description("Create Bid Package description is equal to 254 characters")
    public void createProjectDescEqualTo254() {
        String projectDescription = RandomStringUtils.randomAlphabetic(254);
        HashMap<String, String> prjAttributesMap = new HashMap<>();
        prjAttributesMap.put("projectDescription", projectDescription);
        BidPackageProjectRequest projectRequestBuilder = QmsBidPackageResources.getBidPackageProjectRequestBuilder(prjAttributesMap, currentUser);
        BidPackageProjectResponse bppResponse = QmsBidPackageResources.createBidPackageProject(projectRequestBuilder,
            bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);
        softAssertions.assertThat(bppResponse.getDescription()).isEqualTo(projectDescription);
        QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bppResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @Test
    @TestRail(id = {13754})
    @Description("Verify bid package project is deleted")
    public void verifyBidPackageProjectIsDeleted() {
        QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.getBidPackageProject(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);
        softAssertions.assertThat(qmsErrorMessage.getMessage())
            .contains(String.format("Can't find Project for bid package with identity '%s' and identity '%s'", bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity()));
    }

    @Test
    @TestRail(id = {13894})
    @Description("Find a project with invalid identity")
    public void getProjectWithInvalidIdentity() {
        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.getBidPackageProject(bidPackageResponse.getIdentity(), "INVALID", ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(qmsErrorMessage.getMessage()).contains("'projectIdentity' is not a valid identity");
    }

    @Test
    @TestRail(id = {13753})
    @Description("delete a project with invalid identity")
    public void deleteProjectWithInvalidIdentity() {
        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), "INVALID", ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(qmsErrorMessage.getMessage()).contains("'projectIdentity' is not a valid identity");
    }

    @Test
    @TestRail(id = {14124, 14670})
    @Description("Verify that project is deleted after deleting bid-package")
    public void verifyProjectIsDeletedAfterBidPackageDeleted() {
        String bidPackName = new GenerateStringUtil().getRandomNumbers();
        String projectName = new GenerateStringUtil().getRandomNumbers();
        HashMap<String, String> prjAttributesMap = new HashMap<>();
        prjAttributesMap.put("projectName", projectName);
        BidPackageResponse bidPackResponse = QmsBidPackageResources.createBidPackage(bidPackName, currentUser);
        softAssertions.assertThat(bidPackResponse.getName()).isEqualTo(bidPackName);
        BidPackageProjectResponse bidPackProjectResponse = QmsBidPackageResources.createBidPackageProject(prjAttributesMap, bidPackResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        softAssertions.assertThat(bidPackProjectResponse.getName()).isEqualTo(projectName);

        QmsBidPackageResources.deleteBidPackage(bidPackResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.getBidPackageProject(bidPackResponse.getIdentity(),
            bidPackProjectResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);
        softAssertions.assertThat(qmsErrorMessage.getMessage())
            .contains(String.format("Can't find bidPackage with identity '%s' for customerIdentity '%s'", bidPackResponse.getIdentity(), bidPackResponse.getCustomerIdentity()));
        ApwErrorMessage bppErrorResponse = QmsProjectResources.getProject(bidPackProjectResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);
        softAssertions.assertThat(bppErrorResponse.getMessage())
            .contains(String.format("Resource 'Project' with identity '%s' was not found", bidPackProjectResponse.getIdentity()));
    }

    @Test
    @TestRail(id = {24265})
    @Description("Verify project name cannot be updated to null/empty")
    public void updateEmptyProjectName() {
        //Project Name is Empty
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name("").build())
            .build();
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage()).contains("'name' should not be null");

        //Project Name is null
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(null).build())
            .build();
        getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage()).contains("'name' should not be null");
    }

    @Test
    @TestRail(id = {24266})
    @Description("Verify display name cannot be updated to empty")
    public void updateEmptyProjectDisplayName() {
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .displayName("").build())
            .build();
        ApwErrorMessage errorProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(errorProjectResponse.getMessage())
            .contains("2 validation failures were found:" +
                "* 'displayName' should not be empty." +
                "* 'displayName' should not be blank");
    }

    @Test
    @TestRail(id = {24267})
    @Description("Verify display name can be updated with maximum of 64 characters only")
    public void updateProjectDisplayNameEqualTo64() {
        String projectDisplayName = RandomStringUtils.randomAlphabetic(64);
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .displayName(projectDisplayName).build())
            .build();
        BidPackageProjectResponse getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getDisplayName()).isEqualTo(projectDisplayName);
        softAssertions.assertThat(getBidPackageProjectResponse.getName()).isEqualTo(projectDisplayName);
    }

    @Test
    @TestRail(id = {24268})
    @Description("Verify display name cannot be updated to more than 64 characters")
    public void updateProjectDisplayNameGreaterThan64() {
        String projectDisplayName = RandomStringUtils.randomAlphabetic(70);
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .displayName(projectDisplayName).build())
            .build();
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("'displayName' should not be more than 64 characters");
    }

    @Test
    @TestRail(id = {24269})
    @Description("Verify project status can not be updated to null")
    public void updateEmptyProjectStatus() {
        //Project Status is Empty
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status("").build())
            .build();
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("'status' should not be null");

        //Project Status is null
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status(null).build())
            .build();
        getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("'status' should not be null");
    }

    @Test
    @TestRail(id = {24270})
    @Description("Verify project status can be updated to only following status IN_NEGOTIATION ,COMPLETED  & PURCHASED")
    public void updateProjectStatuses() {
        //Project Status is "COMPLETED"
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status("COMPLETED").build())
            .build();
        BidPackageProjectResponse getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getStatus()).isEqualTo("COMPLETED");

        //Project Status is "IN_NEGOTIATION"
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status("IN_NEGOTIATION").build())
            .build();
        getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getStatus()).isEqualTo("IN_NEGOTIATION");

        //Project Status is "PURCHASED"
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status("PURCHASED").build())
            .build();
        getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getStatus()).isEqualTo("PURCHASED");
    }

    @Test
    @TestRail(id = {24275})
    @Description("Verify other than admin users cannot update the project attributes")
    public void updateProjectAttributesByNonAdminUser() {
        softAssertions.assertThat(bidPackageProjectResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
        softAssertions.assertThat(bidPackageProjectResponse.getItems().size()).isZero();
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bidPackageProjectResponse.getUsers().stream()
                    .anyMatch(u -> u.getRole().equals("ADMIN") &&
                        u.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))))
                .isTrue();
        }

        //Update project with non-admin project user
        UserCredentials nonAdminProjectUser = UserUtil.getUser();
        String projectDescriptionNew = new GenerateStringUtil().getRandomString();
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .description(projectDescriptionNew)
                .build())
            .build();
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), nonAdminProjectUser, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("User does not have rights to update the project attributes");
    }

    @Test
    @TestRail(id = {24276})
    @Description("Verify project owner(identity) cannot be updated to null, empty and invalid")
    public void updateEmptyProjectOwner() {
        //Project owner is Empty
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .owner("").build())
            .build();
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("3 validation failures were found:" +
                "* 'owner' should not be empty." +
                "* 'owner' should not be blank." +
                "* 'owner' is not a valid identity.");

        //Project owner is null
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .owner(null).build())
            .build();
        getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("'owner' should not be null");

        //Project owner is invalid
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .owner("INVALID").build())
            .build();
        getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("'owner' is not a valid identity");
    }

    @Test
    @TestRail(id = {24278})
    @Description("Verify project name can be updated with maximum of 64 characters only")
    public void updateProjectNameEqualTo64() {
        String projectName64 = RandomStringUtils.randomAlphabetic(64);
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(projectName64).build())
            .build();
        BidPackageProjectResponse getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getName()).isEqualTo(projectName64);
    }

    @Test
    @TestRail(id = {24279})
    @Description("Verify project name cannot be updated to more than 64 characters")
    public void updateProjectNameGreaterThan64() {
        String projectName = RandomStringUtils.randomAlphabetic(70);
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(projectName).build())
            .build();
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("'name' should not be more than 64 characters");
    }

    @Test
    @TestRail(id = {24280})
    @Description("Verify dueAt can be updated to valid date or datetime format( yyyy-MM-dd & yyyy-MM-dd'T'HH:mm:ss.SSS'Z' )")
    public void updateBidPackageProjectDueAtValidFormats() {
        //yyyy-MM-dd
        String dueAtNew = DateUtil.getDateDaysAfter(15, DateFormattingUtils.dtf_yyyyMMdd);
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .dueAt(dueAtNew)
                .build())
            .build();
        BidPackageProjectResponse getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
        softAssertions.assertThat(getBidPackageProjectResponse.getDueAt())
            .isEqualTo(LocalDate.parse(dueAtNew).atTime(LocalTime.parse("23:59:59.999")));

        //yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        dueAtNew = DateUtil.getDateDaysAfter(15, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .dueAt(dueAtNew)
                .build())
            .build();
        getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
        softAssertions.assertThat(getBidPackageProjectResponse.getDueAt())
            .isEqualTo(LocalDateTime.parse(dueAtNew, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ));
    }

    @Test
    @TestRail(id = {24281})
    @Issue("COL-1834")
    @Description("Verify response should not contain the dueAt attribute, when dueAt attribute is having null or empty values in request")
    public void updateEmptyProjectDueAt() {
        //Project DueAt is Empty
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .dueAt("").build())
            .build();
        BidPackageProjectResponse getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getDueAt()).isNull();

        //Project DueAt is null
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .dueAt(null).build())
            .build();
        getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getDueAt()).isNull();
    }

    @Test
    @TestRail(id = {24347})
    @Issue("COL-1834")
    @Description("Verify response should not contain the description attribute, when description attribute is having null or empty values in request")
    public void updateEmptyProjectDescription() {
        //Project Description Name is empty
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .description("").build())
            .build();
        BidPackageProjectResponse getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getDescription()).isNull();

        //Project Description Name is null
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .description(null).build())
            .build();
        getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getDescription()).isNull();
    }

    @Test
    @TestRail(id = {24349})
    @Description("Verify project description cannot be updated to more than 254 characters")
    public void updateProjectDescriptionGreaterThan64() {
        String projectDes = RandomStringUtils.randomAlphabetic(255);
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .description(projectDes).build())
            .build();
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("'description' should not be more than 254 characters");
    }

    @Test
    @TestRail(id = {24350})
    @Description("Verify project description can be updated with maximum of 254 characters only")
    public void updateProjectDescriptionEqualTo254() {
        String projectDescription254 = RandomStringUtils.randomAlphabetic(254);
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .description(projectDescription254).build())
            .build();
        BidPackageProjectResponse getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getDescription()).isEqualTo(projectDescription254);
    }

    @Test
    @TestRail(id = {24354})
    @Description("Verify project display name cannot be updated to null")
    public void updateNullProjectDisplayName() {
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .displayName(null).build())
            .build();
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("'displayName' should not be null");
    }

    @Test
    @TestRail(id = {24351, 25959})
    @Description("Verify updated owner/admin users can update the project attributes." +
        "Verify updating Project Owner's identity will change ownerFullName accordingly")
    public void updateProjectWithNewOwner() {
        //Update the project to new owner
        UserCredentials newOwner = UserUtil.getUser();
        String newOwnerUserContext = new AuthUserContextUtil().getAuthUserIdentity(newOwner.getEmail());
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .owner(newOwnerUserContext).build())
            .build();

        BidPackageProjectResponse bppResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(bppResponse.getOwner()).isEqualTo(newOwnerUserContext);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getUsers().stream()
                    .anyMatch(u -> u.getRole().equals("ADMIN") &&
                        u.getUserIdentity().equals(newOwnerUserContext)))
                .isTrue();

            softAssertions.assertThat(bppResponse.getOwner()).isEqualTo(newOwnerUserContext);
            BidPackageProjectResponse finalBppResponse = bppResponse;
            bppResponse.getUsers().stream()
                .filter(u -> u.getUserIdentity().equals(newOwnerUserContext))
                .findFirst()
                .ifPresent(bidPackageProjectUserResponse ->
                    softAssertions.assertThat(finalBppResponse.getOwnerFullName())
                        .isEqualTo(bidPackageProjectUserResponse.getUser().getUserProfile().getGivenName().concat(" ")
                            .concat(bidPackageProjectUserResponse.getUser().getUserProfile().getFamilyName())));
        }

        //Update the project description with new owner
        String projectDescriptionNew = new GenerateStringUtil().getRandomString();
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .description(projectDescriptionNew)
                .build())
            .build();
        bppResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), newOwner, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(bppResponse.getDescription()).isEqualTo(projectDescriptionNew);

        //Update the project description with old owner
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .description(projectDescriptionNew)
                .build())
            .build();
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_FORBIDDEN);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("User does not have rights to update the project attributes");
    }

    @Test
    @TestRail(id = {24313, 24412})
    @Description("Verify dueAt can not be updated to invalid date or invalid datetime format")
    public void updateInvalidProjectDueAt() {
        //Invalid format #1
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .dueAt("13-03-2023").build())
            .build();
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("The value of dueAt is not meeting the expected criteria defined for dueAt");

        //Invalid format #2
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .dueAt("2023-13-15T23:59:59.999Z").build())
            .build();
        getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("The value of dueAt is not meeting the expected criteria defined for dueAt");

        //Invalid format #3 [Back Date]
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .dueAt("2023-02-02").build())
            .build();
        getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("Given dueAt is before than Current Time");
    }

    @Test
    @TestRail(id = {25922})
    @Description("Verify deletion of bid package project created automatically while creating QMS scenario-discussions is deleting all associated items like projectItems, discussions and bidPackageItems")
    public void deleteBidPackageProjectAndVerifyAllAssociatedEntitiesDeleted() {
        ScenarioItem scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        ScenarioDiscussionResponse scenarioDiscussionResponseOne = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        ScenarioDiscussionResponse scenarioDiscussionResponseTwo = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        BidPackageProjectResponse getProjectResponse = QmsProjectResources.getProject(scenarioDiscussionResponseOne.getProjectIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_OK, currentUser);

        //Delete Project
        QmsBidPackageResources.deleteBidPackageProject(getProjectResponse.getBidPackageIdentity(), getProjectResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        //Verify Project Deletion
        ApwErrorMessage getProjectErrorResponse = QmsProjectResources.getProject(getProjectResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);
        softAssertions.assertThat(getProjectErrorResponse.getMessage())
            .contains(String.format("Resource 'Project' with identity '%s' was not found", getProjectResponse.getIdentity()));

        //Verify Bid Package-items deletion
        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.getBidPackageItem(getProjectResponse.getBidPackageIdentity(),
            getProjectResponse.getItems().get(0).getIdentity(),
            currentUser,
            ApwErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(qmsErrorMessage.getMessage())
            .contains(String.format("Can't find bidPackageItem for bid package with identity '%s' and identity '%s'",
                getProjectResponse.getBidPackageIdentity(), getProjectResponse.getItems().get(0).getIdentity()));

        //Verify Project-items deletion
        ApwErrorMessage bpPItemsResponse = QmsBidPackageResources.getBidPackageProjectItems(
            getProjectResponse.getBidPackageIdentity(),
            getProjectResponse.getIdentity(),
            currentUser,
            ApwErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(bpPItemsResponse.getMessage())
            .contains(String.format("Can't find Project for bid package with identity '%s' and identity '%s'",
                getProjectResponse.getBidPackageIdentity(), getProjectResponse.getIdentity()));

        //Verify Scenario Discussions deletion
        String[] discussionIdsArr = new String[] {scenarioDiscussionResponseOne.getIdentity(), scenarioDiscussionResponseTwo.getIdentity()};
        for (String discussionId : discussionIdsArr) {
            ApwErrorMessage discussionErrorResponse = QmsScenarioDiscussionResources.getScenarioDiscussion(
                discussionId,
                ApwErrorMessage.class,
                HttpStatus.SC_NOT_FOUND,
                currentUser);

            softAssertions.assertThat(discussionErrorResponse.getMessage())
                .contains(String.format("Discussion with identity %s was not found", discussionId));
        }
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);
    }

    @Test
    @TestRail(id = {24295})
    @Issue("COL-1836")
    @Description("Verify project status can not be updated to any other status other than following status IN_NEGOTIATION ,COMPLETED & PURCHASED")
    public void updateProjectStatusToActive() {
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status("ACTIVE").build())
            .build();
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("Status  can be changed to only \"IN_NEGOTIATION\",\"COMPLETED\" and PURCHASED\"");
    }

    @Test
    @TestRail(id = {24427})
    @Description("For Bidpackage/project? endpoint  || Verify for project status can be updated to OPEN or IN_PROGRESS")
    public void updateProjectStatusToOpenAndInProgress() {
        //OPEN
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status("OPEN").build())
            .build();
        BidPackageProjectResponse getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getStatus()).isEqualTo("OPEN");

        //IN_PROGRESS
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status("IN_PROGRESS").build())
            .build();
        getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getStatus()).isEqualTo("IN_PROGRESS");
    }

    @Test
    @TestRail(id = {24481})
    @Description("Verify new project can be created with OPEN or IN_PROGRESS Status")
    public void createProjectWithStatusOpenAndInProgress() {
        //OPEN
        HashMap<String, String> prjAttributesMap = new HashMap<>();
        prjAttributesMap.put("projectStatus", "OPEN");
        BidPackageProjectResponse bppResponse = QmsBidPackageResources.createBidPackageProject(prjAttributesMap, bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        softAssertions.assertThat(bppResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getStatus()).isEqualTo("OPEN");
        }
        QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bppResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        //IN_PROGRESS
        prjAttributesMap = new HashMap<>();
        prjAttributesMap.put("projectStatus", "IN_PROGRESS");
        bppResponse = QmsBidPackageResources.createBidPackageProject(prjAttributesMap, bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        softAssertions.assertThat(bppResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getStatus()).isEqualTo("IN_PROGRESS");
        }
        QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bppResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @After
    public void testCleanup() {
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }
}
