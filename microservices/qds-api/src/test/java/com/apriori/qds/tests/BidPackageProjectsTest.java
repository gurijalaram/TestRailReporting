package com.apriori.qds.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.qds.controller.BidPackageResources;
import com.apriori.qds.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BidPackageProjectsTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;
    private static String projectName;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = BidPackageResources.createBidPackage(bidPackageName, new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()));
        bidPackageProjectResponse = BidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13334", "13343"})
    @Description("Create and Delete Bid Package Project")
    public void createAndDeleteProject() {
        BidPackageProjectResponse bppResponse = BidPackageResources.createBidPackageProject(new GenerateStringUtil().getRandomNumbers(), bidPackageResponse.getIdentity(), currentUser);
        softAssertions.assertThat(bppResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bppResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13335", "13348"})
    @Description("Get all Bid Package Projects and verify pagination")
    public void getBidPackageProjects() {
        BidPackageProjectsResponse projectsResponse = BidPackageResources.getBidPackageProjects(bidPackageResponse.getIdentity(), currentUser);
        softAssertions.assertThat(projectsResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(projectsResponse.getIsFirstPage()).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"13338"})
    @Description("Find Bid Package Project By Identity")
    public void getBidPackageProject() {
        BidPackageProjectResponse getBidPackageProjectResponse = BidPackageResources.getBidPackageProject(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13340"})
    @Description("Update Bid Package Project By Identity")
    public void updateBidPackageProject() {
        BidPackageProjectRequest projectRequest = BidPackageResources.getBidPackageProjectRequestBuilder(new GenerateStringUtil().getRandomNumbers());
        BidPackageProjectResponse getBidPackageProjectResponse = BidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
    }

    @After
    public void testCleanup() {
        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), currentUser);
        softAssertions.assertAll();

    }
}
