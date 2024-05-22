package com.apriori.cis.api.controller;

import com.apriori.cis.api.enums.CisAPIEnum;
import com.apriori.cis.api.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectRequest;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectUserRequest;
import com.apriori.cis.api.models.response.bidpackage.BidPackageProjectUsersDeleteResponse;
import com.apriori.cis.api.util.CISTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.json.JsonManager;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class CisBidPackageProjectResources extends CISTestUtil {

    /**
     * Create bid package project
     *
     * @param projectName        - Unique project name
     * @param bidPackageIdentity - bid package identity
     * @param responseClass      - expected response class
     * @param httpStatus         - expected http status code
     * @param currentUser        - UserCredentials
     * @param <T>                - response class type
     * @return response class object
     */
    public static <T> T createBidPackageProject(String projectName, String bidPackageIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        BidPackageProjectRequest projectRequest = getBidPackageProjectRequestBuilder(projectName, projectName);
        List<BidPackageItemRequest> bidPackageProjectItemRequestList = new ArrayList<>();
        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        projectRequest.getProject().setItems(bidPackageProjectItemRequestList);
        projectRequest.getProject().setUsers(usersList);
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECTS, responseClass)
            .inlineVariables(bidPackageIdentity)
            .body(projectRequest)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Get Bidpackage project request builder
     *
     * @param projectName        -project name
     * @param projectDescription - project description
     * @return BidPackageProjectRequest
     */
    public static BidPackageProjectRequest getBidPackageProjectRequestBuilder(String projectName, String projectDescription) {
        BidPackageProjectRequest bidPackageProjectRequest = JsonManager.deserializeJsonFromInputStream(
            FileResourceUtil.getResourceFileStream("testdata/BidPackageProjectRequest.json"),
            BidPackageProjectRequest.class);
        bidPackageProjectRequest.getProject().setName(projectName);
        bidPackageProjectRequest.getProject().setDescription(projectDescription);
        return bidPackageProjectRequest;
    }

    /**
     * Get Bid Package Projects
     *
     * @param <T>                - response class type
     * @param bidPackageIdentity - bid package identity
     * @param currentUser        - UserCredentials
     * @param klass              - response class name
     * @param httpStatus         - Integer
     * @return klass object
     */
    public static <T> T getBidPackageProjects(String bidPackageIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECTS, klass)
            .inlineVariables(bidPackageIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> bppResponse = HTTPRequest.build(requestEntity).get();
        return bppResponse.getResponseEntity();
    }

    /**
     * delete Bid Package Project
     *
     * @param bidPackageIdentity        - bid package identity
     * @param bidPackageProjectIdentity - bid package project identity
     * @param httpStatus                - expected http status code
     * @param currentUser               - UserCredentials
     */
    public static void deleteBidPackageProject(String bidPackageIdentity, String bidPackageProjectIdentity, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT, null)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        HTTPRequest.build(requestEntity).delete();
    }

    /**
     * get Bid Package Project
     *
     * @param <T>                       - klass type
     * @param bidPackageIdentity        - bid package identity
     * @param bidPackageProjectIdentity - bid package project identity
     * @param klass                     response class name
     * @param httpStatus                -Integer
     * @param currentUser               - UserCredentials
     * @return klass object
     */
    public static <T> T getBidPackageProject(String bidPackageIdentity, String bidPackageProjectIdentity, Class<T> klass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Update Bid Package Project
     *
     * @param <T>                             - response of class type
     * @param bidPackageProjectRequestBuilder - BidPackageProjectRequest
     * @param bidPackageIdentity              - bid package identity
     * @param bidPackageProjectIdentity       - bid package project identity
     * @param currentUser                     - UserCredentials
     * @param klass                           - response of klass name
     * @param httpStatus                      - Integer
     * @return - response klass
     */
    public static <T> T updateBidPackageProject(BidPackageProjectRequest bidPackageProjectRequestBuilder, String bidPackageIdentity, String bidPackageProjectIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .body(bidPackageProjectRequestBuilder)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
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
     * @param httpStatus                          expected http status code
     * @return the response entity
     */
    public static <T> T createBidPackageProjectUser(BidPackageProjectUserRequest bidPackageProjectUserRequestBuilder, String bidPackageIdentity, String projectIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT_USERS, responseClass)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .body(bidPackageProjectUserRequestBuilder)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete bid package project users.
     *
     * @param userIdList         the user id list
     * @param bidPackageIdentity the bid package identity
     * @param projectIdentity    the project identity
     * @param currentUser        the current user
     * @return the bid package project users delete response
     */
    public static BidPackageProjectUsersDeleteResponse deleteBidPackageProjectUsers(List<BidPackageProjectUserParameters> userIdList, String bidPackageIdentity, String projectIdentity, UserCredentials currentUser) {
        BidPackageProjectUserRequest deleteRequest = BidPackageProjectUserRequest.builder()
            .projectUsers(userIdList)
            .build();

        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT_USERS_DELETE, BidPackageProjectUsersDeleteResponse.class)
            .inlineVariables(bidPackageIdentity, projectIdentity)
            .body(deleteRequest)
            .token(currentUser.getToken())
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackageProjectUsersDeleteResponse> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * get Bid Package Project Users.
     *
     * @param bidPackageIdentity        - bid package Identity
     * @param bidPackageProjectIdentity - Bid package project identity
     * @param klass                     - expected response class
     * @param httpStatus                - expected http status code
     * @param currentUser               - User
     * @param <T>                       - expected response class type
     * @return - expected response
     */
    public static <T> T getBidPackageProjectUsers(String bidPackageIdentity, String bidPackageProjectIdentity, Class<T> klass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT_USERS, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }
}


