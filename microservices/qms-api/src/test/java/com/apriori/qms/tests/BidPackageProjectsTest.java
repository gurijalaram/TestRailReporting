package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsProjectResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectItemsResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.utils.ApwErrorMessage;
import com.apriori.utils.DateFormattingUtils;
import com.apriori.utils.DateUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Link;
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
    @TestRail(testCaseId = {"13742", "13752", "22955", "14738"})
    @Description("Create and Delete Bid Package Project")
    public void createAndDeleteProject() {
        BidPackageProjectResponse bppResponse = QmsBidPackageResources.createBidPackageProject(new HashMap<>(),
            bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);
        softAssertions.assertThat(bppResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getUsers().stream()
                .allMatch(u -> u.getUser().getAvatarColor() != null)).isTrue();
        }
        softAssertions.assertThat(bppResponse.getItems().size()).isZero();
        QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bppResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13898", "13899", "14671", "23010"})
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
    @TestRail(testCaseId = {"13750", "14684", "22957"})
    @Description("Find Bid Package Project By Identity")
    public void getBidPackageProject() {
        BidPackageProjectResponse getBidPackageProjectResponse = QmsBidPackageResources.getBidPackageProject(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(getBidPackageProjectResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
        if (softAssertions.wasSuccess()) {
            for (int j = 0; j < getBidPackageProjectResponse.getUsers().size(); j++) {
                softAssertions.assertThat(getBidPackageProjectResponse.getUsers().get(j).getUser().getAvatarColor())
                    .isNotNull();
            }
        }
    }

    @Test
    @TestRail(testCaseId = {"13751", "22958", "24277"})
    @Description("Update Bid Package Project By Identity")
    public void updateBidPackageProject() {
        String projectNameNew = new GenerateStringUtil().getRandomString();
        String projectDescriptionNew = new GenerateStringUtil().getRandomString();
        String displayNameNew = new GenerateStringUtil().getRandomString();
        String statusNew = "COMPLETED";
        String dueAtNew = DateUtil.getDateDaysAfter(15, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(projectNameNew)
                .description(projectDescriptionNew)
                .displayName(displayNameNew)
                .status(statusNew)
                .dueAt(dueAtNew)
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
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(getBidPackageProjectResponse.getUsers().stream()
                .allMatch(u -> u.getUser().getAvatarColor() != null)).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"13744"})
    @Description("Create Bid Package project name is greater than 64 characters")
    public void createProjectNameGreaterThan64() {
        HashMap<String, String> prjAttributesMap = new HashMap<>();
        prjAttributesMap.put("projectName", RandomStringUtils.randomAlphabetic(70));
        ApwErrorMessage bppErrorResponse64 = QmsBidPackageResources.createBidPackageProject(prjAttributesMap, bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(bppErrorResponse64.getMessage())
            .contains("'name' should not be more than 64 characters");
    }

    @Test
    @TestRail(testCaseId = {"13745"})
    @Description("Create Bid Package project with empty name")
    public void createProjectWithEmptyName() {
        HashMap<String, String> prjAttributesMap = new HashMap<>();
        prjAttributesMap.put("projectName", "");
        ApwErrorMessage bppErrorResponse64 = QmsBidPackageResources.createBidPackageProject(prjAttributesMap, bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(bppErrorResponse64.getMessage()).contains("'name' should not be null");
    }

    @Test
    @TestRail(testCaseId = {"13747"})
    @Description("Create Bid Package with project description greater than 254 characters")
    public void createProjectDescGreaterThan254() {
        HashMap<String, String> prjAttributesMap = new HashMap<>();
        prjAttributesMap.put("projectDescription", RandomStringUtils.randomAlphabetic(258));
        ApwErrorMessage bppErrorResponse64 = QmsBidPackageResources.createBidPackageProject(prjAttributesMap, bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(bppErrorResponse64.getMessage())
            .contains("'description' should not be more than 254 characters");
    }

    @Test
    @TestRail(testCaseId = {"13743", "13749"})
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
    @TestRail(testCaseId = {"13746"})
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
    @TestRail(testCaseId = {"13754"})
    @Description("Verify bid package project is deleted")
    public void verifyBidPackageProjectIsDeleted() {
        QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.getBidPackageProject(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);
        softAssertions.assertThat(qmsErrorMessage.getMessage())
            .contains(String.format("Can't find Project for bid package with identity '%s' and identity '%s'", bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity()));
    }

    @Test
    @TestRail(testCaseId = {"13894"})
    @Description("Find a project with invalid identity")
    public void getProjectWithInvalidIdentity() {
        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.getBidPackageProject(bidPackageResponse.getIdentity(), "INVALID", ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(qmsErrorMessage.getMessage()).contains("'projectIdentity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"13753"})
    @Description("delete a project with invalid identity")
    public void deleteProjectWithInvalidIdentity() {
        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), "INVALID", ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(qmsErrorMessage.getMessage()).contains("'projectIdentity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"14625"})
    @Description("Verify that the user can find only those projects in which he participates")
    public void getProjectsForParticipant() {
        BidPackageProjectsResponse bidPackageProjectsResponse = QmsProjectResources.getProjects(BidPackageProjectsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(bidPackageProjectsResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"14626", "14469"})
    @Description("Verify that the user can find a project by identity in which he participates")
    public void getProjectForParticipant() {
        BidPackageProjectResponse bppResponse = QmsProjectResources.getProject(bidPackageProjectResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(bppResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"14627"})
    @Description("Verify that the user can find a project by identity in which he participates")
    public void getEmptyProjectsForParticipant() {
        BidPackageProjectsResponse bidProjectsResponse = QmsProjectResources.getProjects(BidPackageProjectsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(bidProjectsResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"14124", "14670"})
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
    @TestRail(testCaseId = {"24265"})
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
    @TestRail(testCaseId = {"24266"})
    @Description("Verify project display name can be updated to empty")
    public void updateEmptyProjectDisplayName() {
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .displayName("").build())
            .build();
        BidPackageProjectResponse getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getDisplayName()).isEmpty();
    }

    @Test
    @TestRail(testCaseId = {"24267"})
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
    }

    @Test
    @TestRail(testCaseId = {"24268"})
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
            .contains("displayName should not be null and have less than 64 characters");
    }

    @Test
    @TestRail(testCaseId = {"24269"})
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
    @TestRail(testCaseId = {"24270", "24295", "24427"})
    @Link("Defect - https://jira.apriori.com/browse/COL-1836")
    @Description("Verify project status can be updated to only following status 'IN_NEGOTIATION' ,'COMPLETED'  & 'PURCHASED'")
    public void updateProjectStatuses() {
        //C24270
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

        //C24427
        //Project Status is "OPEN"
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status("OPEN").build())
            .build();
        getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getStatus()).isEqualTo("OPEN");

        //Project Status is "IN_PROGRESS"
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status("IN_PROGRESS").build())
            .build();
        getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getStatus()).isEqualTo("IN_PROGRESS");

        //C24295
        //Project Status is "ACTIVE"
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status("ACTIVE").build())
            .build();
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("Status  can be changed to only \"IN_NEGOTIATION\",\"COMPLETED\" and PURCHASED\"");
    }

    @Test
    @TestRail(testCaseId = {"24275"})
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
    @TestRail(testCaseId = {"24276"})
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
            .contains("owner should not be null or empty");

        //Project owner is null
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .owner(null).build())
            .build();
        getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("owner should not be null or empty");

        //Project owner is invalid
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .owner("INVALID").build())
            .build();
        getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("Owner 'identity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"24278"})
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
    @TestRail(testCaseId = {"24279"})
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
    @TestRail(testCaseId = {"24280"})
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
    @TestRail(testCaseId = {"24281"})
    @Link("Defect - https://jira.apriori.com/browse/COL-1834")
    @Description("Verify dueAt can be updated to null or empty")
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
    @TestRail(testCaseId = {"24347"})
    @Link("Defect - https://jira.apriori.com/browse/COL-1834")
    @Description("Verify description can be updated to null/empty")
    public void updateEmptyProjectDescription() {
        //Project Description Name is empty
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .description("").build())
            .build();
        BidPackageProjectResponse getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getDescription()).isEmpty();

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
    @TestRail(testCaseId = {"24349"})
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
    @TestRail(testCaseId = {"24350"})
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
    @TestRail(testCaseId = {"24354"})
    @Description("Verify project display name cannot be updated to null")
    public void updateNullProjectDisplayName() {
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .displayName(null).build())
            .build();
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("displayName should not be null and have less than 64 characters");
    }

    @Test
    @TestRail(testCaseId = {"24351"})
    @Description("Verify updated owner/admin users can update the project attributes")
    public void updateProjectWithNewOwner() {
        //Update the project to new owner
        UserCredentials newOwner = UserUtil.getUser();
        String newOwnerUserContext = new AuthUserContextUtil().getAuthUserIdentity(newOwner.getEmail());
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .owner(newOwnerUserContext).build())
            .build();

        BidPackageProjectResponse getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getOwner()).isEqualTo(newOwnerUserContext);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(getBidPackageProjectResponse.getUsers().stream()
                    .anyMatch(u -> u.getRole().equals("ADMIN") &&
                        u.getUserIdentity().equals(newOwnerUserContext)))
                .isTrue();
        }

        //Update the project description with new owner
        String projectDescriptionNew = new GenerateStringUtil().getRandomString();
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .description(projectDescriptionNew)
                .build())
            .build();
        getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), newOwner, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getDescription()).isEqualTo(projectDescriptionNew);

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
    @TestRail(testCaseId = {"24313", "24412"})
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
            .contains("Incorrect date format it should be (yyyy-MM-dd Or yyyy-MM-dd'T'HH:mm:ss.SSS'Z')");

        //Invalid format #2
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .dueAt("2023-13-15T23:59:59.999Z").build())
            .build();
        getBidPackageProjectErrorResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains("Incorrect date format it should be (yyyy-MM-dd Or yyyy-MM-dd'T'HH:mm:ss.SSS'Z')");

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
    @TestRail(testCaseId = {"24462"})
    @Description("Verify project deletion is deleting all associated projectItems, discussions and bidPackageItems")
    public void deleteBidPackageProjectAndVerifyAllEntitiesDeleted() {
        ScenarioItem scenarioItem1 = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        ScenarioItem scenarioItem2 = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);

        BidPackageItemResponse bidPackageItemResponse1 = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem1.getComponentIdentity(), scenarioItem1.getScenarioIdentity(), scenarioItem1.getIterationIdentity()),
            bidPackageResponse.getIdentity(), currentUser, BidPackageItemResponse.class, HttpStatus.SC_CREATED);
        BidPackageItemResponse bidPackageItemResponse2 = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem2.getComponentIdentity(), scenarioItem2.getScenarioIdentity(), scenarioItem2.getIterationIdentity()),
            bidPackageResponse.getIdentity(), currentUser, BidPackageItemResponse.class, HttpStatus.SC_CREATED);

        BidPackageProjectResponse bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        ScenarioDiscussionResponse scenarioDiscussionResponse1 = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem1.getComponentIdentity(), scenarioItem1.getScenarioIdentity(), currentUser);
        ScenarioDiscussionResponse scenarioDiscussionResponse2 = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem1.getComponentIdentity(), scenarioItem1.getScenarioIdentity(), currentUser);
        ScenarioDiscussionResponse scenarioDiscussionResponse3 = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem2.getComponentIdentity(), scenarioItem2.getScenarioIdentity(), currentUser);
        ScenarioDiscussionResponse scenarioDiscussionResponse4 = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem2.getComponentIdentity(), scenarioItem2.getScenarioIdentity(), currentUser);

        //Delete Project
        QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        //Verify Project Deletion
        ApwErrorMessage getBidPackageProjectErrorResponse = QmsBidPackageResources.getBidPackageProject(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);
        softAssertions.assertThat(getBidPackageProjectErrorResponse.getMessage())
            .contains(String.format("Can't find project for bid package with identity '%s'", bidPackageResponse.getIdentity()));

        //Verify Bid package items deletion
        String[] bidPackageItemsIdsArr = new String[]{bidPackageItemResponse1.getIdentity(), bidPackageItemResponse2.getIdentity()};
        for (String bidPackageItemId : bidPackageItemsIdsArr) {
            ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.getBidPackageItem(bidPackageResponse.getIdentity(),
                bidPackageItemId,
                currentUser,
                ApwErrorMessage.class,
                HttpStatus.SC_NOT_FOUND);
            softAssertions.assertThat(qmsErrorMessage.getMessage())
                .contains(String.format("Can't find bidPackageItem for bid package with identity '%s' and identity '%s'",
                    bidPackageResponse.getIdentity(), bidPackageItemId));
        }

        //Verify Project-items deletion
        BidPackageProjectItemsResponse bpPItemsResponse = QmsBidPackageResources.getBidPackageProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            currentUser,
            BidPackageProjectItemsResponse.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(bpPItemsResponse.getItems().size()).isZero();

        //Verify Scenario Discussions deletion
        String[] discussionIdsArr = new String[]{scenarioDiscussionResponse1.getIdentity(), scenarioDiscussionResponse2.getIdentity(),
            scenarioDiscussionResponse3.getIdentity(), scenarioDiscussionResponse4.getIdentity()};
        for (String discussionId : discussionIdsArr) {
            ApwErrorMessage discussionErrorResponse = QmsScenarioDiscussionResources.getScenarioDiscussion(
                discussionId,
                ApwErrorMessage.class,
                HttpStatus.SC_NOT_FOUND,
                currentUser);
            softAssertions.assertThat(discussionErrorResponse.getMessage())
                .contains(String.format("Can't find scenario discussion for project with identity '%s'", bidPackageProjectResponse.getIdentity()));
        }

        //Delete Scenarios
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem1, currentUser);
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem2, currentUser);
    }

    @After
    public void testCleanup() {
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }

    private static final UserCredentials currentUser = UserUtil.getUser();
}
