package com.apriori.qms.controller;

import com.apriori.qms.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectItem;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectItems;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectItemsRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectProfile;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageRequest;
import com.apriori.qms.entity.request.bidpackage.CommentReminder;
import com.apriori.qms.entity.request.bidpackage.EmailReminder;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectItemsBulkResponse;
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
import utils.QmsApiTestUtils;

import java.util.List;

/**
 * The type Qms bid package resources.
 */
@SuppressWarnings("unchecked")
public class QmsBidPackageResources {

    /**
     * Create bid package
     *
     * @param bidPackageName the bid package name
     * @param currentUser    the current user
     * @return bid package response
     */
    public static BidPackageResponse createBidPackage(String bidPackageName, UserCredentials currentUser) {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description(bidPackageName)
                .name(bidPackageName)
                .status("NEW")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, BidPackageResponse.class)
            .body(bidPackageRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<BidPackageResponse> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        return bidPackagesResponse.getResponseEntity();
    }

    /**
     * Create Bid Package
     *
     * @param <T>                      Response class type
     * @param bidPackageRequestBuilder BidPackageRequest Data builder
     * @param responseClass            Expected response class
     * @param httpStatus               Expected http status code
     * @param currentUser              UserCredentials class object
     * @return Expected response class object
     */
    public static <T> T createBidPackage(BidPackageRequest bidPackageRequestBuilder, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, responseClass)
            .body(bidPackageRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Get Bid Package
     *
     * @param <T>                response class type
     * @param bidPackageIdentity Bid package identity
     * @param responseClass      Expected response class
     * @param httpStatus         expected http status code
     * @param currentUser        UserCredentials
     * @return Expected response class object
     */
    public static <T> T getBidPackage(String bidPackageIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        return (T) HTTPRequest.build(
                RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, responseClass)
                    .inlineVariables(bidPackageIdentity)
                    .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                    .expectedResponseCode(httpStatus))
            .get().getResponseEntity();
    }

