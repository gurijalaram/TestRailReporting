package com.apriori;

import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.DateUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.qds.controller.BidPackageResources;
import com.apriori.qds.models.request.bidpackage.BidPackageProjectParameters;
import com.apriori.qds.models.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qds.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qds.models.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qds.models.response.bidpackage.BidPackageResponse;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

public class BidPackageProjectsTest extends TestUtil {

    private static final UserCredentials currentUser = UserUtil.getUser();
    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        String bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = BidPackageResources.createBidPackage(bidPackageName, new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()));
        bidPackageProjectResponse = BidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {13334, 13343, 24418})
    @Description("Create and Delete Bid Package Project")
    public void createAndDeleteProject() {
        BidPackageProjectResponse bppResponse = BidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), currentUser);
        softAssertions.assertThat(bppResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
        softAssertions.assertThat(bppResponse.getDueAt().toLocalTime()).isNotNull();
        softAssertions.assertThat(bppResponse.getOwnerUserIdentity()).isNotNull();
        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bppResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {13335, 13348, 24419})
    @Description("Get all Bid Package Projects and verify pagination")
    public void getBidPackageProjects() {
        BidPackageProjectsResponse projectsResponse = BidPackageResources.getBidPackageProjects(bidPackageResponse.getIdentity(), currentUser);
        softAssertions.assertThat(projectsResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(projectsResponse.getIsFirstPage()).isTrue();
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(projectsResponse.getItems().stream()
                .allMatch(p -> p.getDueAt().toLocalTime() != null)).isTrue();
            softAssertions.assertThat(projectsResponse.getItems().stream()
                .allMatch(p -> p.getOwnerUserIdentity() != null)).isTrue();
        }
    }

    @Test
    @TestRail(id = {13338, 24419})
    @Description("Find Bid Package Project By Identity")
    public void getBidPackageProject() {
        BidPackageProjectResponse getBidPackageProjectResponse = BidPackageResources.getBidPackageProject(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
        softAssertions.assertThat(getBidPackageProjectResponse.getDueAt().toLocalTime()).isNotNull();
        softAssertions.assertThat(getBidPackageProjectResponse.getOwnerUserIdentity()).isNotNull();
    }

    @Test
    @TestRail(id = {13340, 24420})
    @Description("Update Bid Package Project By Identity")
    public void updateBidPackageProject() {
        String projectNameNew = new GenerateStringUtil().getRandomString();
        String projectDescriptionNew = new GenerateStringUtil().getRandomString();
        String statusNew = "COMPLETED";
        String dueAtNew = DateUtil.getDateDaysAfter(15, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(projectNameNew)
                .description(projectDescriptionNew)
                .status(statusNew)
                .dueAt(dueAtNew)
                .build())
            .build();
        BidPackageProjectResponse getBidPackageProjectResponse = BidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
        softAssertions.assertThat(getBidPackageProjectResponse.getName()).isEqualTo(projectNameNew);
        softAssertions.assertThat(getBidPackageProjectResponse.getDescription()).isEqualTo(projectDescriptionNew);
        softAssertions.assertThat(getBidPackageProjectResponse.getStatus()).isEqualTo(statusNew);
        softAssertions.assertThat(getBidPackageProjectResponse.getDueAt())
            .isEqualTo(LocalDateTime.parse(dueAtNew, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ));
        softAssertions.assertThat(getBidPackageProjectResponse.getDueAt().toLocalTime()).isNotNull();
        softAssertions.assertThat(getBidPackageProjectResponse.getOwnerUserIdentity()).isNotNull();
    }

    @Test
    @TestRail(id = {24424, 24425})
    @Description("Verify DueAt & Description field is optional for Project creation")
    public void createBidPackageProjectWithOptionalFields() {
        HashMap<String, String> projectAttributes = new HashMap<>();
        projectAttributes.put("projectDueAt", "N/A");
        projectAttributes.put("projectDescription", "N/A");
        BidPackageProjectResponse bppResponse = BidPackageResources.createBidPackageProject(projectAttributes, bidPackageResponse.getIdentity(), currentUser);
        softAssertions.assertThat(bppResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
        softAssertions.assertThat(bppResponse.getDueAt()).isNull();
        softAssertions.assertThat(bppResponse.getDescription()).isNull();
        softAssertions.assertThat(bppResponse.getOwnerUserIdentity()).isNotNull();
        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bppResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {24426})
    @Description("Verify for project status can be updated to OPEN or IN_PROGRESS")
    public void updateBidPackageProjectValidStatus() {
        //OPEN Status
        String statusNew = "OPEN";
        BidPackageProjectRequest projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status(statusNew)
                .build())
            .build();
        BidPackageProjectResponse getBidPackageProjectResponse = BidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
        softAssertions.assertThat(getBidPackageProjectResponse.getStatus()).isEqualTo(statusNew);

        //IN_PROGRESS Status
        statusNew = "IN_PROGRESS";
        projectRequest = BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .status(statusNew)
                .build())
            .build();
        getBidPackageProjectResponse = BidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
        softAssertions.assertThat(getBidPackageProjectResponse.getStatus()).isEqualTo(statusNew);
    }

    @AfterEach
    public void testCleanup() {
        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), currentUser);
        softAssertions.assertAll();

    }
}
