package com.apriori;

import com.apriori.cis.controller.CisBidPackageProjectResources;
import com.apriori.cis.controller.CisBidPackageResources;
import com.apriori.cis.models.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.cis.models.request.bidpackage.BidPackageProjectUserRequest;
import com.apriori.cis.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.cis.models.response.bidpackage.BidPackageProjectUsersPostResponse;
import com.apriori.cis.models.response.bidpackage.BidPackageResponse;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CisBidPackageProjectUsersTest extends TestUtil {

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
    @TestRail(id = {24361})
    @Description("Add Multiple Users to the Project")
    public void testCreateBidPackageDefaultProjectUser() {
        String firstUserEmail = UserUtil.getUser().getEmail();
        String secondUserEmail = UserUtil.getUser().getEmail();
        String firstUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(firstUserEmail);
        String secondUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(secondUserEmail);

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(firstUserEmail).role("DEFAULT").build());
        usersList.add(BidPackageProjectUserParameters.builder().userEmail(secondUserEmail).role("GUEST").build());

        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(usersList).build();
        BidPackageProjectUsersPostResponse bulkProjectUserResponse = CisBidPackageProjectResources.createBidPackageProjectUser(bidPackageProjectUserRequestBuilder, bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), BidPackageProjectUsersPostResponse.class, currentUser);

        //Success
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(firstUserIdentity))).isTrue();
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getSuccesses().stream()
            .anyMatch(u -> u.getUserIdentity().equals(secondUserIdentity))).isTrue();

        //Failure
        softAssertions.assertThat(bulkProjectUserResponse.getProjectUsers().getFailures().size()).isZero();

        //Delete Added Users
        List<BidPackageProjectUserParameters> userIdentityList = new ArrayList<>();
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(firstUserIdentity).build());
        userIdentityList.add(BidPackageProjectUserParameters.builder().identity(secondUserIdentity).build());
        CisBidPackageProjectResources.deleteBidPackageProjectUser(userIdentityList, bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
    }

    @AfterEach
    public void testCleanup() {
        CisBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT,
            currentUser);
        softAssertions.assertAll();
    }
}
