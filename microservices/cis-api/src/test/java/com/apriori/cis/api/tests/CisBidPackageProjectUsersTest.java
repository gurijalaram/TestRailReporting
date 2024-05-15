package com.apriori.cis.api.tests;

import com.apriori.cis.api.controller.CisBidPackageProjectResources;
import com.apriori.cis.api.controller.CisBidPackageResources;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectUserRequest;
import com.apriori.cis.api.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.cis.api.models.response.bidpackage.BidPackageProjectUsersPostResponse;
import com.apriori.cis.api.models.response.bidpackage.BidPackageProjectUsersResponse;
import com.apriori.cis.api.models.response.bidpackage.BidPackageResponse;
import com.apriori.cis.api.models.response.bidpackage.CisErrorMessage;
import com.apriori.cis.api.util.CISTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class CisBidPackageProjectUsersTest extends CISTestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static String projectName;
    private static UserCredentials currentUser;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        String bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageProjectResponse = CisBidPackageProjectResources.createBidPackageProject(projectName,
            bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
    }


    @Test
    @TestRail(id = {24361, 24366, 13803, 13804})
    @Description("Add Multiple Users to the Project, Remove Multiple Users from the Project and remove specific user")
    public void testCreateBidPackageDefaultProjectUser() {
        UserCredentials firstUser = UserUtil.getUser();
        UserCredentials secondUser = UserUtil.getUser();
        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(firstUser.getEmail()).role("DEFAULT").build());
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(secondUser.getEmail()).role("GUEST").build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(usersList).build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = CisBidPackageProjectResources.createBidPackageProjectUser(bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(firstUser.getUserDetails().getIdentity()))).isTrue();
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(secondUser.getUserDetails().getIdentity()))).isTrue();

        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getFailures().size()).isZero();

        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(bulkProjectUserResponse.getProjectUsers().getSuccesses().get(0).getIdentity()).build());
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(bulkProjectUserResponse.getProjectUsers().getSuccesses().get(1).getIdentity()).build());
        CisBidPackageProjectResources.deleteBidPackageProjectUsers(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {14147})
    @Description("Get Bid Package Project Users")
    public void getBidPackageProjectUsers() {
        BidPackageProjectUsersResponse bidPackageProjectUsersResponse = CisBidPackageProjectResources.getBidPackageProjectUsers(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            BidPackageProjectUsersResponse.class,
            HttpStatus.SC_OK,
            currentUser);
        softAssertions.assertThat(bidPackageProjectUsersResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(bidPackageProjectUsersResponse.getIsFirstPage()).isTrue();
    }

    @Test
    @TestRail(id = {14153})
    @Description("Add a Project User with Invalid Project Identity")
    public void testProjectWithInvalidIdentity() {
        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(Collections.singletonList(BidPackageProjectUserParameters.builder()
                .userEmail(UserUtil.getUser().getEmail())
                .role("DEFAULT")
                .build()))
            .build();
        CisErrorMessage cisErrorMessage = CisBidPackageProjectResources.createBidPackageProjectUser(bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(),
            "INVALID", CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        softAssertions.assertThat(cisErrorMessage.getMessage()).isEqualTo("'projectIdentity' is not a valid identity.");
    }

    @AfterEach
    public void testCleanup() {
        CisBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT,
            currentUser);
        softAssertions.assertAll();
    }
}
