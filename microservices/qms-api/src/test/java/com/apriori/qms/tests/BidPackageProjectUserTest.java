package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUserResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUsersDeleteResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUsersPostResponse;
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

import java.util.Collections;
import java.util.HashMap;

public class BidPackageProjectUserTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static BidPackageProjectUsersPostResponse bidPackageProjectUserResponse;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        String bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        UserCredentials newUser = UserUtil.getUser();
        bidPackageProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser("DEFAULT",
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), newUser);
    }

    @Test
    @TestRail(testCaseId = {"15485", "13789"})
    @Description("Create and delete DEFAULT ROLE project user")
    public void createAndDeleteBidPackageDefaultProjectUser() {
        UserCredentials defaultUser = UserUtil.getUser();
        BidPackageProjectUsersPostResponse bidPackageDefaultProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser("DEFAULT",
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), defaultUser);
        softAssertions.assertThat(bidPackageDefaultProjectUserResponse.get(0).getProjectIdentity())
            .isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageProjectUsersDeleteResponse deleteUserResponse = QmsBidPackageResources.deleteBidPackageProjectUser(Collections.singletonList(
                BidPackageProjectUserParameters.builder()
                    .identity(bidPackageDefaultProjectUserResponse.get(0)
                        .getIdentity()).build()),
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageDefaultProjectUserResponse.get(0).getIdentity(),
            defaultUser);

        softAssertions.assertThat(deleteUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(i -> i.getIdentity().equals(bidPackageDefaultProjectUserResponse.get(0).getIdentity()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"13782", "13788"})
    @Description("Create and delete ADMIN ROLE project user")
    public void createAndDeleteBidPackageAdminProjectUser() {
        UserCredentials adminUser = UserUtil.getUser();
        BidPackageProjectUsersPostResponse bidPackageAdminProjectUserResponse = QmsBidPackageResources.createBidPackageProjectUser("ADMIN",
            bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), adminUser);

        softAssertions.assertThat(bidPackageAdminProjectUserResponse.get(0).getProjectIdentity())
            .isEqualTo(bidPackageProjectResponse.getIdentity());

        BidPackageProjectUsersDeleteResponse deleteUserResponse = QmsBidPackageResources.deleteBidPackageProjectUser(Collections.singletonList(
                BidPackageProjectUserParameters.builder()
                    .identity(bidPackageAdminProjectUserResponse.get(0)
                        .getIdentity()).build()),
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageAdminProjectUserResponse.get(0).getIdentity(),
            adminUser);

        softAssertions.assertThat(deleteUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(i -> i.getIdentity().equals(bidPackageAdminProjectUserResponse.get(0).getIdentity()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"13793"})
    @Description("get bid package project user by identity")
    public void getBidPackageProjectUser() {
        BidPackageProjectUserResponse getBidPackageProjectUserResponse = QmsBidPackageResources.getBidPackageProjectUser(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectUserResponse.get(0).getIdentity(),
            currentUser, BidPackageProjectUserResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectUserResponse.getProjectIdentity())
            .isEqualTo(bidPackageProjectResponse.getIdentity());
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
            bidPackageProjectUserResponse.get(0).getIdentity(),
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
        BidPackageProjectUsersDeleteResponse deleteUserResponse = QmsBidPackageResources.deleteBidPackageProjectUser(Collections.singletonList(
                BidPackageProjectUserParameters.builder()
                    .identity(bidPackageProjectUserResponse.get(0)
                        .getIdentity()).build()),
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectUserResponse.get(0).getIdentity(),
            currentUser);

        softAssertions.assertThat(deleteUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(i -> i.getIdentity().equals(bidPackageProjectUserResponse.get(0).getIdentity()))).isTrue();
        softAssertions.assertAll();
    }

    private static final UserCredentials currentUser = UserUtil.getUser();
}
