package com.apriori.qds.controller;

import com.apriori.DateFormattingUtils;
import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.DateUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.json.JsonManager;
import com.apriori.qds.enums.QDSAPIEnum;
import com.apriori.qds.models.request.bidpackage.BidPackageItemParameters;
import com.apriori.qds.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.qds.models.request.bidpackage.BidPackageParameters;
import com.apriori.qds.models.request.bidpackage.BidPackageProjectItemParameters;
import com.apriori.qds.models.request.bidpackage.BidPackageProjectItemRequest;
import com.apriori.qds.models.request.bidpackage.BidPackageProjectParameters;
import com.apriori.qds.models.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qds.models.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qds.models.request.bidpackage.BidPackageProjectUserRequest;
import com.apriori.qds.models.request.bidpackage.BidPackageRequest;
import com.apriori.qds.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qds.models.response.bidpackage.BidPackageProjectUserResponse;
import com.apriori.qds.models.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qds.models.response.bidpackage.BidPackageResponse;
import com.apriori.qds.utils.QdsApiTestUtils;
import com.apriori.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;

import java.util.HashMap;

/**
 * The type Bid package resources.
 */
public class BidPackageResources {

    /**
     * Create bid package response.
     *
     * @param bidPackageName the bid package name
     * @param userContext    the user context
     * @return the bid package response
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
     * Delete bid package response wrapper.
     *
     * @param bidPackageIdentity the bid package identity
     * @param currentUser        the current user
     * @return the response wrapper
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
     * Create bid package project bid package project response.
     *
     * @param projectAttributesMap the project attributes map
     * @param bidPackageIdentity   the bid package identity
     * @param currentUser          the current user
     * @return the bid package project response
     */
    public static BidPackageProjectResponse createBidPackageProject(HashMap<String, String> projectAttributesMap, String bidPackageIdentity, UserCredentials currentUser) {
        BidPackageProjectRequest projectRequest = getBidPackageProjectRequestBuilder(projectAttributesMap, currentUser);
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECTS, BidPackageProjectResponse.class)
            .inlineVariables(bidPackageIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .body(projectRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return (BidPackageProjectResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }


    /**
     * Create bid package project response wrapper.
     *
     * @param <T>                             the type parameter
     * @param bidPackageProjectRequestBuilder the bid package project request builder
     * @param bidPackageIdentity              the bid package identity
     * @param currentUser                     the current user
     * @param responseClass                   the response class
     * @param httpStatus                      the http status
     * @return the response wrapper
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
     * Delete bid package project response wrapper.
     *
     * @param bidPackageIdentity        the bid package identity
     * @param bidPackageProjectIdentity the bid package project identity
     * @param currentUser               the current user
     */
    public static void deleteBidPackageProject(String bidPackageIdentity, String bidPackageProjectIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT, null)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Gets bid package projects.
     *
     * @param bidPackageIdentity the bid package identity
     * @param currentUser        the current user
     * @return the bid package projects
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
     * Gets bid package project.
     *
     * @param <T>                       the type parameter
     * @param bidPackageIdentity        the bid package identity
     * @param bidPackageProjectIdentity the bid package project identity
     * @param currentUser               the current user
     * @param klass                     the klass
     * @param httpStatus                the http status
     * @return the bid package project
     */
    public static <T> T getBidPackageProject(String bidPackageIdentity, String bidPackageProjectIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);
        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Update bid package project t.
     *
     * @param <T>                             the type parameter
     * @param bidPackageProjectRequestBuilder the bid package project request builder
     * @param bidPackageIdentity              the bid package identity
     * @param bidPackageProjectIdentity       the bid package project identity
     * @param currentUser                     the current user
     * @param klass                           the klass
     * @param httpStatus                      the http status
     * @return the t
     */
    public static <T> T updateBidPackageProject(BidPackageProjectRequest bidPackageProjectRequestBuilder, String bidPackageIdentity, String bidPackageProjectIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
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
        String projectDisplayName = projectAttributesMap.getOrDefault("projectDisplayName", "N/A");
        String projectDescription = projectAttributesMap.getOrDefault("projectDescription", new GenerateStringUtil().getRandomString());
        String projectStatus = projectAttributesMap.getOrDefault("projectStatus", "COMPLETED");
        String projectOwner = projectAttributesMap.getOrDefault("projectOwner", "N/A");
        String projectOwnerUserIdentity = projectAttributesMap.getOrDefault("projectOwnerUserIdentity", new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()));
        String projectDueAt = projectAttributesMap.getOrDefault("projectDueAt", DateUtil.getDateDaysAfter(10, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ));

        BidPackageProjectRequest projectRequest = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream("testdata/QdsProjectRequest.json"), BidPackageProjectRequest.class);
        BidPackageProjectParameters project = projectRequest.getProject();
        project.setName(projectName);
        project.setDisplayName(projectDisplayName);
        project.setDescription(projectDescription);
        project.setDueAt(projectDueAt);
        project.setOwner(projectOwner);
        project.setStatus(projectStatus);
        project.setOwnerUserIdentity(projectOwnerUserIdentity);
        project.setItems(null);
        project.setUsers(null);
        return projectRequest;
    }

