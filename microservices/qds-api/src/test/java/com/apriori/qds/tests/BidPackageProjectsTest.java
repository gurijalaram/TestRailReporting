package com.apriori.qds.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.qds.controller.BidPackageResources;
import com.apriori.qds.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qds.enums.QDSAPIEnum;
import com.apriori.qds.utils.QdsApiTestUtils;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
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
        bidPackageResponse = BidPackageResources.createBidPackage(bidPackageName, new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()));
        bidPackageProjectResponse = BidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getResponseEntity().getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13334", "13343", "13345"})
    @Description("Create, Delete and verify Bid Package Project is removed")
    public void createAndDeleteProject() {
        ResponseWrapper<BidPackageProjectResponse> bppResponse = BidPackageResources.createBidPackageProject(new GenerateStringUtil().getRandomNumbers(), bidPackageResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertThat(bppResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bidPackageResponse.getResponseEntity().getIdentity());
        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getResponseEntity().getIdentity(), bppResponse.getResponseEntity().getIdentity(), currentUser);

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT, ErrorMessage.class)
            .inlineVariables(bidPackageResponse.getResponseEntity().getIdentity(), bppResponse.getResponseEntity().getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NOT_FOUND);

        ResponseWrapper<ErrorMessage> deleteResponse = HTTPRequest.build(requestEntity).delete();

        softAssertions.assertThat(deleteResponse.getResponseEntity().getMessage()).contains("Can't find Project for bid package with identity");

    }

    @Test
    @TestRail(testCaseId = {"13335", "13348"})
    @Description("Get all Bid Package Projects and verify pagination")
    public void getBidPackageProjects() {
        ResponseWrapper<BidPackageProjectsResponse> projectsResponse = BidPackageResources.getBidPackageProjects(bidPackageResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertThat(projectsResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(projectsResponse.getResponseEntity().getIsFirstPage()).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"13338"})
    @Description("Find Bid Package Project By Identity")
    public void getBidPackageProject() {
        ResponseWrapper<BidPackageProjectResponse> getBidPackageProjectResponse = BidPackageResources.getBidPackageProject(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bidPackageResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13340"})
    @Description("Update Bid Package Project By Identity")
    public void updateBidPackageProject() {
        BidPackageProjectRequest projectRequest = BidPackageResources.getBidPackageProjectRequestBuilder(new GenerateStringUtil().getRandomNumbers());
        ResponseWrapper<BidPackageProjectResponse> getBidPackageProjectResponse = BidPackageResources.updateBidPackageProject(projectRequest,
            bidPackageResponse.getResponseEntity().getIdentity(), bidPackageProjectResponse.getResponseEntity().getIdentity(), currentUser, BidPackageProjectResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bidPackageResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13694"})
    @Description("Create project with name is equal to 64 characters")
    public void createProjectEqualTo64() {
        String projectName = RandomStringUtils.randomAlphabetic(64);
        BidPackageProjectRequest projectRequest = BidPackageResources.getBidPackageProjectRequestBuilder(projectName);
        ResponseWrapper<BidPackageProjectResponse> responseWrapper = BidPackageResources.createBidPackageProject(projectRequest,
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isEqualTo(projectName);
    }

    @Test
    @TestRail(testCaseId = {"13336"})
    @Description("Create project with name is greater than 64 characters")
    public void createProjectEqualGreaterThan64() {
        String projectName = RandomStringUtils.randomAlphabetic(75);
        BidPackageProjectRequest projectRequest = BidPackageResources.getBidPackageProjectRequestBuilder(projectName);
        projectRequest.getProject().setDescription(RandomStringUtils.randomAlphabetic(15));
        ResponseWrapper<ErrorMessage> responseWrapper = BidPackageResources.createBidPackageProject(projectRequest,
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            ErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).contains("'name' should not be more than 64 characters");
    }

    @Test
    @TestRail(testCaseId = {"13346"})
    @Description("Create project with project name is null")
    public void createProjectWithNameEmpty() {
        BidPackageProjectRequest projectRequest = BidPackageResources.getBidPackageProjectRequestBuilder("");
        projectRequest.getProject().setDescription(RandomStringUtils.randomAlphabetic(15));
        ResponseWrapper<ErrorMessage> responseWrapper = BidPackageResources.createBidPackageProject(projectRequest,
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            ErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).contains("'name' should not be null");
    }

    @Test
    @TestRail(testCaseId = {"13695"})
    @Description("Create project with project description size is equal to 254 characters")
    public void createProjectWithDescriptionIsEqualTo254() {
        String projectDesc = RandomStringUtils.randomAlphabetic(254);
        BidPackageProjectRequest projectRequest = BidPackageResources.getBidPackageProjectRequestBuilder(RandomStringUtils.randomAlphabetic(15));
        projectRequest.getProject().setDescription(projectDesc);
        ResponseWrapper<BidPackageProjectResponse> responseWrapper = BidPackageResources.createBidPackageProject(projectRequest,
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getDescription()).isEqualTo(projectDesc);
    }

    @Test
    @TestRail(testCaseId = {"13337"})
    @Description("Create project with project description size is greater than 254 characters")
    public void createProjectWithDescriptionIsGreaterThan254() {
        BidPackageProjectRequest projectRequest = BidPackageResources.getBidPackageProjectRequestBuilder(RandomStringUtils.randomAlphabetic(15));
        projectRequest.getProject().setDescription(RandomStringUtils.randomAlphabetic(265));
        ResponseWrapper<ErrorMessage> responseWrapper = BidPackageResources.createBidPackageProject(projectRequest,
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            ErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).contains("'description' should not be more than 254 characters");
    }

    @Test
    @TestRail(testCaseId = {"13347"})
    @Description("Create project with project description is null")
    public void createProjectWithDescriptionEmpty() {
        BidPackageProjectRequest projectRequest = BidPackageResources.getBidPackageProjectRequestBuilder(RandomStringUtils.randomAlphabetic(15));
        projectRequest.getProject().setDescription("");
        ResponseWrapper<ErrorMessage> responseWrapper = BidPackageResources.createBidPackageProject(projectRequest,
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            ErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).contains("'description' should not be null");
    }

    @Test
    @TestRail(testCaseId = {"13351"})
    @Description("Create project by non-existing bid-package")
    public void createProjectWithBidPackageNotExist() {
        ResponseWrapper<BidPackageResponse> bpResponse = BidPackageResources.createBidPackage(RandomStringUtils.randomAlphabetic(10), new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()));
        BidPackageResources.deleteBidPackage(bpResponse.getResponseEntity().getIdentity(), currentUser);
        BidPackageProjectRequest projectRequest = BidPackageResources.getBidPackageProjectRequestBuilder(RandomStringUtils.randomAlphabetic(15));
        ResponseWrapper<ErrorMessage> responseWrapper = BidPackageResources.createBidPackageProject(projectRequest,
            bpResponse.getResponseEntity().getIdentity(),
            currentUser,
            ErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).contains("Can't find bidPackage with identity '" + bpResponse.getResponseEntity().getIdentity() + "' for customerIdentity '" + PropertiesContext.get("${env}.customer_identity") + "'");
    }

    @Test
    @TestRail(testCaseId = {"13339"})
    @Description("Find a project with invalid project identity")
    public void getBidPackageProjectInvalidIdentity() {
        ResponseWrapper<ErrorMessage> getBidPackageProjectResponse = BidPackageResources.getBidPackageProject(bidPackageResponse.getResponseEntity().getIdentity(),
            "INVALIDIDENTITY", currentUser, ErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(getBidPackageProjectResponse.getResponseEntity().getMessage()).contains("'identity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"13344"})
    @Description("Delete a project with invalid project identity")
    public void deleteBidPackageProjectInvalidIdentity() {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT, ErrorMessage.class)
            .inlineVariables(bidPackageResponse.getResponseEntity().getIdentity(), "INVALIDIDENTITY")
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ErrorMessage> deleteResponse = HTTPRequest.build(requestEntity).delete();

        softAssertions.assertThat(deleteResponse.getResponseEntity().getMessage()).contains("'identity' is not a valid identity");
    }

    @After
    public void testCleanup() {
        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertAll();

    }
}
