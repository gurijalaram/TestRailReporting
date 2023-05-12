package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUserResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUsersResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ParticipantsResponse;
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

public class BidPackageProjectUserTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static BidPackageProjectUserResponse bidPackageProjectUserResponse;
    UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;
    private static String projectName;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        UserCredentials newUser = UserUtil.getUser();
        bidPackageProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser("DEFAULT",
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), newUser);
    }

    @Test
    @TestRail(testCaseId = {"15485", "13789"})
    @Description("Create and delete DEFAULT ROLE project user")
    public void createAndDeleteBidPackageDefaultProjectUser() {
        UserCredentials defaultUser = UserUtil.getUser();
        BidPackageProjectUserResponse bidPackageDefaultProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser("DEFAULT",
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), defaultUser);
        softAssertions.assertThat(bidPackageDefaultProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        QmsBidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageDefaultProjectUserResponse.getIdentity(),
            defaultUser);
    }

    @Test
    @TestRail(testCaseId = {"13782", "13788"})
    @Description("Create and delete ADMIN ROLE project user")
    public void createAndDeleteBidPackageAdminProjectUser() {
        UserCredentials adminUser = UserUtil.getUser();
        BidPackageProjectUserResponse bidPackageAdminProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser("ADMIN",
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), adminUser);

        softAssertions.assertThat(bidPackageAdminProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());

        QmsBidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageAdminProjectUserResponse.getIdentity(),
            adminUser);
    }

    @Test
    @TestRail(testCaseId = {"13793"})
    @Description("get bid package project user by identity")
    public void getBidPackageProjectUser() {
        BidPackageProjectUserResponse getBidPackageProjectUserResponse = QmsBidPackageResources.getBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectUserResponse.getIdentity(),
            currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13790"})
    @Description("find all bid package project users")
    public void getBidPackageProjectUsers() {
        BidPackageProjectUsersResponse getBidPackageProjectUserResponse = QmsBidPackageResources.getBidPackageProjectUsers(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            currentUser, BidPackageProjectUsersResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"13786"})
    @Description("Updated user role from default to admin")
    public void updateBidPackageDefaultProjectUser() {
        BidPackageProjectUserResponse updateBidPackageProjectUserResponse = QmsBidPackageResources.updateBidPackageProjectUser("ADMIN",
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectUserResponse.getIdentity(),
            currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageProjectUserResponse.getRole()).isEqualTo("ADMIN");
    }

    @Test
    @TestRail(testCaseId = {"14224", "14225", "14236"})
    @Description("Get list of possible users, pagination and validate json response schema")
    public void getParticipants() {
        ParticipantsResponse participantsResponse = QmsBidPackageResources.getParticipants(currentUser);

        softAssertions.assertThat(participantsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(participantsResponse.getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(participantsResponse.getItems().size()).isGreaterThan(0);
    }

    @After
    public void testCleanup() {
        QmsBidPackageResources.deleteBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectUserResponse.getIdentity(),
            currentUser);
        QmsBidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }
}
