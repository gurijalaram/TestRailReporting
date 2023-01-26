package com.apriori.cis.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageProjectParameters;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageProjectProfile;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.cisapi.entity.request.bidpackage.CommentReminder;
import com.apriori.cisapi.entity.request.bidpackage.EmailReminder;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageResponse;
import com.apriori.cisapi.entity.response.bidpackage.CisErrorMessage;
import com.apriori.cisapi.utils.CisBidPackageResources;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CisBidPackageProjectsTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;
    private static String projectName;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageProjectResponse = CisBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"14378", "14098"})
    @Description("Create and Delete Bid Package Project")
    public void createAndDeleteProject() {
        BidPackageProjectResponse bppResponse = CisBidPackageResources.createBidPackageProject(new GenerateStringUtil().getRandomNumbers(),
            bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);
        softAssertions.assertThat(bppResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
        CisBidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bppResponse.getIdentity(),
            null, HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"15151"})
    @Description("Get list of all Bid Package Projects and verify pagination")
    public void getBidPackageProjects() {
        BidPackageProjectsResponse projectsResponse = CisBidPackageResources.getBidPackageProjects(bidPackageResponse.getIdentity(), currentUser, HttpStatus.SC_OK);

        softAssertions.assertThat(projectsResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(projectsResponse.getIsFirstPage()).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"14381", "14383"})
    @Description("Find Project By ID")
    public void getBidPackageProject() {
        BidPackageProjectResponse getBidPackageProjectResponse = CisBidPackageResources.getBidPackageProject(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(getBidPackageProjectResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"14383"})
    @Description("Find Projects - Invalid BidPackageID")
    public void testGetProjectsWithInvalidBidPackageId() {
        CisErrorMessage bidPackageProjectsError = CisBidPackageResources.getBidPackageProjects(
            "Invalid BidPackageID", currentUser, CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(bidPackageProjectsError.getMessage()).isEqualTo("'bidPackageIdentity' is not a valid identity.");
    }

    @Test
    @TestRail(testCaseId = {"14382"})
    @Description("Find Project - Invalid Data")
    public void testGetProjectWithInvalidData() {
        CisErrorMessage bidPackageProjectsError = CisBidPackageResources.getBidPackageProject(
            "Invalid BidPackageID", "Invalid ProjectID", CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        softAssertions.assertThat(bidPackageProjectsError.getMessage()).isEqualTo("'bidPackageIdentity' is not a valid identity.");
    }

    @Test
    @TestRail(testCaseId = {"14380"})
    @Description("Find Project - Invalid Data")
    public void testCreateProjectWithInvalidData() {
        CisErrorMessage bidPackageProjectsError = CisBidPackageResources.createBidPackageProject(
            projectName, "Invalid BidPackageID", CisErrorMessage.class, HttpStatus.SC_INTERNAL_SERVER_ERROR, currentUser);

        softAssertions.assertThat(bidPackageProjectsError.getMessage()).isEqualTo("'bidPackageIdentity' is not a valid identity.");
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("")
    public void testCreateProjectWithExistingName() {
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();

        BidPackageProjectResponse bppResponse = CisBidPackageResources.createBidPackageProject(projectName,
            bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getName()).isEqualTo(bidPackageName);

        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(bppResponse.getName())
                .description("sample2")
                .status("COMPLETED")
                .type("INTERNAL")
                .projectProfile(BidPackageProjectProfile.builder()
                    .emailReminder(EmailReminder.builder()
                        .active(true)
                        .startDuration("P1DT5M")
                        .frequencyValue("R2/P1D")
                        .build())
                    .commentReminder(CommentReminder.builder()
                        .active(true)
                        .startDuration("P1D")
                        .frequencyValue("R/P4H")
                        .build())
                    .build())
                .build())
            .build();

        CisErrorMessage cisErrorMessageResponse = CisBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(), CisErrorMessage.class, HttpStatus.SC_CONFLICT, currentUser);

        softAssertions.assertThat(cisErrorMessageResponse.getMessage()).isEqualTo("Project named '"
            + bppResponse.getName() + "' already exists for Customer '"
            + bppResponse.getBidPackageIdentity() + "'.");
    }

    @After
    public void testCleanup() {
        CisBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }
}
