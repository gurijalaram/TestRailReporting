package com.apriori.cis.api.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.cis.api.controller.CisBidPackageProjectResources;
import com.apriori.cis.api.controller.CisBidPackageResources;
import com.apriori.cis.api.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.cis.api.models.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.cis.api.models.response.bidpackage.BidPackageResponse;
import com.apriori.cis.api.models.response.bidpackage.CisErrorMessage;
import com.apriori.cis.api.util.CISTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CisBidPackageProjectsTest extends CISTestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static String projectName;
    private static UserCredentials currentUser;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        String bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);
    }

    @Test
    @TestRail(id = {14378, 14098})
    @Description("Create and Delete Bid Package Project")
    public void testCreateAndDeleteProject() {
        BidPackageProjectResponse bppResponse = CisBidPackageProjectResources.createBidPackageProject(new GenerateStringUtil().getRandomNumbers(),
            bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);
        softAssertions.assertThat(bppResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
        CisBidPackageProjectResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bppResponse.getIdentity(),
            HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {15151, 14384})
    @Description("Get list of all Bid Package Projects and verify pagination")
    public void testGetBidPackageProjects() {
        bidPackageProjectResponse = CisBidPackageProjectResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        BidPackageProjectsResponse projectsResponse = CisBidPackageProjectResources.getBidPackageProjects(bidPackageResponse.getIdentity(), currentUser, BidPackageProjectsResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(projectsResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(projectsResponse.getIsFirstPage()).isTrue();
        CisBidPackageProjectResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(),
            HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @Test
    @TestRail(id = {14381, 14383, 14376})
    @Description("Find Project By ID")
    public void testGetBidPackageProject() {
        bidPackageProjectResponse = CisBidPackageProjectResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        BidPackageProjectResponse getBidPackageProjectResponse = CisBidPackageProjectResources.getBidPackageProject(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(getBidPackageProjectResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
        CisBidPackageProjectResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(),
            HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @Test
    @TestRail(id = {14384})
    @Description("Find Projects - Invalid BidPackageID")
    public void testGetProjectsWithInvalidBidPackageId() {
        CisErrorMessage bidPackageProjectsError = CisBidPackageProjectResources.getBidPackageProjects(
            "Invalid BidPackageID", currentUser, CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(bidPackageProjectsError.getMessage())
            .isEqualTo("'bidPackageIdentity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14382})
    @Description("Find Project - Invalid Data")
    public void testGetProjectWithInvalidData() {
        CisErrorMessage bidPackageProjectsError = CisBidPackageProjectResources.getBidPackageProject(
            "Invalid BidPackageID", "Invalid ProjectID", CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        softAssertions.assertThat(bidPackageProjectsError.getMessage())
            .isEqualTo("2 validation failures were found:" +
                "\n* 'bidPackageIdentity' is not a valid identity." +
                "\n* 'projectIdentity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14380})
    @Description("Find Project - Invalid Data")
    public void testCreateProjectWithInvalidData() {
        CisErrorMessage bidPackageProjectsError = CisBidPackageProjectResources.createBidPackageProject(
            projectName, "Invalid BidPackageID", CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        softAssertions.assertThat(bidPackageProjectsError.getMessage())
            .isEqualTo("'bidPackageIdentity' is not a valid identity.");
    }

    @Test
    @TestRail(id = 14379)
    @Description("Create Project with Existing Name")
    public void testCreateProjectWithExistingName() {
        String newProjectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();

        BidPackageProjectResponse bppResponse = CisBidPackageProjectResources.createBidPackageProject(newProjectName,
            bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getName()).isEqualTo(newProjectName);

        CisErrorMessage cisErrorMessageResponse = CisBidPackageProjectResources.createBidPackageProject(newProjectName, bidPackageResponse.getIdentity(), CisErrorMessage.class, HttpStatus.SC_CONFLICT, currentUser);

        softAssertions.assertThat(cisErrorMessageResponse.getMessage()).isEqualTo("Project named '"
            + bppResponse.getName() + "' already exists for bid package with identity '"
            + bppResponse.getBidPackageIdentity() + "'.");
    }

    @AfterEach
    public void testCleanup() {
        softAssertions.assertAll();
    }
}
