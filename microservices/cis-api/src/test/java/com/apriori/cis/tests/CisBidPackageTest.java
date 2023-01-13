package com.apriori.cis.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageParameters;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageRequest;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageResponse;
import com.apriori.cisapi.entity.response.bidpackage.BidPackagesResponse;
import com.apriori.cisapi.entity.response.bidpackage.CisErrorMessage;
import com.apriori.cisapi.utils.CisBidPackageResources;
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

public class CisBidPackageTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"14126"})
    @Description("Create, Delete and verify Bid Package is deleted")
    public void createDeleteAndVerifyBidPackage() {
        String bpName = bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        BidPackageResponse createBidPackageResponse = CisBidPackageResources.createBidPackage(bpName, currentUser);
        softAssertions.assertThat(createBidPackageResponse.getName()).isEqualTo(bpName);

        CisBidPackageResources.deleteBidPackage(createBidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        CisErrorMessage cisErrorMessageResponse = CisBidPackageResources.deleteBidPackage(createBidPackageResponse.getIdentity(),
            CisErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);

        softAssertions.assertThat(cisErrorMessageResponse.getMessage()).contains("Can't find bidPackage with identity '" + createBidPackageResponse.getIdentity() + "'");
    }


    @Test
    @TestRail(testCaseId = {"13367", "13874", "14743"})
    @Description("Find List of bid packages and verify pagination for customer identity")
    public void getBidPackages() {
        BidPackagesResponse getBidPackagesResponse = CisBidPackageResources.getBidPackages(BidPackagesResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(getBidPackagesResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(getBidPackagesResponse.getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(getBidPackagesResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"13368"})
    @Description("Find List of bid packages from another user")
    public void getBidPackagesFromOtherUser() {
        UserCredentials otherUser = UserUtil.getUser();
        BidPackagesResponse bidPackagesResponse = CisBidPackageResources.getBidPackages(BidPackagesResponse.class, HttpStatus.SC_OK, otherUser);
        softAssertions.assertThat(bidPackagesResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"14128"})
    @Description("Get bid package by identity")
    public void getBidPackage() {
        BidPackageResponse getBidPackageResp = CisBidPackageResources.getBidPackage(bidPackageResponse.getIdentity(), BidPackageResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(getBidPackageResp.getName()).isEqualTo(bidPackageName);
    }

    @Test
    @TestRail(testCaseId = {"14129"})
    @Description("Updated existing Bid Package status")
    public void updateBidPackageStatus() {
        BidPackageRequest bidPackageRequestBuilder = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .name(bidPackageResponse.getName())
                .description(bidPackageResponse.getDescription())
                .status("COMPLETE")
                .build())
            .build();

        BidPackageResponse bidPackageUpdateResponse = CisBidPackageResources.updateBidPackage(bidPackageResponse.getIdentity(),
            bidPackageRequestBuilder, BidPackageResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(bidPackageUpdateResponse.getStatus()).isEqualTo("COMPLETE");
    }

    @After
    public void testCleanup() {
        CisBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }
}
