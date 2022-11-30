package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
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
    private static ResponseWrapper<BidPackageResponse> bidPackageResponse;
    private static ResponseWrapper<BidPackageProjectResponse> bidPackageProjectResponse;
    UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;
    private static String projectName;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()));
        bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getResponseEntity().getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13742", "13752"})
    @Description("Create and Delete Bid Package Project")
    public void createAndDeleteProject() {
        ResponseWrapper<BidPackageProjectResponse> bppResponse = QmsBidPackageResources.createBidPackageProject(new GenerateStringUtil().getRandomNumbers(), bidPackageResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertThat(bppResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bidPackageResponse.getResponseEntity().getIdentity());
        QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getResponseEntity().getIdentity(), bppResponse.getResponseEntity().getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13898", "13899"})
    @Description("Get all Bid Package Projects and verify pagination")
    public void getBidPackageProjects() {
        ResponseWrapper<BidPackageProjectsResponse> projectsResponse = QmsBidPackageResources.getBidPackageProjects(bidPackageResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertThat(projectsResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(projectsResponse.getResponseEntity().getIsFirstPage()).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"13750"})
    @Description("Find Bid Package Project By Identity")
    public void getBidPackageProject() {
        ResponseWrapper<BidPackageProjectResponse> getBidPackageProjectResponse = QmsBidPackageResources.getBidPackageProject(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bidPackageResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13751"})
    @Description("Update Bid Package Project By Identity")
    public void updateBidPackageProject() {
        BidPackageProjectRequest projectRequest = QmsBidPackageResources.getBidPackageProjectRequestBuilder(new GenerateStringUtil().getRandomNumbers());
        ResponseWrapper<BidPackageProjectResponse> getBidPackageProjectResponse = QmsBidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getResponseEntity().getIdentity(), bidPackageProjectResponse.getResponseEntity().getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bidPackageResponse.getResponseEntity().getIdentity());
    }

    @After
    public void testCleanup() {
        QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertAll();

    }
}