    // BID PACKAGE PROJECT ITEM RESOURCES

    /**
     * Create bid package project item t.
     *
     * @param <T>                 the type parameter
     * @param bidPackageIdentity  the bid package identity
     * @param projectIdentity     the project identity
     * @param projectItemIdentity the project item identity
     * @param currentUser         the current user
     * @param klass               the klass
     * @param httpStatus          the http status
     * @return the t
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

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
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
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_ITEM, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectItemIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
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
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_ITEMS, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete bid package project item response wrapper.
     *
     * @param bidPackageIdentity  the bid package identity
     * @param projectIdentity     the project identity
     * @param projectItemIdentity the project item identity
     * @param currentUser         the current user
     */
    public static void deleteBidPackageProjectItem(String bidPackageIdentity, String projectIdentity, String projectItemIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_ITEM, null)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectItemIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    // BID PACKAGE ITEM RESOURCES

    /**
     * Create bid package item t.
     *
     * @param <T>                          the type parameter
     * @param bidPackageItemRequestBuilder the bid package item request builder
     * @param bidPackageIdentity           the bid package identity
     * @param currentUser                  the current user
     * @param klass                        the klass
     * @param httpStatus                   the http status
     * @return the t
     */
    public static <T> T createBidPackageItem(BidPackageItemRequest bidPackageItemRequestBuilder, String bidPackageIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_ITEMS, klass)
            .inlineVariables(bidPackageIdentity)
            .body(bidPackageItemRequestBuilder)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Update bid package item t.
     *
     * @param <T>                          the type parameter
     * @param bidPackageItemRequestBuilder the bid package item request builder
     * @param bidPackageIdentity           the bid package identity
     * @param bidPackageItemIdentity       the bid package item identity
     * @param currentUser                  the current user
     * @param klass                        the klass
     * @param httpStatus                   the http status
     * @return the t
     */
    public static <T> T updateBidPackageItem(BidPackageItemRequest bidPackageItemRequestBuilder, String bidPackageIdentity, String bidPackageItemIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .body(bidPackageItemRequestBuilder)
            .headers(QdsApiTestUtils.setUpHeader())
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
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
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
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_ITEMS, klass)
            .inlineVariables(bidPackageIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete bid package item response wrapper.
     *
     * @param bidPackageIdentity     the bid package identity
     * @param bidPackageItemIdentity the bid package item identity
     * @param currentUser            the current user
     */
    public static void deleteBidPackageItem(String bidPackageIdentity, String bidPackageItemIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_ITEM, null)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
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
     * Create bid package project user bid package project user response.
     *
     * @param role               the role
     * @param bidPackageIdentity the bid package identity
     * @param projectIdentity    the project identity
     * @param currentUser        the current user
     * @return the bid package project user response
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
     * Delete bid package project user response wrapper.
     *
     * @param bidPackageIdentity  the bid package identity
     * @param projectIdentity     the project identity
     * @param projectUserIdentity the project user identity
     * @param currentUser         the current user
     */
    public static void deleteBidPackageProjectUser(String bidPackageIdentity, String projectIdentity, String projectUserIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_USER, null)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectUserIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
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
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_USER, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity, projectUserIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
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
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_USERS, klass)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();

    }

    /**
     * Update bid package project user t.
     *
     * @param <T>                 the type parameter
     * @param role                the role
     * @param bidPackageIdentity  the bid package identity
     * @param projectIdentity     the project identity
     * @param projectUserIdentity the project user identity
     * @param currentUser         the current user
     * @param klass               the klass
     * @param httpStatus          the http status
     * @return the t
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

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }
}
