package com.apriori.qds.controller;

import com.apriori.qds.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qds.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qds.entity.request.bidpackage.BidPackageParameters;
import com.apriori.qds.entity.request.bidpackage.BidPackageProjectItemParameters;
import com.apriori.qds.entity.request.bidpackage.BidPackageProjectItemRequest;
import com.apriori.qds.entity.request.bidpackage.BidPackageProjectParameters;
import com.apriori.qds.entity.request.bidpackage.BidPackageProjectProfile;
import com.apriori.qds.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qds.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qds.entity.request.bidpackage.BidPackageProjectUserRequest;
import com.apriori.qds.entity.request.bidpackage.BidPackageRequest;
import com.apriori.qds.entity.request.bidpackage.CommentReminder;
import com.apriori.qds.entity.request.bidpackage.EmailReminder;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectUserResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qds.enums.QDSAPIEnum;
import com.apriori.qds.utils.QdsApiTestUtils;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;

public class BidPackageResources {

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

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, BidPackageResponse.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return (BidPackageResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * delete bid package
     *
     * @param bidPackageIdentity bid package identity
     * @param currentUser        - UserCredentials
     * @return Response object of string
     */
    public static ResponseWrapper<String> deleteBidPackage(String bidPackageIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, null)
            .inlineVariables(bidPackageIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
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
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECTS, BidPackageProjectResponse.class)
            .inlineVariables(bidPackageIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .body(projectRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return (BidPackageProjectResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }


    /**
     * Create bid package project
     *
     * @param bidPackageProjectRequestBuilder - BidPackageProjectRequest data builder
     * @param bidPackageIdentity
     * @param responseClass                   - response of klass name
     * @param httpStatus                      - http status code
     * @param <T>                             - response of class type
     * @return - ResponseWrapper of response klass
     */
    public static <T> ResponseWrapper<T> createBidPackageProject(BidPackageProjectRequest bidPackageProjectRequestBuilder, String bidPackageIdentity, UserCredentials currentUser, Class<T> responseClass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECTS, responseClass)
            .inlineVariables(bidPackageIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .body(bidPackageProjectRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * delete Bid Package Project
     *
     * @param bidPackageIdentity
     * @param bidPackageProjectIdentity
     * @param currentUser
     * @return ResponseWrapper[String]
     */
    public static ResponseWrapper<String> deleteBidPackageProject(String bidPackageIdentity, String bidPackageProjectIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT, null)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Get All bid Package Projects
     *
     * @param bidPackageIdentity
     * @param currentUser        - UserCredentials
     * @return BidPackageProjectsResponse
     */
    public static BidPackageProjectsResponse getBidPackageProjects(String bidPackageIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECTS, BidPackageProjectsResponse.class)
            .inlineVariables(bidPackageIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        return (BidPackageProjectsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * get Bid Package Project
     *
     * @param bidPackageIdentity
     * @param bidPackageProjectIdentity
     * @param currentUser               - UserCredentials
     * @param klass                     response class name
     * @param <T>                       klass type
     * @return klass object
     */
    public static <T> T getBidPackageProject(String bidPackageIdentity, String bidPackageProjectIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * @param bidPackageProjectRequestBuilder
     * @param bidPackageIdentity
     * @param bidPackageProjectIdentity
     * @param currentUser                     - UserCredentials
     * @param klass                           - response of klass name
     * @param httpStatus                      - http status code
     * @param <T>                             - response of class type
     * @return - ResponseWrapper of response klass
     */
    public static <T> T updateBidPackageProject(BidPackageProjectRequest bidPackageProjectRequestBuilder, String bidPackageIdentity, String bidPackageProjectIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .body(bidPackageProjectRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).patch().getResponseEntity();
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

    // BID PACKAGE PROJECT ITEM RESOURCES

    /**
     * Create Bid package project item
     *
     * @param bidPackageIdentity
     * @param projectIdentity
     * @param projectItemIdentity
     * @param currentUser         - UserCredentials
     * @param klass               - response class name
     * @param httpStatus          - Integer
     * @param <T>                 - response class type
     * @return klass object
     */
    public static <T> T createBidPackageProjectItem(String bidPackageIdentity, String projectIdentity, String projectItemIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        BidPackageProjectItemRequest bidPackageProjectItemRequestBuilder = BidPackageProjectItemRequest.builder()
            .projectItem(BidPackageProjectItemParameters.builder()
                .bidPackageItemIdentity(projectItemIdentity)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_ITEMS, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .body(bidPackageProjectItemRequestBuilder)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * get bid package project item
     *
     * @param bidPackageIdentity
     * @param projectIdentity
     * @param projectItemIdentity
     * @param currentUser         - UserCredentials
     * @param klass               - response class name
     * @param httpStatus          - Integer
     * @param <T>                 - response class type
     * @return klass object
     */
    public static <T> T getBidPackageProjectItem(String bidPackageIdentity, String projectIdentity, String projectItemIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_ITEM, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectItemIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get all bid package project items
     *
     * @param bidPackageIdentity
     * @param projectIdentity
     * @param currentUser        - UserCredentials
     * @param klass              - response class name
     * @param httpStatus         - Integer
     * @param <T>                - response class type
     * @return klass object
     */
    public static <T> T getBidPackageProjectItems(String bidPackageIdentity, String projectIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_ITEMS, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Delete Bid Package Project Item
     *
     * @param bidPackageIdentity
     * @param projectIdentity
     * @param projectItemIdentity
     * @param currentUser
     * @return ResponseWrapper of string object
     */
    public static ResponseWrapper<String> deleteBidPackageProjectItem(String bidPackageIdentity, String projectIdentity, String projectItemIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_ITEM, null)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectItemIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    // BID PACKAGE ITEM RESOURCES

    /**
     * Create Bid Package Item
     *
     * @param bidPackageItemRequestBuilder
     * @param bidPackageIdentity
     * @param currentUser                  - UserCredentials
     * @param klass                        - response class name
     * @param httpStatus                   - Integer
     * @param <T>                          - response class type
     * @return ResponseWrapper of klass object
     */
    public static <T> T createBidPackageItem(BidPackageItemRequest bidPackageItemRequestBuilder, String bidPackageIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_ITEMS, klass)
            .inlineVariables(bidPackageIdentity)
            .body(bidPackageItemRequestBuilder)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Update Bid Package Item
     *
     * @param bidPackageItemRequestBuilder
     * @param bidPackageIdentity
     * @param bidPackageItemIdentity
     * @param currentUser                  - UserCredentials
     * @param klass                        - response class name
     * @param httpStatus                   - Integer
     * @param <T>                          - response class type
     * @return  klass object
     */
    public static <T> T updateBidPackageItem(BidPackageItemRequest bidPackageItemRequestBuilder, String bidPackageIdentity, String bidPackageItemIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .body(bidPackageItemRequestBuilder)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).patch().getResponseEntity();
    }

    /**
     * Get Bid Package Item
     *
     * @param bidPackageIdentity
     * @param bidPackageItemIdentity
     * @param currentUser            - UserCredentials
     * @param klass                  - response class name
     * @param httpStatus             - Integer
     * @param <T>                    - response class type
     * @return klass object
     */
    public static <T> T getBidPackageItem(String bidPackageIdentity, String bidPackageItemIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get List of all bid package items
     *
     * @param bidPackageIdentity
     * @param currentUser        - UserCredentials
     * @param klass              - response class name
     * @param httpStatus         - Integer
     * @param <T>                - response class type
     * @return klass object
     */
    public static <T> T getBidPackageItems(String bidPackageIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_ITEMS, klass)
            .inlineVariables(bidPackageIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
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
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_ITEM, null)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
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
                .userEmail(currentUser.getEmail())
                .role(role)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_USERS, BidPackageProjectUserResponse.class)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .body(bidPackageProjectUserRequestBuilder)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return (BidPackageProjectUserResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Delete Bid Package Project User
     *
     * @param bidPackageIdentity
     * @param projectIdentity
     * @param projectUserIdentity
     * @param currentUser
     * @return ResponseWrapper[String]
     */
    public static ResponseWrapper<String> deleteBidPackageProjectUser(String bidPackageIdentity, String projectIdentity, String projectUserIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_USER, null)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectUserIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * get bid package project user
     *
     * @param bidPackageIdentity
     * @param projectIdentity
     * @param projectUserIdentity
     * @param currentUser         - UserCredentials
     * @param klass               - response class name
     * @param httpStatus          - Integer
     * @param <T>                 - response class type
     * @return klass object
     */
    public static <T> T getBidPackageProjectUser(String bidPackageIdentity, String projectIdentity, String projectUserIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_USER, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectUserIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();

    }

    /**
     * get all bid package project users
     *
     * @param bidPackageIdentity
     * @param projectIdentity
     * @param currentUser        - UserCredentials
     * @param klass              - response class name
     * @param httpStatus         - Integer
     * @param <T>                - response class type
     * @return klass object
     */
    public static <T> T getBidPackageProjectUsers(String bidPackageIdentity, String projectIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_USERS, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();

    }

    /**
     * Update bid package project user
     *
     * @param role                - DEFAULT or ADMIN
     * @param bidPackageIdentity
     * @param projectIdentity
     * @param projectUserIdentity
     * @param currentUser         - UserCredentials
     * @param klass               - response class name
     * @param httpStatus          - Integer
     * @param <T>                 - response class type
     * @return klass object
     */
    public static <T> T updateBidPackageProjectUser(String role, String bidPackageIdentity, String projectIdentity, String projectUserIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUser(BidPackageProjectUserParameters.builder()
                .userEmail(currentUser.getEmail())
                .role(role)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_USER, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectUserIdentity)
            .body(bidPackageProjectUserRequestBuilder)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).patch().getResponseEntity();
    }
}
