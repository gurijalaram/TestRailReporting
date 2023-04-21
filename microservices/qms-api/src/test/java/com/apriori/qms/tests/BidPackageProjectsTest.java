package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.utils.ApwErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BidPackageProjectsTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static String bidPackageName;
    private static String projectName;
    UserCredentials currentUser = UserUtil.getUser();

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13742", "13752", "22955"})
    @Description("Create and Delete Bid Package Project")
    public void createAndDeleteProject() {
        BidPackageProjectResponse bppResponse = QmsBidPackageResources.createBidPackageProject(new GenerateStringUtil().getRandomNumbers(),
            bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);
        softAssertions.assertThat(bppResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
        if (softAssertions.wasSuccess()) {
            for (int j = 0; j < bppResponse.getUsers().size(); j++) {
                softAssertions.assertThat(bppResponse.getUsers().get(j).getUser().getAvatarColor()).isNotNull();
            }
        }
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
    @TestRail(testCaseId = {"13751", "22958"})
    @Description("Update Bid Package Project By Identity")
    public void updateBidPackageProject() {
        BidPackageProjectRequest projectRequest = QmsBidPackageResources.getBidPackageProjectRequestBuilder(new GenerateStringUtil().getRandomNumbers(), new GenerateStringUtil().getRandomNumbers());
        BidPackageProjectResponse getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
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
    @TestRail(testCaseId = {"13744"})
    @Description("Create Bid Package is greater than 64 characters")
    public void createProjectNameGreaterThan64() {
        ApwErrorMessage bppErrorResponse64 = QmsBidPackageResources.createBidPackageProject(RandomStringUtils.randomAlphabetic(70), bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(bppErrorResponse64.getMessage())
            .contains("'name' should not be more than 64 characters");
    }

    @Test
    @TestRail(testCaseId = {"13745"})
    @Description("Create Bid Package with empty name")
    public void createProjectWithEmptyName() {
        ApwErrorMessage bppErrorResponse64 = QmsBidPackageResources.createBidPackageProject("", bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(bppErrorResponse64.getMessage()).contains("'name' should not be null");
    }

    @Test
    @TestRail(testCaseId = {"13746"})
    @Description("Create Bid Package with project description greater than 254 characters")
    public void createProjectNameGreaterThan254() {
        ApwErrorMessage bppErrorResponse64 = QmsBidPackageResources.createBidPackageProject(RandomStringUtils.randomAlphabetic(258), bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(bppErrorResponse64.getMessage())
            .contains("'description' should not be more than 254 characters");
    }

    @Test
    @TestRail(testCaseId = {"13743", "13749"})
    @Description("Create Bid Package is equal to 64 characters, delete the bid package and create project with deleted Bid Package identity")
    public void createProjectNameEqualTo64() {
        String projectName = RandomStringUtils.randomAlphabetic(64);
        BidPackageProjectResponse bppResponse = QmsBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        softAssertions.assertThat(bppResponse.getName()).isEqualTo(projectName);

        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        ApwErrorMessage bppErrorResponse = QmsBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);

        softAssertions.assertThat(bppErrorResponse.getMessage())
            .contains(String.format("Can't find bidPackage with identity '%s' for customerIdentity '%s'", bidPackageResponse.getIdentity(), bidPackageResponse.getCustomerIdentity()));
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13746"})
    @Description("Create Bid Package description is equal to 254 characters")
    public void createProjectDescEqualTo254() {
        String projectName254 = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        String projectDesc254 = RandomStringUtils.randomAlphabetic(254);
        BidPackageProjectRequest projectRequestBuilder = QmsBidPackageResources.getBidPackageProjectRequestBuilder(projectName254, projectDesc254);
        BidPackageProjectResponse bppResponse = QmsBidPackageResources.createBidPackageProject(projectRequestBuilder,
            bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getName()).isEqualTo(projectName254);

        QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bppResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13748"})
    @Description("Create Bid Package with empty description")
    public void createProjectWithEmptyDescription() {
        String projectName12 = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        BidPackageProjectRequest projectRequestBuilder = QmsBidPackageResources.getBidPackageProjectRequestBuilder(projectName12, "");
        ApwErrorMessage bppErrorResponse64 = QmsBidPackageResources.createBidPackageProject(projectRequestBuilder, bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(bppErrorResponse64.getMessage()).contains("'description' should not be null");
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
        BidPackageProjectsResponse bidPackageProjectsResponse = QmsBidPackageResources.getProjects(BidPackageProjectsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(bidPackageProjectsResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"14626", "14469"})
    @Description("Verify that the user can find a project by identity in which he participates")
    public void getProjectForParticipant() {
        BidPackageProjectResponse bppResponse = QmsBidPackageResources.getProject(bidPackageProjectResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(bppResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"14627"})
    @Description("Verify that the user can find a project by identity in which he participates")
    public void getEmptyProjectsForParticipant() {
        BidPackageProjectsResponse bidProjectsResponse = QmsBidPackageResources.getProjects(BidPackageProjectsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(bidProjectsResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"14124", "14670"})
    @Description("Verify that project is deleted after deleting bid-package")
    public void verifyProjectIsDeletedAfterBidPackageDeleted() {
        String bidPackName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        String bidProjectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        BidPackageResponse bidPackResponse = QmsBidPackageResources.createBidPackage(bidPackName, currentUser);
        softAssertions.assertThat(bidPackResponse.getName()).isEqualTo(bidPackName);

        BidPackageProjectResponse bidPackProjectResponse = QmsBidPackageResources.createBidPackageProject(bidProjectName, bidPackResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        softAssertions.assertThat(bidPackProjectResponse.getName()).isEqualTo(bidProjectName);

        QmsBidPackageResources.deleteBidPackage(bidPackResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.getBidPackageProject(bidPackResponse.getIdentity(),
            bidPackProjectResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);

        softAssertions.assertThat(qmsErrorMessage.getMessage())
            .contains(String.format("Can't find bidPackage with identity '%s' for customerIdentity '%s'", bidPackResponse.getIdentity(), bidPackResponse.getCustomerIdentity()));

        ApwErrorMessage bppErrorResponse = QmsBidPackageResources.getProject(bidPackProjectResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);
        softAssertions.assertThat(bppErrorResponse.getMessage())
            .contains(String.format("Resource 'Project' with identity '%s' was not found", bidPackProjectResponse.getIdentity()));
    }

    @After
    public void testCleanup() {
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }
}
