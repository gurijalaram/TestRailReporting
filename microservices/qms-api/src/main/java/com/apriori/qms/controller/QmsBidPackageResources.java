package com.apriori.qms.controller;

import com.apriori.qms.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectProfile;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageRequest;
import com.apriori.qms.entity.request.bidpackage.CommentReminder;
import com.apriori.qms.entity.request.bidpackage.EmailReminder;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUserResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ParticipantsResponse;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;

import javax.xml.ws.Response;

public class QmsBidPackageResources {

    /**
     * Create bid package
     *
     * @return response object on BidPackageResponse class
     */
    public static BidPackageResponse createBidPackage(String bidPackageName, String userContext) {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description(bidPackageName)
                .name(bidPackageName)
                .status("NEW")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, BidPackageResponse.class)
            .body(bidPackageRequest)
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<BidPackageResponse> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        return bidPackagesResponse.getResponseEntity();
    }

    /**
     * delete bid package
     *
     * @param bidPackageIdentity
     * @param currentUser        - UserCredentials
     * @return Response object of string
     */
    public static ResponseWrapper<String> deleteBidPackage(String bidPackageIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, null)
            .inlineVariables(bidPackageIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * create Bid Package Project
     *
     * @param projectName
     * @param bidPackageIdentity
     * @param currentUser
     * @return BidPackageProjectResponse
     */
    public static BidPackageProjectResponse createBidPackageProject(String projectName, String bidPackageIdentity, UserCredentials currentUser) {
        BidPackageProjectRequest projectRequest = getBidPackageProjectRequestBuilder(projectName);
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECTS, BidPackageProjectResponse.class)
            .inlineVariables(bidPackageIdentity)
            .body(projectRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<BidPackageProjectResponse> response =  HTTPRequest.build(requestEntity).post();
        return response.getResponseEntity();
    }

    /**
     * Get All bid Package Projects
     *
     * @param bidPackageIdentity
     * @param currentUser        - UserCredentials
     * @return BidPackageProjectsResponse
     */
    public static BidPackageProjectsResponse getBidPackageProjects(String bidPackageIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECTS, BidPackageProjectsResponse.class)
            .inlineVariables(bidPackageIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackageProjectsResponse> bppResponse = HTTPRequest.build(requestEntity).get();
        return bppResponse.getResponseEntity();
    }

    /**
     * delete Bid Package Project
     *
     * @param bidPackageIdentity
     * @param bidPackageProjectIdentity
     * @param currentUser
     * @return ResponseWrapper<String>
     */
    public static ResponseWrapper<String> deleteBidPackageProject(String bidPackageIdentity, String bidPackageProjectIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT, null)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * get Bid Package Project
     *
     * @param <T>                       klass type
     * @param bidPackageIdentity
     * @param bidPackageProjectIdentity
     * @param currentUser               - UserCredentials
     * @param klass                     response class name
     * @param httpStatus                -Integer
     * @return klass object
     */
    public static <T> T getBidPackageProject(String bidPackageIdentity, String bidPackageProjectIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Update Bid Package Project
     *
     * @param <T>                             - response of class type
     * @param bidPackageProjectRequestBuilder - BidPackageProjectRequest
     * @param bidPackageIdentity
     * @param bidPackageProjectIdentity
     * @param currentUser                     - UserCredentials
     * @param klass                           - response of klass name
     * @param httpStatus                      - Integer
     * @return - response klass
     */
    public static <T> T updateBidPackageProject(BidPackageProjectRequest bidPackageProjectRequestBuilder, String bidPackageIdentity, String bidPackageProjectIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .body(bidPackageProjectRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper =  HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * BidPackageProjectRequestBuilder
     *
     * @param projectName
     * @return BidPackageProjectRequest
     */
    public static BidPackageProjectRequest getBidPackageProjectRequestBuilder(String projectName) {
        return BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(projectName)
                .description(projectName)
                .status("COMPLETED")
                .type("INTERNAL")
                .projectProfile(BidPackageProjectProfile.builder()
                    .emailReminder(EmailReminder.builder()
                        .active(true)
                        .startDuration("P1DT5M")
                        .frequencyValue("R2/P1D")
                        .build())
                    .commentReminder(CommentReminder.builder()
                        .active(true)
                        .startDuration("P1D")
                        .frequencyValue("R/P4H")
                        .build())
                    .build())
                .build())
            .build();
    }

    // BID PACKAGE ITEM RESOURCES

    /**
     * Create Bid Package Item
     *
     * @param <T>                          - response class type
     * @param bidPackageItemRequestBuilder
     * @param bidPackageIdentity
     * @param currentUser                  - UserCredentials
     * @param klass                        - response class name
     * @param httpStatus                   - Integer
     * @return klass object
     */
    public static <T> T createBidPackageItem(BidPackageItemRequest bidPackageItemRequestBuilder, String bidPackageIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_ITEMS, klass)
            .inlineVariables(bidPackageIdentity)
            .body(bidPackageItemRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Update Bid Package Item
     *
     * @param <T>                          - response class type
     * @param bidPackageItemRequestBuilder
     * @param bidPackageIdentity
     * @param bidPackageItemIdentity
     * @param currentUser                  - UserCredentials
     * @param klass                        - response class name
     * @param httpStatus                   - Integer
     * @return klass object
     */
    public static <T> T updateBidPackageItem(BidPackageItemRequest bidPackageItemRequestBuilder, String bidPackageIdentity, String bidPackageItemIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .body(bidPackageItemRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Get Bid Package Item
     *
     * @param <T>                    - response class type
     * @param bidPackageIdentity
     * @param bidPackageItemIdentity
     * @param currentUser            - UserCredentials
     * @param klass                  - response class name
     * @param httpStatus             - Integer
     * @return klass object
     */
    public static <T> T getBidPackageItem(String bidPackageIdentity, String bidPackageItemIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<T> responseWrapper =  HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Get List of all bid package items
     *
     * @param <T>                - response class type
     * @param bidPackageIdentity
     * @param currentUser        - UserCredentials
     * @param klass              - response class name
     * @param httpStatus         - Integer
     * @return klass object
     */
    public static <T> T getBidPackageItems(String bidPackageIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_ITEMS, klass)
            .inlineVariables(bidPackageIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete bid package item
     *
     * @param bidPackageIdentity
     * @param bidPackageItemIdentity
     * @param currentUser
     * @return ResponseWrapper of String
     */
    public static ResponseWrapper<String> deleteBidPackageItem(String bidPackageIdentity, String bidPackageItemIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_ITEM, null)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Create and get BidPackageItemRequestBuilder
     *
     * @param componentIdentity
     * @param scenarioIdentity
     * @param iterationIdentity
     * @return BidPackageItemRequest
     */
    public static BidPackageItemRequest bidPackageItemRequestBuilder(String componentIdentity, String scenarioIdentity, String iterationIdentity) {
        return BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(componentIdentity)
                .scenarioIdentity(scenarioIdentity)
                .iterationIdentity(iterationIdentity)
                .build())
            .build();
    }

    /**
     * Create bid package project user
     *
     * @param role               - DEFAULT or ADMIN
     * @param bidPackageIdentity
     * @param projectIdentity
     * @param currentUser
     * @return BidPackageProjectUserResponse
     */
    public static BidPackageProjectUserResponse createBidPackageProjectUser(String role, String bidPackageIdentity, String projectIdentity, UserCredentials currentUser) {
        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUser(BidPackageProjectUserParameters.builder()
                .userIdentity(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                .userEmail(currentUser.getEmail())
                .role(role)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_USERS, BidPackageProjectUserResponse.class)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .body(bidPackageProjectUserRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<BidPackageProjectUserResponse> responseWrapper =  HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete Bid Package Project User
     *
     * @param bidPackageIdentity
     * @param projectIdentity
     * @param projectUserIdentity
     * @param currentUser
     * @return ResponseWrapper<String>
     */
    public static ResponseWrapper<String> deleteBidPackageProjectUser(String bidPackageIdentity, String projectIdentity, String projectUserIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_USER, null)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectUserIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * get bid package project user
     *
     * @param <T>                 - response class type
     * @param bidPackageIdentity
     * @param projectIdentity
     * @param projectUserIdentity
     * @param currentUser         - UserCredentials
     * @param klass               - response class name
     * @param httpStatus          - Integer
     * @return klass object
     */
    public static <T> T getBidPackageProjectUser(String bidPackageIdentity, String projectIdentity, String projectUserIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_USER, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectUserIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper =  HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * get all bid package project users
     *
     * @param <T>                - response class type
     * @param bidPackageIdentity
     * @param projectIdentity
     * @param currentUser        - UserCredentials
     * @param klass              - response class name
     * @param httpStatus
     * @return  klass object
     */
    public static <T> T getBidPackageProjectUsers(String bidPackageIdentity, String projectIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_USERS, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();

    }

    /**
     * Update bid package project user
     *
     * @param <T>                 - response class type
     * @param role                - DEFAULT or ADMIN
     * @param bidPackageIdentity
     * @param projectIdentity
     * @param projectUserIdentity
     * @param currentUser         - UserCredentials
     * @param klass               - response class name
     * @param httpStatus          - Integer
     * @return klass object
     */
    public static <T> T updateBidPackageProjectUser(String role, String bidPackageIdentity, String projectIdentity, String projectUserIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUser(BidPackageProjectUserParameters.builder()
                .userEmail(currentUser.getEmail())
                .role(role)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_USER, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectUserIdentity)
            .body(bidPackageProjectUserRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();

    }

    /**
     * get Participants
     * @param currentUser
     * @return ParticipantsResponse
     */
    public static ParticipantsResponse getParticipants(UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PARTICIPANTS, ParticipantsResponse.class)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ParticipantsResponse> responseWrapper =  HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();

    }
}