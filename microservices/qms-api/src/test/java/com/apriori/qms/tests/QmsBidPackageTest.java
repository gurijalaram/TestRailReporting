package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageRequest;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackagesResponse;
import com.apriori.qms.entity.response.bidpackage.QmsErrorMessage;
import com.apriori.qms.enums.QMSAPIEnum;
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

public class QmsBidPackageTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;
    private static String userContext;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, userContext);
    }

    @Test
    @TestRail(testCaseId = {"13361"})
    @Description("Create Bid Package")
    public void createBidPackage() {
        softAssertions.assertThat(bidPackageResponse.getName()).isEqualTo(bidPackageName);
    }

    @Test
    @TestRail(testCaseId = {"13362"})
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

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, QmsErrorMessage.class)
            .body(bidPackageRequest)
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<QmsErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("should not be more than 64 characters");
    }

    @Test
    @TestRail(testCaseId = {"13806", "13372", "13373"})
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
                RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, BidPackageResponse.class)
                    .body(bidPackageRequest)
                    .apUserContext(userContext)
                    .expectedResponseCode(HttpStatus.SC_CREATED))
            .post();

        softAssertions.assertThat(bidPackageCreatedResponse.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        softAssertions.assertThat(bidPackageCreatedResponse.getResponseEntity().getAssignedTo()).isEqualTo(userIdentity);

        ResponseWrapper<String> deleteBidResponse = QmsBidPackageResources.deleteBidPackage(bidPackageCreatedResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertThat(deleteBidResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);

        ResponseWrapper<QmsErrorMessage> bidPackageResponse = HTTPRequest.build(
                RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, QmsErrorMessage.class)
                    .inlineVariables(bidPackageCreatedResponse.getResponseEntity().getIdentity())
                    .apUserContext(userContext)
                    .expectedResponseCode(HttpStatus.SC_NOT_FOUND))
            .get();

        softAssertions.assertThat(bidPackageResponse.getResponseEntity().getMessage()).contains("Can't find bidPackage with identity '" + bidPackageCreatedResponse.getResponseEntity().getIdentity() + "'");
    }

    @Test
    @TestRail(testCaseId = {"13807"})
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

        RequestEntity requestEntity = RequestEntityUtil.init(com.apriori.qms.enums.QMSAPIEnum.BID_PACKAGES, BidPackageResponse.class)
            .body(bidPackageRequest)
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<BidPackageResponse> bidPackageAssignedResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackageAssignedResponse.getResponseEntity().getAssignedTo()).isEqualTo(userIdentity);
        QmsBidPackageResources.deleteBidPackage(bidPackageAssignedResponse.getResponseEntity().getIdentity(), currentUser);

    }

    @Test
    @TestRail(testCaseId = {"13363"})
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

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, QmsErrorMessage.class)
            .body(bidPackageRequest)
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<QmsErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("should not be more than 254 characters");
    }

    @Test
    @TestRail(testCaseId = {"13364"})
    @Description("Create Bid Package with empty name")
    public void createBidPackageWithEmptyName() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("TEST")
                .name("")
                .status("NEW")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, QmsErrorMessage.class)
            .body(bidPackageRequest)
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<QmsErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("'name' should not be null");
    }

    @Test
    @TestRail(testCaseId = {"13364"})
    @Description("Create Bid Package with empty Description")
    public void createBidPackageWithEmptyDescription() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("")
                .name(RandomStringUtils.randomAlphabetic(10))
                .status("NEW")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, QmsErrorMessage.class)
            .body(bidPackageRequest)
            .apUserContext(userContext);

        ResponseWrapper<QmsErrorMessage> errorResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(errorResponse.getResponseEntity().getMessage()).contains("'description' should not be null");
    }

    @Test
    @TestRail(testCaseId = {"13886"})
    @Description("Create Bid Package with empty Status")
    public void createBidPackageWithEmptyStatus() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description(RandomStringUtils.randomAlphabetic(15))
                .name(RandomStringUtils.randomAlphabetic(10))
                .status("")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, QmsErrorMessage.class)
            .body(bidPackageRequest)
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<QmsErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("'status' should not be null");
    }

    @Test
    @TestRail(testCaseId = {"13366"})
    @Description("Updated existing Bid Package status")
    public void updateBidPackageStatus() {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, BidPackageResponse.class)
            .inlineVariables(bidPackageResponse.getIdentity())
            .body(BidPackageRequest.builder()
                .bidPackage(BidPackageParameters.builder()
                    .name(bidPackageResponse.getName())
                    .description(bidPackageResponse.getDescription())
                    .status("ASSIGNED")
                    .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                    .build())
                .build())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackageResponse> bidPackageUpdateResponse = HTTPRequest.build(requestEntity).patch();

        softAssertions.assertThat(bidPackageUpdateResponse.getResponseEntity().getStatus()).isEqualTo("ASSIGNED");
    }

    @Test
    @TestRail(testCaseId = {"13887"})
    @Description("Updated existing Bid Package description")
    public void updateBidPackageDescription() {
        String bpDesc = RandomStringUtils.randomAlphabetic(15);
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, BidPackageResponse.class)
            .inlineVariables(bidPackageResponse.getIdentity())
            .body(BidPackageRequest.builder()
                .bidPackage(BidPackageParameters.builder()
                    .name(bidPackageResponse.getName())
                    .description(bpDesc)
                    .status(bidPackageResponse.getStatus())
                    .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                    .build())
                .build())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackageResponse> bidPackageUpdateResponse = HTTPRequest.build(requestEntity).patch();

        softAssertions.assertThat(bidPackageUpdateResponse.getResponseEntity().getDescription()).isEqualTo(bpDesc);
    }

    @Test
    @TestRail(testCaseId = {"13888"})
    @Description("Updated existing Bid Package name")
    public void updateBidPackageAssigned() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, BidPackageResponse.class)
            .inlineVariables(bidPackageResponse.getIdentity())
            .body(BidPackageRequest.builder()
                .bidPackage(BidPackageParameters.builder()
                    .name(RandomStringUtils.randomAlphabetic(10))
                    .description(bidPackageResponse.getDescription())
                    .status(bidPackageResponse.getStatus())
                    .assignedTo(userIdentity)
                    .build())
                .build())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackageResponse> bidPackageUpdateResponse = HTTPRequest.build(requestEntity).patch();

        softAssertions.assertThat(bidPackageUpdateResponse.getResponseEntity().getAssignedTo()).isEqualTo(userIdentity);
    }

    @Test
    @TestRail(testCaseId = {"13367", "13874", "13368"})
    @Description("Find List of bid packages, pagination from the user for same customer")
    public void getBidPackages() {
        String otherUserContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, BidPackagesResponse.class)
            .apUserContext(otherUserContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackagesResponse> bidPackagesResponse = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getIsFirstPage()).isTrue();
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"13893"})
    @Description("Find bid package from other customer identity")
    public void getBidPackagesFromAnotherCustomer() {
        String otherUserContext = new AuthUserContextUtil().getAuthUserContext("testUser1@widgets.aprioritest.com");
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, BidPackagesResponse.class)
            .apUserContext(otherUserContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackagesResponse> bidPackagesResponse = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getIsFirstPage()).isTrue();
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"13369"})
    @Description("Get bid package")
    public void getBidPackage() {
        String otherUserContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, BidPackageResponse.class)
            .inlineVariables(bidPackageResponse.getIdentity())
            .apUserContext(otherUserContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackageResponse> bidPackageResponse = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(bidPackageResponse.getResponseEntity().getName()).isEqualTo(bidPackageName);
    }

    @Test
    @TestRail(testCaseId = {"13370"})
    @Description("Get bid package with invalid identity")
    public void getBidPackageWithInvalidIdentity() {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, QmsErrorMessage.class)
            .inlineVariables("INVALID IDENTITY")
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<QmsErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("'bidPackageIdentity' is not a valid identity");
    }

    @After
    public void testCleanup() {
        ResponseWrapper<String> deleteBidResponse = QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), currentUser);
        softAssertions.assertThat(deleteBidResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        softAssertions.assertAll();
    }
}
