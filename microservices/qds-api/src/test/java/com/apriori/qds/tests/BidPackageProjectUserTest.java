package com.apriori.qds.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.qds.controller.BidPackageResources;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectUserResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectUsersResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BidPackageProjectUserTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static ResponseWrapper<BidPackageResponse> bidPackageResponse;
    private static ResponseWrapper<BidPackageProjectResponse> bidPackageProjectResponse;
    private static ResponseWrapper<BidPackageProjectUserResponse>  bidPackageProjectUserResponse;
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
        bidPackageProjectUserResponse = BidPackageResources.createBidPackageProjectUser("DEFAULT",
            bidPackageResponse.getResponseEntity().getIdentity(), bidPackageProjectResponse.getResponseEntity().getIdentity(),currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13352", "13649"})
    @Description("Create and delete DEFAULT ROLE project user")
    public void createAndDeleteBidPackageDefaultProjectUser() {
        UserCredentials defaultUser = UserUtil.getUser();
        ResponseWrapper<BidPackageProjectUserResponse>  bidPackageDefaultProjectUserResponse  = BidPackageResources.createBidPackageProjectUser("DEFAULT",
            bidPackageResponse.getResponseEntity().getIdentity(), bidPackageProjectResponse.getResponseEntity().getIdentity(),defaultUser);

        softAssertions.assertThat(bidPackageDefaultProjectUserResponse.getResponseEntity().getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getResponseEntity().getIdentity());

        BidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageDefaultProjectUserResponse.getResponseEntity().getIdentity(),
            defaultUser);
    }

    @Test
    @TestRail(testCaseId = {"13353", "13647"})
    @Description("Create and delete ADMIN ROLE project user")
    public void createAndDeleteBidPackageAdminProjectUser() {
        UserCredentials adminUser = UserUtil.getUser();
        ResponseWrapper<BidPackageProjectUserResponse>  bidPackageAdminProjectUserResponse = BidPackageResources.createBidPackageProjectUser("ADMIN",
            bidPackageResponse.getResponseEntity().getIdentity(), bidPackageProjectResponse.getResponseEntity().getIdentity(),adminUser);

        softAssertions.assertThat(bidPackageAdminProjectUserResponse.getResponseEntity().getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getResponseEntity().getIdentity());

        BidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageAdminProjectUserResponse.getResponseEntity().getIdentity(),
            adminUser);
    }

    @Test
    @TestRail(testCaseId = {"13355"})
    @Description("get bid package project user by identity")
    public void getBidPackageProjectUser() {
        ResponseWrapper<BidPackageProjectUserResponse> getBidPackageProjectUserResponse = BidPackageResources.getBidPackageProjectUser(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageProjectUserResponse.getResponseEntity().getIdentity(),
            currentUser, BidPackageProjectUserResponse.class);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getResponseEntity().getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13354"})
    @Description("find all bid package project users")
    public void getBidPackageProjectUsers() {
        ResponseWrapper<BidPackageProjectUsersResponse> getBidPackageProjectUserResponse = BidPackageResources.getBidPackageProjectUsers(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            currentUser, BidPackageProjectUsersResponse.class);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"13356"})
    @Description("Updated user role from default to admin")
    public void updateBidPackageDefaultProjectUser() {
        ResponseWrapper<BidPackageProjectUserResponse> updateBidPackageProjectUserResponse = BidPackageResources.updateBidPackageProjectUser("ADMIN",
            bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageProjectUserResponse.getResponseEntity().getIdentity(),
            currentUser, BidPackageProjectUserResponse.class);

        softAssertions.assertThat(updateBidPackageProjectUserResponse.getResponseEntity().getRole()).isEqualTo("ADMIN");
    }


    @After
    public void testCleanup() {
        BidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageProjectUserResponse.getResponseEntity().getIdentity(),
            currentUser);
        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getResponseEntity().getIdentity(), bidPackageProjectResponse.getResponseEntity().getIdentity(), currentUser);
        BidPackageResources.deleteBidPackage(bidPackageResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
