package com.apriori.qds.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.qds.controller.BidPackageResources;
import com.apriori.qds.entity.request.bidpackage.BidPackageParameters;
import com.apriori.qds.entity.request.bidpackage.BidPackageRequest;
import com.apriori.qds.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackagesResponse;
import com.apriori.qds.enums.QDSAPIEnum;
import com.apriori.qds.utils.QdsApiTestUtils;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BidPackageTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static ResponseWrapper<BidPackageResponse> bidPackageResponse;
    UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;
    private static String userContext;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        bidPackageResponse = BidPackageResources.createBidPackage(bidPackageName, userContext);
    }

    @Test
    @TestRail(testCaseId = {"13312"})
    @Description("Create Bid Package")
    public void createBidPackage() {
        softAssertions.assertThat(bidPackageResponse.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        softAssertions.assertThat(bidPackageResponse.getResponseEntity().getName()).isEqualTo(bidPackageName);
    }

    @Test
    @TestRail(testCaseId = {"13693"})
    @Description("Create Bid Package with existing name")
    public void createBidPackageWithExistingName() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description(bidPackageName)
                .name(bidPackageName)
                .status("NEW")
                .assignedTo(userIdentity)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, ErrorMessage.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<ErrorMessage> bidPackageErrorResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackageErrorResponse.getStatusCode()).isEqualTo(HttpStatus.SC_CONFLICT);
        softAssertions.assertThat(bidPackageErrorResponse.getResponseEntity().getMessage()).contains("already exists for Customer");
    }

    @Test
    @TestRail(testCaseId = {"13313"})
    @Description("Create Bid Package greater than 64 characters")
    public void createBidPackageNameMoreThan64() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("descripton")
                .name(RandomStringUtils.randomAlphabetic(70))
                .status("NEW")
                .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, ErrorMessage.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<ErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackagesResponse.getStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("should not be more than 64 characters");
    }

    @Test
    @TestRail(testCaseId = {"13710", "13331", "13332"})
    @Description("Create Bid Package is equal to 64 characters, delete bid package and verify bid package is deleted")
    public void createBidPackageNameEqualTo64() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("description")
                .name(RandomStringUtils.randomAlphabetic(64))
                .status("NEW")
                .assignedTo(userIdentity)
                .build())
            .build();

        ResponseWrapper<BidPackageResponse> bidPackageCreatedResponse = HTTPRequest.build(
                RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, BidPackageResponse.class)
                    .body(bidPackageRequest)
                    .headers(QdsApiTestUtils.setUpHeader())
                    .apUserContext(userContext))
            .post();

        softAssertions.assertThat(bidPackageCreatedResponse.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        softAssertions.assertThat(bidPackageCreatedResponse.getResponseEntity().getAssignedTo()).isEqualTo(userIdentity);

        ResponseWrapper<String> deleteBidResponse = BidPackageResources.deleteBidPackage(bidPackageCreatedResponse.getResponseEntity().getIdentity(), userContext);
        softAssertions.assertThat(deleteBidResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);

        ResponseWrapper<ErrorMessage> bidPackageResponse = HTTPRequest.build(
                RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, ErrorMessage.class)
                    .inlineVariables(bidPackageCreatedResponse.getResponseEntity().getIdentity())
                    .headers(QdsApiTestUtils.setUpHeader())
                    .apUserContext(userContext))
            .get();

        softAssertions.assertThat(bidPackageResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
        softAssertions.assertThat(bidPackageResponse.getResponseEntity().getMessage()).contains("Can't find bidPackage with identity '" + bidPackageCreatedResponse.getResponseEntity().getIdentity() + "'");
    }

    @Test
    @TestRail(testCaseId = {"13636"})
    @Description("Create Bid Package description is equal to 254 characters")
    public void createBidPackageNameEqualTo254() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description(RandomStringUtils.randomAlphabetic(254))
                .name(RandomStringUtils.randomAlphabetic(15))
                .status("NEW")
                .assignedTo(userIdentity)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, BidPackageResponse.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<BidPackageResponse> bidPackageAssignedResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackageAssignedResponse.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        softAssertions.assertThat(bidPackageAssignedResponse.getResponseEntity().getAssignedTo()).isEqualTo(userIdentity);

        ResponseWrapper<String> deleteBidResponse = BidPackageResources.deleteBidPackage(bidPackageAssignedResponse.getResponseEntity().getIdentity(), userContext);
        softAssertions.assertThat(deleteBidResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @TestRail(testCaseId = {"13325"})
    @Description("Create Bid Package description is greater 254 characters")
    public void createBidPackageNameGreaterThan254() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description(RandomStringUtils.randomAlphabetic(260))
                .name(bidPackageName)
                .status("NEW")
                .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, ErrorMessage.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<ErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackagesResponse.getStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("should not be more than 254 characters");
    }

    @Test
    @TestRail(testCaseId = {"13350"})
    @Description("Create Bid Package with empty name")
    public void createBidPackageWithEmptyName() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("TEST")
                .name("")
                .status("NEW")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, ErrorMessage.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<ErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackagesResponse.getStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("'name' should not be null");
    }

    @Test
    @TestRail(testCaseId = {"13349"})
    @Description("Create Bid Package with empty Description")
    public void createBidPackageWithEmptyDescription() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("")
                .name(RandomStringUtils.randomAlphabetic(10))
                .status("NEW")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, ErrorMessage.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<ErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackagesResponse.getStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("'description' should not be null");
    }

    @Test
    @TestRail(testCaseId = {"13823"})
    @Description("Create Bid Package with empty Description")
    public void createBidPackageWithEmptyStatus() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description(RandomStringUtils.randomAlphabetic(15))
                .name(RandomStringUtils.randomAlphabetic(10))
                .status("")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, ErrorMessage.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<ErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackagesResponse.getStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("'status' should not be null");
    }

    @Test
    @TestRail(testCaseId = {"13326"})
    @Description("Updated existing Bid Package status")
    public void updateBidPackageStatus() {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, BidPackageResponse.class)
            .inlineVariables(bidPackageResponse.getResponseEntity().getIdentity())
            .body(BidPackageRequest.builder()
                .bidPackage(BidPackageParameters.builder()
                    .name(bidPackageResponse.getResponseEntity().getName())
                    .description(bidPackageResponse.getResponseEntity().getDescription())
                    .status("ASSIGNED")
                    .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                    .build())
                .build())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<BidPackageResponse> bidPackageUpdateResponse = HTTPRequest.build(requestEntity).patch();

        softAssertions.assertThat(bidPackageUpdateResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(bidPackageUpdateResponse.getResponseEntity().getStatus()).isEqualTo("ASSIGNED");
    }

    @Test
    @TestRail(testCaseId = {"13872"})
    @Description("Updated existing Bid Package description")
    public void updateBidPackageDescription() {
        String bpDesc = RandomStringUtils.randomAlphabetic(15);
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, BidPackageResponse.class)
            .inlineVariables(bidPackageResponse.getResponseEntity().getIdentity())
            .body(BidPackageRequest.builder()
                .bidPackage(BidPackageParameters.builder()
                    .name(bidPackageResponse.getResponseEntity().getName())
                    .description(bpDesc)
                    .status(bidPackageResponse.getResponseEntity().getStatus())
                    .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                    .build())
                .build())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<BidPackageResponse> bidPackageUpdateResponse = HTTPRequest.build(requestEntity).patch();

        softAssertions.assertThat(bidPackageUpdateResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(bidPackageUpdateResponse.getResponseEntity().getDescription()).isEqualTo(bpDesc);
    }

    @Test
    @TestRail(testCaseId = {"13693"})
    @Description("Updated existing Bid Package name")
    public void updateBidPackageAssigned() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, BidPackageResponse.class)
            .inlineVariables(bidPackageResponse.getResponseEntity().getIdentity())
            .body(BidPackageRequest.builder()
                .bidPackage(BidPackageParameters.builder()
                    .name(RandomStringUtils.randomAlphabetic(10))
                    .description(bidPackageResponse.getResponseEntity().getDescription())
                    .status(bidPackageResponse.getResponseEntity().getStatus())
                    .assignedTo(userIdentity)
                    .build())
                .build())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<BidPackageResponse> bidPackageUpdateResponse = HTTPRequest.build(requestEntity).patch();

        softAssertions.assertThat(bidPackageUpdateResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(bidPackageUpdateResponse.getResponseEntity().getAssignedTo()).isEqualTo(userIdentity);
    }

    @Test
    @TestRail(testCaseId = {"13327", "13873", "13409"})
    @Description("Find List of bid packages, pagination from the user for same customer")
    public void getBidPackages() {
        String otherUserContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, BidPackagesResponse.class)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(otherUserContext);

        ResponseWrapper<BidPackagesResponse> bidPackagesResponse = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(bidPackagesResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getIsFirstPage()).isTrue();
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"13408"})
    @Description("Find bid package from other customer identity")
    public void getBidPackagesFromAnotherCustomer() {
        String otherUserContext = new AuthUserContextUtil().getAuthUserContext("testUser1@widgets.aprioritest.com");
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, BidPackagesResponse.class)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(otherUserContext);

        ResponseWrapper<BidPackagesResponse> bidPackagesResponse = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(bidPackagesResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getIsFirstPage()).isTrue();
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(testCaseId = {"13329"})
    @Description("Get bid package")
    public void getBidPackage() {
        String otherUserContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, BidPackageResponse.class)
            .inlineVariables(bidPackageResponse.getResponseEntity().getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(otherUserContext);

        ResponseWrapper<BidPackageResponse> bidPackageResponse = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(bidPackageResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(bidPackageResponse.getResponseEntity().getName()).isEqualTo(bidPackageName);
    }

    @Test
    @TestRail(testCaseId = {"13333"})
    @Description("Get bid package with invalid identity")
    public void getBidPackageWithInvalidIdentity() {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, ErrorMessage.class)
            .inlineVariables("INVALID IDENTITY")
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<ErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(bidPackagesResponse.getStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("'identity' is not a valid identity");
    }

    @After
    public void testCleanup() {
        ResponseWrapper<String> deleteBidResponse = BidPackageResources.deleteBidPackage(bidPackageResponse.getResponseEntity().getIdentity(), userContext);
        softAssertions.assertThat(deleteBidResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        softAssertions.assertAll();
    }
}
