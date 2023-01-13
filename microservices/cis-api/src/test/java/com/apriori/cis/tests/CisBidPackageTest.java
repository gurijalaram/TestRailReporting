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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CisBidPackageTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;

    @BeforeClass
    public static void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"14126", "14130"})
    @Description("Create, Delete and verify Bid Package is deleted")
    public void testCreateDeleteAndVerifyBidPackage() {
        String bpName = bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        BidPackageResponse createBidPackageResponse = CisBidPackageResources.createBidPackage(bpName, currentUser);
        softAssertions.assertThat(createBidPackageResponse.getName()).isEqualTo(bpName);

        CisBidPackageResources.deleteBidPackage(createBidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        CisErrorMessage cisErrorMessageResponse = CisBidPackageResources.deleteBidPackage(createBidPackageResponse.getIdentity(),
            CisErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);

        softAssertions.assertThat(cisErrorMessageResponse.getMessage()).contains("Can't find bidPackage with identity '" + createBidPackageResponse.getIdentity() + "'");
    }


    @Test
    @TestRail(testCaseId = {"14131"})
    @Description("Find List of bid packages and verify pagination for customer identity")
    public void testGetBidPackages() {
        BidPackagesResponse getBidPackagesResponse = CisBidPackageResources.getBidPackages(BidPackagesResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(getBidPackagesResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(getBidPackagesResponse.getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(getBidPackagesResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"14128"})
    @Description("Get bid package by identity")
    public void testGetBidPackage() {
        BidPackageResponse getBidPackageResp = CisBidPackageResources.getBidPackage(bidPackageResponse.getIdentity(), BidPackageResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(getBidPackageResp.getName()).isEqualTo(bidPackageName);
    }

    @Test
    @TestRail(testCaseId = {"14129"})
    @Description("Updated existing Bid Package status")
    public void testUpdateBidPackageStatus() {
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

    @Test
    @TestRail(testCaseId = {"14372"})
    @Description("Create Bid Package empty name")
    public void testCreateBidPackageEmptyName() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("Test Description")
                .name("")
                .status("IN_NEGOTIATION")
                .build())
            .build();

        CisErrorMessage cisErrorMessageResponse = CisBidPackageResources.createBidPackage(bidPackageRequest, CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        softAssertions.assertThat(cisErrorMessageResponse.getMessage()).contains("'name' should not be null");
    }

    @Test
    @TestRail(testCaseId = {"14129"})
    @Description("Create Bid Package empty name")
    public void testUpdateBidPackageWithInvalidIdentity() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("Update description ASSDEF")
                .name("BIITest")
                .status("COMPLETE")
                .build())
            .build();

        CisErrorMessage cisErrorMessageResponse = CisBidPackageResources.updateBidPackage("123456",
            bidPackageRequest, CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        softAssertions.assertThat(cisErrorMessageResponse.getMessage()).contains("'identity' is not a valid identity");
    }

    @AfterClass
    public static void testCleanup() {
        CisBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }
}
