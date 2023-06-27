package com.apriori.qms.controller;

import com.apriori.qms.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectItem;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectItems;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectItemsRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageRequest;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUsersDeleteResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUsersPostResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ParticipantsResponse;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.utils.DateFormattingUtils;
import com.apriori.utils.DateUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;
import utils.QmsApiTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * The type Qms bid package resources.
 */
public class QmsBidPackageResources {

    /**
     * Create bid package response.
     *
     * @param bidPackageName the bid package name
     * @param currentUser    the current user
     * @return the bid package response
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
     * Create bid package
     *
     * @param <T>                      the type parameter
     * @param bidPackageRequestBuilder the bid package request builder
     * @param responseClass            the response class
     * @param httpStatus               the http status
     * @param currentUser              the current user
     * @return the response entity
     */
    public static <T> T createBidPackage(BidPackageRequest bidPackageRequestBuilder, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, responseClass)
            .body(bidPackageRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets bid package.
     *
     * @param <T>                the type parameter
     * @param bidPackageIdentity the bid package identity
     * @param responseClass      the response class
     * @param httpStatus         the http status
     * @param currentUser        the current user
     * @return the bid package
     */
    public static <T> T getBidPackage(String bidPackageIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        ResponseWrapper<T> responseWrapper = HTTPRequest.build(
                RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, responseClass)
                    .inlineVariables(bidPackageIdentity)
                    .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                    .expectedResponseCode(httpStatus))
            .get();

        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets bid packages.
     *
     * @param <T>           the type parameter
     * @param responseClass the response class
     * @param httpStatus    the http status
     * @param currentUser   the current user
     * @return the bid packages
     */
    public static <T> T getBidPackages(Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        ResponseWrapper<T> responseWrapper = HTTPRequest.build(
                RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, responseClass)
                    .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                    .expectedResponseCode(httpStatus))
            .get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Update bid package
     *
     * @param <T>                      the type parameter
     * @param bidPackageIdentity       the bid package identity
     * @param bidPackageRequestBuilder the bid package request builder
     * @param responseClass            the response class
     * @param httpStatus               the http status
     * @param currentUser              the current user
     * @return the response entity
     */
    public static <T> T updateBidPackage(String bidPackageIdentity, BidPackageRequest bidPackageRequestBuilder, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, responseClass)
            .inlineVariables(bidPackageIdentity)
            .body(bidPackageRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete bid package
     *
     * @param <T>                the type parameter
     * @param bidPackageIdentity the bid package identity
     * @param responseClass      the response class
     * @param httpStatus         the http status
     * @param currentUser        the current user
     * @return the response entity
     */
    public static <T> T deleteBidPackage(String bidPackageIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, responseClass)
            .inlineVariables(bidPackageIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).delete();
        return responseWrapper.getResponseEntity();
    }


    /**
     * Create bid package project
     *
     * @param <T>                  the type parameter
     * @param projectAttributesMap the project attributes map
     * @param bidPackageIdentity   the bid package identity
     * @param responseClass        the response class
     * @param httpStatus           the http status
     * @param currentUser          the current user
     * @return the response entity
     */
    public static <T> T createBidPackageProject(HashMap<String, String> projectAttributesMap, String bidPackageIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        BidPackageProjectRequest projectRequest = getBidPackageProjectRequestBuilder(projectAttributesMap, currentUser);
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECTS, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .inlineVariables(bidPackageIdentity)
            .body(projectRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Create bid package project
     *
     * @param <T>                             the type parameter
     * @param bidPackageProjectRequestBuilder the bid package project request builder
     * @param bidPackageIdentity              the bid package identity
     * @param responseClass                   the response class
     * @param httpStatus                      the http status
     * @param currentUser                     the current user
     * @return the response entity
     */
    public static <T> T createBidPackageProject(BidPackageProjectRequest bidPackageProjectRequestBuilder, String bidPackageIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECTS, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .inlineVariables(bidPackageIdentity)
            .body(bidPackageProjectRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets bid package projects.
     *
     * @param bidPackageIdentity the bid package identity
     * @param currentUser        the current user
     * @return the bid package projects
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
     * Delete bid package project
     *
     * @param <T>                       the type parameter
     * @param bidPackageIdentity        the bid package identity
     * @param bidPackageProjectIdentity the bid package project identity
     * @param responseClass             the response class
     * @param httpStatus                the http status
     * @param currentUser               the current user
     * @return the response entity
     */
    public static <T> T deleteBidPackageProject(String bidPackageIdentity, String bidPackageProjectIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).delete();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets bid package project
     *
     * @param <T>                       the type parameter
     * @param bidPackageIdentity        the bid package identity
     * @param bidPackageProjectIdentity the bid package project identity
     * @param klass                     the klass
     * @param httpStatus                the http status
     * @param currentUser               the current user
     * @return the bid package project
     */
    public static <T> T getBidPackageProject(String bidPackageIdentity, String bidPackageProjectIdentity, Class<T> klass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Update bid package project
     *
     * @param <T>                             the type parameter
     * @param bidPackageProjectRequestBuilder the bid package project request builder
     * @param bidPackageIdentity              the bid package identity
     * @param bidPackageProjectIdentity       the bid package project identity
     * @param currentUser                     the current user
     * @param klass                           the klass
     * @param httpStatus                      the http status
     * @return the response entity
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
     * Gets bid package project request builder.
     *
     * @param projectAttributesMap the project attributes map
     * @param currentUser          the current user
     * @return the bid package project request builder
     */
    public static BidPackageProjectRequest getBidPackageProjectRequestBuilder(HashMap<String, String> projectAttributesMap, UserCredentials currentUser) {
        String projectName = projectAttributesMap.getOrDefault("projectName", new GenerateStringUtil().getRandomString());
        String projectDisplayName = projectAttributesMap.getOrDefault("projectDisplayName", new GenerateStringUtil().getRandomString());
        String projectDescription = projectAttributesMap.getOrDefault("projectDescription", new GenerateStringUtil().getRandomString());
        String projectStatus = projectAttributesMap.getOrDefault("projectStatus", "COMPLETED");
        String projectOwner = projectAttributesMap.getOrDefault("projectOwner", new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()));
        String projectDueAt = projectAttributesMap.getOrDefault("projectDueAt", DateUtil.getDateDaysAfter(10, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ));

        BidPackageProjectRequest projectRequest = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream("testdata/QmsProjectRequest.json"), BidPackageProjectRequest.class);
        BidPackageProjectParameters project = projectRequest.getProject();
        project.setName(projectName);
        project.setDisplayName(projectDisplayName);
        project.setDescription(projectDescription);
        project.setDueAt(projectDueAt);
        project.setOwner(projectOwner);
        project.setStatus(projectStatus);
        project.setItems(null);
        project.setUsers(null);
        return projectRequest;
    }

    // BID PACKAGE ITEM RESOURCES

    /**
     * Create bid package item
     *
     * @param <T>                          the type parameter
     * @param bidPackageItemRequestBuilder the bid package item request builder
     * @param bidPackageIdentity           the bid package identity
     * @param currentUser                  the current user
     * @param klass                        the klass
     * @param httpStatus                   the http status
     * @return the response entity
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
     * Update bid package item
     *
     * @param <T>                          the type parameter
     * @param bidPackageItemRequestBuilder the bid package item request builder
     * @param bidPackageIdentity           the bid package identity
     * @param bidPackageItemIdentity       the bid package item identity
     * @param klass                        the klass
     * @param httpStatus                   the http status
     * @param currentUser                  the current user
     * @return the response entity
     */
    public static <T> T updateBidPackageItem(BidPackageItemRequest bidPackageItemRequestBuilder, String bidPackageIdentity, String bidPackageItemIdentity, Class<T> klass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .body(bidPackageItemRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets bid package item.
     *
     * @param <T>                    the type parameter
     * @param bidPackageIdentity     the bid package identity
     * @param bidPackageItemIdentity the bid package item identity
     * @param currentUser            the current user
     * @param klass                  the klass
     * @param httpStatus             the http status
     * @return the bid package item
     */
    public static <T> T getBidPackageItem(String bidPackageIdentity, String bidPackageItemIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets bid package items.
     *
     * @param <T>                the type parameter
     * @param bidPackageIdentity the bid package identity
     * @param currentUser        the current user
     * @param klass              the klass
     * @param httpStatus         the http status
     * @return the bid package items
     */
    public static <T> T getBidPackageItems(String bidPackageIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_ITEMS, klass)
            .inlineVariables(bidPackageIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete bid package item.
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
     * Bid package item request builder bid package item request.
     *
     * @param componentIdentity the component identity
     * @param scenarioIdentity  the scenario identity
     * @param iterationIdentity the iteration identity
     * @return the bid package item request
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
     * Create bid package project user bid package project users post response.
     *
     * @param role               the role
     * @param bidPackageIdentity the bid package identity
     * @param projectIdentity    the project identity
     * @param currentUser        the current user
     * @return the bid package project users post response
     */
    public static BidPackageProjectUsersPostResponse createBidPackageProjectUser(String role, String bidPackageIdentity, String projectIdentity, UserCredentials currentUser) {
        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(Collections.singletonList(BidPackageProjectUserParameters.builder()
                .userEmail(currentUser.getEmail())
                .role(role)
                .build()))
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_USERS, BidPackageProjectUsersPostResponse.class)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .body(bidPackageProjectUserRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<BidPackageProjectUsersPostResponse> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }


    /**
     * Create bid package project user.
     *
     * @param <T>                                 the type parameter
     * @param bidPackageProjectUserRequestBuilder the bid package project user request builder
     * @param bidPackageIdentity                  the bid package identity
     * @param projectIdentity                     the project identity
     * @param responseClass                       the response class
     * @param currentUser                         the current user
     * @return the response wrapper entity
     */
    public static <T> T createBidPackageProjectUser(BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder, String bidPackageIdentity, String projectIdentity, Class<T> responseClass, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_USERS, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .body(bidPackageProjectUserRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete bid package project user bid package project users delete response.
     *
     * @param userIdList         the user id list
     * @param bidPackageIdentity the bid package identity
     * @param projectIdentity    the project identity
     * @param currentUser        the current user
     * @return the bid package project users delete response
     */
    public static BidPackageProjectUsersDeleteResponse deleteBidPackageProjectUser(List<BidPackageProjectUserParameters> userIdList, String bidPackageIdentity, String projectIdentity, UserCredentials currentUser) {
        BidPackageProjectUserRequest deleteRequest = BidPackageProjectUserRequest.builder()
            .projectUsers(userIdList)
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_USERS_DELETE, BidPackageProjectUsersDeleteResponse.class)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .body(deleteRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackageProjectUsersDeleteResponse> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets bid package project user.
     *
     * @param <T>                 the type parameter
     * @param bidPackageIdentity  the bid package identity
     * @param projectIdentity     the project identity
     * @param projectUserIdentity the project user identity
     * @param currentUser         the current user
     * @param klass               the klass
     * @param httpStatus          the http status
     * @return the bid package project user
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
     * Gets bid package project users.
     *
     * @param <T>                the type parameter
     * @param bidPackageIdentity the bid package identity
     * @param projectIdentity    the project identity
     * @param currentUser        the current user
     * @param klass              the klass
     * @param httpStatus         the http status
     * @return the bid package project users
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
     * @param <T>                 the type parameter
     * @param role                the role
     * @param bidPackageIdentity  the bid package identity
     * @param projectIdentity     the project identity
     * @param projectUserIdentity the project user identity
     * @param currentUser         the current user
     * @param klass               the klass
     * @param httpStatus          the http status
     * @return the response entity
     */
    public static <T> T updateBidPackageProjectUser(String role, String bidPackageIdentity, String projectIdentity, String projectUserIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder = BidPackageProjectUserRequest.builder()
            .projectUsers(Collections.singletonList(BidPackageProjectUserParameters.builder()
                .userEmail(currentUser.getEmail())
                .role(role)
                .build()))
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_USER, klass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .inlineVariables(bidPackageIdentity, projectIdentity, projectUserIdentity)
            .body(bidPackageProjectUserRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();

    }

    /**
     * Update bid package project user
     *
     * @param <T>                 the type parameter
     * @param projectUserRequest  the project user request
     * @param bidPackageIdentity  the bid package identity
     * @param projectIdentity     the project identity
     * @param projectUserIdentity the project user identity
     * @param currentUser         the current user
     * @param klass               the klass
     * @param httpStatus          the http status
     * @return the response entity
     */
    public static <T> T updateBidPackageProjectUser(BidPackageProjectUserRequest projectUserRequest, String bidPackageIdentity, String projectIdentity, String projectUserIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_USER, klass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .inlineVariables(bidPackageIdentity, projectIdentity, projectUserIdentity)
            .body(projectUserRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();

    }

    /**
     * Gets participants.
     *
     * @param currentUser the current user
     * @return the participants
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
     * Create bid package bulk project items bid package project items bulk response.
     *
     * @param bidPackageIdentity the bid package identity
     * @param projectIdentity    the project identity
     * @param bidPackageItemList the bid package item list
     * @param currentUser        the current user
     * @return the bid package project items bulk response
     */
    public static <T> T createBidPackageBulkProjectItems(String bidPackageIdentity, String projectIdentity, List<BidPackageProjectItem> bidPackageItemList, Class<T> klass, UserCredentials currentUser) {
        BidPackageProjectItemsRequest bidPackageProjectItemsRequestBuilder = BidPackageProjectItemsRequest.builder()
            .projectItems(BidPackageProjectItems.builder()
                .projectItem(bidPackageItemList)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_ITEMS, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .body(bidPackageProjectItemsRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete bid package bulk project items bid package project items bulk response.
     *
     * @param <T>                                 the type parameter
     * @param bidPackageIdentity                  the bid package identity
     * @param projectIdentity                     the project identity
     * @param bidPackageProjectItemIdentitiesList the bid package item list
     * @param klass                               the klass
     * @param httpStatus                          the http status
     * @param currentUser                         the current user
     * @return the bid package project items bulk response
     */
    public static <T> T deleteBidPackageBulkProjectItems(String bidPackageIdentity, String projectIdentity, List<BidPackageProjectItem> bidPackageProjectItemIdentitiesList, Class<T> klass, Integer httpStatus, UserCredentials currentUser) {
        BidPackageProjectItemsRequest bidPackageProjectItemsRequestBuilder = BidPackageProjectItemsRequest.builder()
            .projectItems(BidPackageProjectItems.builder()
                .projectItem(bidPackageProjectItemIdentitiesList)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE_PROJECT_ITEMS_DELETE, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .body(bidPackageProjectItemsRequestBuilder)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }
}