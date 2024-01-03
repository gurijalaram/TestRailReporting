package com.apriori.qds.api.tests;

import com.apriori.qds.api.controller.BidPackageResources;
import com.apriori.qds.api.enums.UserRole;
import com.apriori.qds.api.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qds.api.models.response.bidpackage.BidPackageProjectUserResponse;
import com.apriori.qds.api.models.response.bidpackage.BidPackageProjectUsersResponse;
import com.apriori.qds.api.models.response.bidpackage.BidPackageResponse;
import com.apriori.qds.api.utils.QdsApiTestUtils;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;

@ExtendWith(TestRulesAPI.class)
public class BidPackageProjectUserTest extends QdsApiTestUtils {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private UserCredentials currentUser;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        String bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = BidPackageResources.createBidPackage(bidPackageName, new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()));
        bidPackageProjectResponse = BidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {13352, 13649, 13725, 13729})
    @Description("Create, Get and delete DEFAULT ROLE project user")
    public void createAndDeleteBidPackageDefaultProjectUser() {
        UserCredentials defaultUser = UserUtil.getUser();
        UserCredentials adminUser = UserUtil.getUser();

        BidPackageProjectUserResponse defaultProjectUserResponse = BidPackageResources.createBidPackageProjectUser(UserRole.DEFAULT.getUserRole(),
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, defaultUser);

        softAssertions.assertThat(defaultProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageProjectUserResponse adminProjectUserResponse = BidPackageResources.createBidPackageProjectUser(UserRole.ADMIN.getUserRole(),
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, adminUser);

        softAssertions.assertThat(adminProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageProjectUserResponse getBidPackageProjectUserResponse = BidPackageResources.getBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            defaultProjectUserResponse.getIdentity(),
            defaultUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            defaultProjectUserResponse.getIdentity(),
            adminUser, null, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @TestRail(id = {13353, 13647, 13721, 13724})
    @Description("Create, Get and delete project user as ADMIN")
    public void createGetAndDeleteBidPackageAdminProjectUser() {
        UserCredentials projectUser = UserUtil.getUser();
        BidPackageProjectUserResponse bidPackageDefaultProjectUserResponse = BidPackageResources.createBidPackageProjectUser(UserRole.ADMIN.getUserRole(),
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, projectUser);

        softAssertions.assertThat(bidPackageDefaultProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageProjectUserResponse getBidPackageProjectUserResponse = BidPackageResources.getBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageDefaultProjectUserResponse.getIdentity(),
            currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageProjectUsersResponse getBidPackageProjectUsersResponse = BidPackageResources.getBidPackageProjectUsers(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            projectUser, BidPackageProjectUsersResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUsersResponse.getItems().size()).isGreaterThan(0);

        BidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageDefaultProjectUserResponse.getIdentity(),
            currentUser, null, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @TestRail(id = {13355})
    @Description("get bid package project user by identity")
    public void getBidPackageProjectUser()  {
        UserCredentials adminUser = UserUtil.getUser();
        BidPackageProjectUserResponse adminProjectUserResponse = BidPackageResources.createBidPackageProjectUser(UserRole.ADMIN.getUserRole(),
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, adminUser);

        softAssertions.assertThat(adminProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageProjectUserResponse getBidPackageProjectUserResponse = BidPackageResources.getBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            adminProjectUserResponse.getIdentity(),
            currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());
    }

    @Test
    @TestRail(id = {13354})
    @Description("find all bid package project users")
    public void getBidPackageProjectUsers() {
        UserCredentials adminUser = UserUtil.getUser();
        BidPackageProjectUserResponse adminProjectUserResponse = BidPackageResources.createBidPackageProjectUser(UserRole.ADMIN.getUserRole(),
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, adminUser);

        softAssertions.assertThat(adminProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageProjectUsersResponse getBidPackageProjectUserResponse = BidPackageResources.getBidPackageProjectUsers(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            currentUser, BidPackageProjectUsersResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {13356})
    @Description("Updated user role from default to admin")
    public void updateBidPackageDefaultProjectUser() {
        UserCredentials defaultUser = UserUtil.getUser();
        BidPackageProjectUserResponse defaultProjectUserResponse = BidPackageResources.createBidPackageProjectUser(UserRole.DEFAULT.getUserRole(),
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, defaultUser);

        BidPackageProjectUserResponse updateBidPackageProjectUserResponse = BidPackageResources.updateBidPackageProjectUser(UserRole.ADMIN.getUserRole(),
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            defaultProjectUserResponse.getIdentity(),
            currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageProjectUserResponse.getRole()).isEqualTo("ADMIN");
    }

    @Test
    @TestRail(id = {13730, 13731})
    @Description("delete GUEST and ADMIN Project User")
    public void deleteBidPackageProjectUser() {
        UserCredentials adminUser = UserUtil.getUser();
        UserCredentials guestUser = UserUtil.getUser();

        BidPackageProjectUserResponse adminProjectUserResponse = BidPackageResources.createBidPackageProjectUser(UserRole.ADMIN.getUserRole(),
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, adminUser);

        softAssertions.assertThat(adminProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageProjectUserResponse guestProjectUserResponse = BidPackageResources.createBidPackageProjectUser(UserRole.GUEST.getUserRole(),
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, guestUser);

        softAssertions.assertThat(guestProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            guestProjectUserResponse.getIdentity(),
            adminUser, null, HttpStatus.SC_NO_CONTENT);

        BidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            adminProjectUserResponse.getIdentity(),
            currentUser, null, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @TestRail(id = {13722})
    @Description("find all bid package project users by default user")
    public void getBidPackageByDefaultUser() {
        UserCredentials defaultUser = UserUtil.getUser();
        BidPackageProjectUserResponse defaultProjectUserResponse = BidPackageResources.createBidPackageProjectUser(UserRole.DEFAULT.getUserRole(),
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser, defaultUser);

        softAssertions.assertThat(defaultProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageProjectUsersResponse getBidPackageProjectUserResponse = BidPackageResources.getBidPackageProjectUsers(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            defaultUser, BidPackageProjectUsersResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getItems().size()).isGreaterThan(0);
    }

    @AfterEach
    public void testCleanup() {
        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
        BidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
