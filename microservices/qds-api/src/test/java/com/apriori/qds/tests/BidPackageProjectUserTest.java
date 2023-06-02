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
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class BidPackageProjectUserTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static BidPackageProjectUserResponse bidPackageProjectUserResponse;
    UserCredentials currentUser = UserUtil.getUser();

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        String bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = BidPackageResources.createBidPackage(bidPackageName, new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()));
        bidPackageProjectResponse = BidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), currentUser);
        bidPackageProjectUserResponse = BidPackageResources.createBidPackageProjectUser("DEFAULT",
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13352", "13649"})
    @Description("Create and delete DEFAULT ROLE project user")
    public void createAndDeleteBidPackageDefaultProjectUser() {
        UserCredentials defaultUser = UserUtil.getUser();
        BidPackageProjectUserResponse bidPackageDefaultProjectUserResponse = BidPackageResources.createBidPackageProjectUser("DEFAULT",
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), defaultUser);

        softAssertions.assertThat(bidPackageDefaultProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageDefaultProjectUserResponse.getIdentity(),
            defaultUser);
    }

    @Test
    @TestRail(testCaseId = {"13353", "13647"})
    @Description("Create and delete ADMIN ROLE project user")
    public void createAndDeleteBidPackageAdminProjectUser() {
        UserCredentials adminUser = UserUtil.getUser();
        BidPackageProjectUserResponse bidPackageAdminProjectUserResponse = BidPackageResources.createBidPackageProjectUser("ADMIN",
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), adminUser);

        softAssertions.assertThat(bidPackageAdminProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageAdminProjectUserResponse.getIdentity(),
            adminUser);
    }

    @Test
    @TestRail(testCaseId = {"13355"})
    @Description("get bid package project user by identity")
    public void getBidPackageProjectUser() {
        BidPackageProjectUserResponse getBidPackageProjectUserResponse = BidPackageResources.getBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectUserResponse.getIdentity(),
            currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13354"})
    @Description("find all bid package project users")
    public void getBidPackageProjectUsers() {
        BidPackageProjectUsersResponse getBidPackageProjectUserResponse = BidPackageResources.getBidPackageProjectUsers(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            currentUser, BidPackageProjectUsersResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"13356"})
    @Description("Updated user role from default to admin")
    public void updateBidPackageDefaultProjectUser() {
        BidPackageProjectUserResponse updateBidPackageProjectUserResponse = BidPackageResources.updateBidPackageProjectUser("ADMIN",
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectUserResponse.getIdentity(),
            currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageProjectUserResponse.getRole()).isEqualTo("ADMIN");
    }

    @After
    public void testCleanup() {
        BidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectUserResponse.getIdentity(),
            currentUser);
        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
        BidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
