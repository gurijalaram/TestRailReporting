package com.apriori;

import com.apriori.cisapi.controller.CisBidPackageProjectResources;
import com.apriori.cisapi.controller.CisBidPackageResources;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageProjectUserRequest;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageProjectUsersPostResponse;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageResponse;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CisBidPackageProjectUsersTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static String projectName;
    private final UserCredentials currentUser = UserUtil.getUser();

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        String bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageProjectResponse = CisBidPackageProjectResources.createBidPackageProject(projectName,
            bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
    }

    @After
    public void testCleanup() {
        CisBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT,
            currentUser);
        softAssertions.assertAll();
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
}
