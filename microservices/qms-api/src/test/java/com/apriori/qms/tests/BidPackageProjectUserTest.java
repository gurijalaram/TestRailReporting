package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUserResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUsersResponse;
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

public class BidPackageProjectUserTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static ResponseWrapper<BidPackageResponse> bidPackageResponse;
    private static ResponseWrapper<BidPackageProjectResponse> bidPackageProjectResponse;
    private static ResponseWrapper<BidPackageProjectUserResponse> bidPackageProjectUserResponse;
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
        UserCredentials newUser = UserUtil.getUser();
        bidPackageProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser("DEFAULT",
            bidPackageResponse.getResponseEntity().getIdentity(), bidPackageProjectResponse.getResponseEntity().getIdentity(), newUser);
    }

    @Test
    @TestRail(testCaseId = {"15485", "13789"})
    @Description("Create and delete DEFAULT ROLE project user")
    public void createAndDeleteBidPackageDefaultProjectUser() {
        UserCredentials defaultUser = UserUtil.getUser();
        ResponseWrapper<BidPackageProjectUserResponse> bidPackageDefaultProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser("DEFAULT",
            bidPackageResponse.getResponseEntity().getIdentity(), bidPackageProjectResponse.getResponseEntity().getIdentity(), defaultUser);
        softAssertions.assertThat(bidPackageDefaultProjectUserResponse.getResponseEntity().getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getResponseEntity().getIdentity());

        QmsBidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageDefaultProjectUserResponse.getResponseEntity().getIdentity(),
            defaultUser);
    }

    @Test
    @TestRail(testCaseId = {"13782", "13788"})
    @Description("Create and delete ADMIN ROLE project user")
    public void createAndDeleteBidPackageAdminProjectUser() {
        UserCredentials adminUser = UserUtil.getUser();
        ResponseWrapper<BidPackageProjectUserResponse> bidPackageAdminProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser("ADMIN",
            bidPackageResponse.getResponseEntity().getIdentity(), bidPackageProjectResponse.getResponseEntity().getIdentity(), adminUser);

        softAssertions.assertThat(bidPackageAdminProjectUserResponse.getResponseEntity().getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getResponseEntity().getIdentity());

        QmsBidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageAdminProjectUserResponse.getResponseEntity().getIdentity(),
            adminUser);
    }

    @Test
    @TestRail(testCaseId = {"13793"})
    @Description("get bid package project user by identity")
    public void getBidPackageProjectUser() {
        ResponseWrapper<BidPackageProjectUserResponse> getBidPackageProjectUserResponse = QmsBidPackageResources.getBidPackageProjectUser(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageProjectUserResponse.getResponseEntity().getIdentity(),
            currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getResponseEntity().getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13790"})
    @Description("find all bid package project users")
    public void getBidPackageProjectUsers() {
        ResponseWrapper<BidPackageProjectUsersResponse> getBidPackageProjectUserResponse = QmsBidPackageResources.getBidPackageProjectUsers(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            currentUser, BidPackageProjectUsersResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"13786"})
    @Description("Updated user role from default to admin")
    public void updateBidPackageDefaultProjectUser() {
        ResponseWrapper<BidPackageProjectUserResponse> updateBidPackageProjectUserResponse = QmsBidPackageResources.updateBidPackageProjectUser("ADMIN",
            bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageProjectUserResponse.getResponseEntity().getIdentity(),
            currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageProjectUserResponse.getResponseEntity().getRole()).isEqualTo("ADMIN");
    }
    
    @After
    public void testCleanup() {
        QmsBidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageProjectUserResponse.getResponseEntity().getIdentity(),
            currentUser);
        QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getResponseEntity().getIdentity(), bidPackageProjectResponse.getResponseEntity().getIdentity(), currentUser);
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
