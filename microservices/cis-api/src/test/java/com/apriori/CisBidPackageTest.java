package com.apriori;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import com.apriori.cis.controller.CisBidPackageResources;
import com.apriori.cis.models.request.bidpackage.BidPackageParameters;
import com.apriori.cis.models.request.bidpackage.BidPackageRequest;
import com.apriori.cis.models.response.bidpackage.BidPackageResponse;
import com.apriori.cis.models.response.bidpackage.BidPackagesResponse;
import com.apriori.cis.models.response.bidpackage.CisErrorMessage;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesApi.class)
public class CisBidPackageTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static UserCredentials currentUser;
    private static String bidPackageName;

    @BeforeEach
    public  void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        bidPackageResponse = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);
    }

    @Test
    @TestRail(id = {14126, 14130})
    @Description("Create, Delete and verify Bid Package is deleted")
    public void testCreateDeleteAndVerifyBidPackage() {
        String bpName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        BidPackageResponse createBidPackageResponse = CisBidPackageResources.createBidPackage(bpName, currentUser);
        softAssertions.assertThat(createBidPackageResponse.getName()).isEqualTo(bpName);

        CisBidPackageResources.deleteBidPackage(createBidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        CisErrorMessage cisErrorMessageResponse = CisBidPackageResources.deleteBidPackage(createBidPackageResponse.getIdentity(),
            CisErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);

        softAssertions.assertThat(cisErrorMessageResponse.getMessage()).contains("Can't find bidPackage with identity '" + createBidPackageResponse.getIdentity() + "'");

    }

    @Test
    @TestRail(id = {14131})
    @Description("Find List of bid packages and verify pagination for customer identity")
    public void testGetBidPackages() {
        BidPackagesResponse getBidPackagesResponse = CisBidPackageResources.getBidPackages(BidPackagesResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(getBidPackagesResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(getBidPackagesResponse.getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(getBidPackagesResponse.getItems().size()).isGreaterThan(0);
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
        String packageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        BidPackageResponse createBidPackageResponse = CisBidPackageResources.createBidPackage(packageName, currentUser);

        softAssertions.assertThat(createBidPackageResponse.getName()).isEqualTo(packageName);

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

        CisBidPackageResources.deleteBidPackage(createBidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @AfterEach
    public void testCleanup() {
        softAssertions.assertAll();
        CisBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
    }
}
