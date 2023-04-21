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

import java.time.LocalDateTime;
import java.util.List;

public class QmsBidPackageResources {

    /**
     * Create bid package
     *
     * @param bidPackageName
     * @param currentUser
     * @return
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
     * @param bidPackageRequestBuilder BidPackageRequest Data builder
     * @param responseClass            Expected response class
     * @param httpStatus               Expected http status code
     * @param currentUser              UserCredentials class object
     * @param <T>                      Response class type
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
     * @param bidPackageIdentity Bid package identity
     * @param responseClass      Expected response class
     * @param httpStatus         expected http status code
     * @param currentUser        UserCredentials
     * @param <T>                response class type
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
     * @param responseClass expected response class
     * @param httpStatus    expected http status code
     * @param currentUser   UserCredentials
     * @param <T>           expected response class type
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
     * @param bidPackageIdentity       Bid Package Identity
     * @param bidPackageRequestBuilder BidPackageRequest request data builder
     * @param responseClass            expected response class
     * @param httpStatus               expected http status code
     * @param currentUser              UserCredentials
     * @param <T>                      expected response class type
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
     * @param bidPackageIdentity bid package identity
     * @param responseClass      expected class name
     * @param httpStatus         expected http status code
     * @param currentUser        UserCredentials class object
     * @param <T>                response class tupe
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
     * @param projectName        - Unique project name
     * @param bidPackageIdentity bid package identity
     * @param responseClass      expected response class
     * @param httpStatus         expected http status code
     * @param currentUser        UserCredentials
     * @param <T>                response class type
     * @return response class object
     */
    public static <T> T createBidPackageProject(String projectName, String bidPackageIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        BidPackageProjectRequest projectRequest = getBidPackageProjectRequestBuilder(projectName, projectName);
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECTS, responseClass)
            .inlineVariables(bidPackageIdentity)
            .body(projectRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    public static <T> T createBidPackageProject(BidPackageProjectRequest bidPackageProjectRequestBuilder, String bidPackageIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECTS, responseClass)
            .inlineVariables(bidPackageIdentity)
            .body(bidPackageProjectRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
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
     * @param responseClass
     * @param httpStatus
     * @param currentUser
     * @return ResponseWrapper[String]
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
     * @param bidPackageIdentity
     * @param bidPackageProjectIdentity
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

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * BidPackageProjectRequestBuilder
     *
     * @param projectName
     * @param projectDescription
     * @return BidPackageProjectRequest
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

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Update Bid Package Item
     *
     * @param <T>                          - response class type
     * @param bidPackageItemRequestBuilder
     * @param bidPackageIdentity
     * @param bidPackageItemIdentity
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
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
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

        ResponseWrapper<BidPackageProjectUserResponse> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
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

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
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
     * @param currentUser
     * @return ParticipantsResponse
     */
    public static ParticipantsResponse getParticipants(UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PARTICIPANTS, ParticipantsResponse.class)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ParticipantsResponse> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * ProjectRequestBuilder
     *
     * @param projectName        the project name
     * @param projectDescription the project description
     * @param itemsList          the items list
     * @param usersList          the users list
     * @return BidPackageProjectRequest project request builder
     */
    public static BidPackageProjectRequest getProjectRequestBuilder(String projectName, String projectDescription, List<BidPackageItemRequest> itemsList, List<BidPackageProjectUserParameters> usersList) {
        return BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(projectName)
                .displayName(projectName)
                .dueAt(LocalDateTime.now().plusDays(10))
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
                .items(itemsList)
                .users(usersList)
                .build())
            .build();
    }

    /**
     * Get list of all projects
     *
     * @param responseClass expected response class
     * @param httpStatus    expected http status code
     * @param currentUser   UserCredentials
     * @param <T>           response class type
     * @return Response class object
     */
    public static <T> T getProjects(Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECTS, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Find  a project by identity in which user participates
     *
     * @param projectIdentity Project identity
     * @param responseClass   expected response class
     * @param httpStatus      expected http status code
     * @param currentUser     UserCredentials
     * @param <T>             response class type
     * @return Response class object
     */
    public static <T> T getProject(String projectIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECT, responseClass)
            .inlineVariables(projectIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Create project
     *
     * @param <T>                Response class type
     * @param projectName        Unique project name
     * @param projectDescription Unique project description
     * @param itemsList          the items list
     * @param usersList          the users list
     * @param responseClass      Expected response class
     * @param httpStatus         Expected http status code
     * @param currentUser        UserCredentials
     * @return response class object
     */
    public static <T> T createProject(String projectName, String projectDescription, List<BidPackageItemRequest> itemsList, List<BidPackageProjectUserParameters> usersList, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        BidPackageProjectRequest projectRequest = getProjectRequestBuilder(projectName, projectDescription, itemsList, usersList);
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECTS, responseClass)
            .body(projectRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }
}