    /**
     * Get list of Bid packages
     *
     * @param <T>           expected response class type
     * @param responseClass expected response class
     * @param httpStatus    expected http status code
     * @param currentUser   UserCredentials
     * @return expected response class object
     */
    public static <T> T getBidPackages(Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        return (T) HTTPRequest.build(
                RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, responseClass)
                    .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                    .expectedResponseCode(httpStatus))
            .get().getResponseEntity();
    }

    /**
     * Update bid package
     *
     * @param <T>                      expected response class type
     * @param bidPackageIdentity       Bid Package Identity
     * @param bidPackageRequestBuilder BidPackageRequest request data builder
     * @param responseClass            expected response class
     * @param httpStatus               expected http status code
     * @param currentUser              UserCredentials
     * @return Response class object
     */
    public static <T> T updateBidPackage(String bidPackageIdentity, BidPackageRequest bidPackageRequestBuilder, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, responseClass)
            .inlineVariables(bidPackageIdentity)
            .body(bidPackageRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).patch().getResponseEntity();
    }

    /**
     * Delete Bid package
     *
     * @param <T>                response class type
     * @param bidPackageIdentity bid package identity
     * @param responseClass      expected class name
     * @param httpStatus         expected http status code
     * @param currentUser        UserCredentials class object
     * @return expected class
     */
    public static <T> T deleteBidPackage(String bidPackageIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, responseClass)
            .inlineVariables(bidPackageIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).delete().getResponseEntity();
    }

    /**
     * Create bid package project
     *
     * @param <T>                response class type
     * @param projectName        - Unique project name
     * @param bidPackageIdentity bid package identity
     * @param responseClass      expected response class
     * @param httpStatus         expected http status code
     * @param currentUser        UserCredentials
     * @return response class object
     */
    public static <T> T createBidPackageProject(String projectName, String bidPackageIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        BidPackageProjectRequest projectRequest = getBidPackageProjectRequestBuilder(projectName, projectName);
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECTS, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .inlineVariables(bidPackageIdentity)
            .body(projectRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Create bid package project t.
     *
     * @param <T>                             the type parameter
     * @param bidPackageProjectRequestBuilder the bid package project request builder
     * @param bidPackageIdentity              the bid package identity
     * @param responseClass                   the response class
     * @param httpStatus                      the http status
     * @param currentUser                     the current user
     * @return the t
     */
    public static <T> T createBidPackageProject(BidPackageProjectRequest bidPackageProjectRequestBuilder, String bidPackageIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECTS, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .inlineVariables(bidPackageIdentity)
            .body(bidPackageProjectRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Get All bid Package Projects
     *
     * @param bidPackageIdentity the bid package identity
     * @param currentUser        - UserCredentials
     * @return BidPackageProjectsResponse bid package projects
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
     * @param <T>                       the type parameter
     * @param bidPackageIdentity        the bid package identity
     * @param bidPackageProjectIdentity the bid package project identity
     * @param responseClass             the response class
     * @param httpStatus                the http status
     * @param currentUser               the current user
     * @return ResponseWrapper[String] t
     */
    public static <T> T deleteBidPackageProject(String bidPackageIdentity, String bidPackageProjectIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT, responseClass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).delete().getResponseEntity();
    }

    /**
     * get Bid Package Project
     *
     * @param <T>                       klass type
     * @param bidPackageIdentity        the bid package identity
     * @param bidPackageProjectIdentity the bid package project identity
     * @param klass                     response class name
     * @param httpStatus                -Integer
     * @param currentUser               - UserCredentials
     * @return klass object
     */
    public static <T> T getBidPackageProject(String bidPackageIdentity, String bidPackageProjectIdentity, Class<T> klass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Update Bid Package Project
     *
     * @param <T>                             - response of class type
     * @param bidPackageProjectRequestBuilder - BidPackageProjectRequest
     * @param bidPackageIdentity              the bid package identity
     * @param bidPackageProjectIdentity       the bid package project identity
     * @param currentUser                     - UserCredentials
     * @param klass                           - response of klass name
     * @param httpStatus                      - Integer
     * @return - response klass
     */
    public static <T> T updateBidPackageProject(BidPackageProjectRequest bidPackageProjectRequestBuilder, String bidPackageIdentity, String bidPackageProjectIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT, klass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .body(bidPackageProjectRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * BidPackageProjectRequestBuilder
     *
     * @param projectName        the project name
     * @param projectDescription the project description
     * @return BidPackageProjectRequest bid package project request builder
     */
    public static BidPackageProjectRequest getBidPackageProjectRequestBuilder(String projectName, String projectDescription) {
        return BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(projectName)
                .description(projectDescription)
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
     * @param bidPackageItemRequestBuilder the bid package item request builder
     * @param bidPackageIdentity           the bid package identity
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

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Update Bid Package Item
     *
     * @param <T>                          - response class type
     * @param bidPackageItemRequestBuilder the bid package item request builder
     * @param bidPackageIdentity           the bid package identity
     * @param bidPackageItemIdentity       the bid package item identity
     * @param klass                        - response class name
     * @param httpStatus                   - Integer
     * @param currentUser                  - UserCredentials
     * @return klass object
     */
    public static <T> T updateBidPackageItem(BidPackageItemRequest bidPackageItemRequestBuilder, String bidPackageIdentity, String bidPackageItemIdentity, Class<T> klass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .body(bidPackageItemRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).patch().getResponseEntity();
    }

    /**
     * Get Bid Package Item
     *
     * @param <T>                    - response class type
     * @param bidPackageIdentity     the bid package identity
     * @param bidPackageItemIdentity the bid package item identity
     * @param currentUser            - UserCredentials
     * @param klass                  - response class name
     * @param httpStatus             - Integer
     * @return klass object
     */
    public static <T> T getBidPackageItem(String bidPackageIdentity, String bidPackageItemIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get List of all bid package items
     *
     * @param <T>                - response class type
     * @param bidPackageIdentity the bid package identity
     * @param currentUser        - UserCredentials
     * @param klass              - response class name
     * @param httpStatus         - Integer
     * @return klass object
     */
    public static <T> T getBidPackageItems(String bidPackageIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_ITEMS, klass)
            .inlineVariables(bidPackageIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Delete bid package item
     *
     * @param bidPackageIdentity     the bid package identity
     * @param bidPackageItemIdentity the bid package item identity
     * @param currentUser            the current user
     */
    public static void deleteBidPackageItem(String bidPackageIdentity, String bidPackageItemIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_ITEM, null)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Create and get BidPackageItemRequestBuilder
     *
     * @param componentIdentity the component identity
     * @param scenarioIdentity  the scenario identity
     * @param iterationIdentity the iteration identity
     * @return BidPackageItemRequest bid package item request
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
     * @param bidPackageIdentity the bid package identity
     * @param projectIdentity    the project identity
     * @param currentUser        the current user
     * @return BidPackageProjectUserResponse bid package project user response
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

        ResponseWrapper<BidPackageProjectUserResponse> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete Bid Package Project User
     *
     * @param bidPackageIdentity  the bid package identity
     * @param projectIdentity     the project identity
     * @param projectUserIdentity the project user identity
     * @param currentUser         the current user
     */
    public static void deleteBidPackageProjectUser(String bidPackageIdentity, String projectIdentity, String projectUserIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_USER, null)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectUserIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    /**
     * get bid package project user
     *
     * @param <T>                 - response class type
     * @param bidPackageIdentity  the bid package identity
     * @param projectIdentity     the project identity
     * @param projectUserIdentity the project user identity
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

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * get all bid package project users
     *
     * @param <T>                - response class type
     * @param bidPackageIdentity the bid package identity
     * @param projectIdentity    the project identity
     * @param currentUser        - UserCredentials
     * @param klass              - response class name
     * @param httpStatus         the http status
     * @return klass object
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
     * @param bidPackageIdentity  - Bid package identity
     * @param projectIdentity     - project identity
     * @param projectUserIdentity - project user identity
     * @param currentUser         - UserCredentials
     * @param klass               - response class name
     * @param httpStatus          - Integer
     * @return response class object
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
     *
     * @param currentUser the current user
     * @return ParticipantsResponse participants
     */
    public static ParticipantsResponse getParticipants(UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PARTICIPANTS, ParticipantsResponse.class)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ParticipantsResponse> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets bid package project item.
     *
     * @param <T>                 the type parameter
     * @param bidPackageIdentity  the bid package identity
     * @param projectIdentity     the project identity
     * @param projectItemIdentity the project item identity
     * @param currentUser         the current user
     * @param klass               the klass
     * @param httpStatus          the http status
     * @return the bid package project item
     */
    public static <T> T getBidPackageProjectItem(String bidPackageIdentity, String projectIdentity, String projectItemIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_ITEM, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectItemIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets bid package project items.
     *
     * @param <T>                the type parameter
     * @param bidPackageIdentity the bid package identity
     * @param projectIdentity    the project identity
     * @param currentUser        the current user
     * @param klass              the klass
     * @param httpStatus         the http status
     * @return the bid package project items
     */
    public static <T> T getBidPackageProjectItems(String bidPackageIdentity, String projectIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_ITEMS, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();

    }


    /**
     * Create bid package bulk project items bid package project items post response.
     *
     * @param bidPackageIdentity the bid package identity
     * @param projectIdentity    the project identity
     * @param bidPackageItemList the bid package item list
     * @param currentUser        the current user
     * @return the bid package project items post response
     */
    public static BidPackageProjectItemsBulkResponse createBidPackageBulkProjectItems(String bidPackageIdentity, String projectIdentity, List<BidPackageProjectItem> bidPackageItemList, UserCredentials currentUser) {
        BidPackageProjectItemsRequest bidPackageProjectItemsRequestBuilder = BidPackageProjectItemsRequest.builder()
            .projectItems(BidPackageProjectItems.builder()
                .projectItem(bidPackageItemList)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_ITEMS, BidPackageProjectItemsBulkResponse.class)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .body(bidPackageProjectItemsRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackageProjectItemsBulkResponse> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }
}