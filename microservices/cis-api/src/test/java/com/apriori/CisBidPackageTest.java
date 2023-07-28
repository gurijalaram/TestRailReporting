package com.apriori;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import com.apriori.cisapi.controller.CisBidPackageResources;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageParameters;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageRequest;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageResponse;
import com.apriori.cisapi.entity.response.bidpackage.BidPackagesResponse;
import com.apriori.cisapi.entity.response.bidpackage.CisErrorMessage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CisBidPackageTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static final UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;

    @BeforeClass
    public static void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);
    }

    @Test
    @TestRail(id = {14126, 14130})
    @Description("Create, Delete and verify Bid Package is deleted")
    public void testCreateDeleteAndVerifyBidPackage() {
        String bpName = bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        BidPackageResponse createBidPackageResponse = CisBidPackageResources.createBidPackage(bpName, currentUser);
        softAssertions.assertThat(createBidPackageResponse.getName()).isEqualTo(bpName);

        CisBidPackageResources.deleteBidPackage(createBidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        CisErrorMessage cisErrorMessageResponse = CisBidPackageResources.deleteBidPackage(createBidPackageResponse.getIdentity(),
            CisErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);

        softAssertions.assertThat(cisErrorMessageResponse.getMessage()).contains("Can't find bidPackage with identity '" + createBidPackageResponse.getIdentity() + "'");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14131})
    @Description("Find List of bid packages and verify pagination for customer identity")
    public void testGetBidPackages() {
        BidPackagesResponse getBidPackagesResponse = CisBidPackageResources.getBidPackages(BidPackagesResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(getBidPackagesResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(getBidPackagesResponse.getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(getBidPackagesResponse.getItems().size()).isGreaterThan(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14128})
    @Description("Get bid package by identity")
    public void testGetBidPackage() {
        BidPackageResponse getBidPackageResp = CisBidPackageResources.getBidPackage(bidPackageResponse.getIdentity(), BidPackageResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(getBidPackageResp.getName()).isEqualTo(bidPackageName);
    }

    @Test
    @TestRail(id = {14129})
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

        assertThat(bidPackageUpdateResponse.getStatus(), equalTo("COMPLETE"));
    }

    @Test
    @TestRail(id = {14372})
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

        assertThat(cisErrorMessageResponse.getMessage(), containsString("'name' should not be null"));
    }

    @Test
    @TestRail(id = {14375})
    @Description("Create Bid Package empty name")
    public void testUpdateBidPackageWithInvalidIdentity() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("Update description")
                .name("BIITest")
                .status("COMPLETE")
                .build())
            .build();

        CisErrorMessage cisErrorMessageResponse = CisBidPackageResources.updateBidPackage("123456",
            bidPackageRequest, CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        assertThat(cisErrorMessageResponse.getMessage(), containsString("'identity' is not a valid identity"));
    }

    @Test
    @TestRail(id = {14371})
    @Description("Create Bid Package empty name")
    public void testCreateBidPackageWithExistingName() {
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        BidPackageResponse createBidPackageResponse = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);

        softAssertions.assertThat(createBidPackageResponse.getName()).isEqualTo(bidPackageName);

        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("Test Description")
                .name(createBidPackageResponse.getName())
                .status("ASSIGNED")
                .build())
            .build();

        CisErrorMessage cisErrorMessageResponse = CisBidPackageResources.createBidPackage(bidPackageRequest, CisErrorMessage.class, HttpStatus.SC_CONFLICT, currentUser);

        softAssertions.assertThat(cisErrorMessageResponse.getMessage()).isEqualTo("BidPackage with name '"
            + createBidPackageResponse.getName() + "' already exists for Customer '"
            + createBidPackageResponse.getCustomerIdentity() + "'.");

        softAssertions.assertAll();
    }

    @AfterClass
    public static void testCleanup() {
        CisBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
    }
}
