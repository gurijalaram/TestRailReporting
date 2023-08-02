package com.apriori.cis.controller;

import com.apriori.cis.enums.CisAPIEnum;
import com.apriori.cis.models.request.bidpackage.BidPackageItem;
import com.apriori.cis.models.request.bidpackage.BidPackageProjectItemParameters;
import com.apriori.cis.models.request.bidpackage.BidPackageProjectItemRequest;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;

@SuppressWarnings("unchecked")
public class CisBidPackageProjectItemResources {

    /**
     * Create bid package project item
     *
     * @param bidPackageIdentity        - bid package identity
     * @param bidPackageItemIdentity    - Unique project name
     * @param bidPackageProjectIdentity - bid package item identity
     * @param responseClass             - expected response class
     * @param httpStatus                - expected http status code
     * @param currentUser               - UserCredentials
     * @param <T>                       - response class type
     * @return response class object
     */
    public static <T> T createBidPackageProjectItem(String bidPackageIdentity, String bidPackageItemIdentity, String bidPackageProjectIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        BidPackageProjectItemRequest projectItemRequest = getBidPackageProjectItemRequestBuilder(bidPackageItemIdentity);
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT_ITEMS, responseClass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .body(projectItemRequest)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * BidPackageProjectItemRequestBuilder
     *
     * @param bidPackageItemIdentity - bid package project item identity
     * @return BidPackageProjectRequest
     */
    public static BidPackageProjectItemRequest getBidPackageProjectItemRequestBuilder(String bidPackageItemIdentity) {
        return BidPackageProjectItemRequest.builder()
            .projectItem(BidPackageProjectItemParameters.builder()
                .bidPackageItem(BidPackageItem.builder().identity(bidPackageItemIdentity).build())
                .build())
            .build();
    }

    /**
     * Delete bid package project item
     *
     * @param bidPackageIdentity            - bid package identity
     * @param bidPackageProjectIdentity     - bid package item identity
     * @param bidPackageProjectItemIdentity - bid package item identity
     * @param currentUser                   - UserCredentials class object
     * @return ResponseWrapper of String
     */
    public static <T> T deleteBidPackageProjectItem(String bidPackageIdentity, String bidPackageProjectIdentity, String bidPackageProjectItemIdentity, Class<T> responseClass, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT_ITEM, responseClass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity, bidPackageProjectItemIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return (T) HTTPRequest.build(requestEntity).delete().getResponseEntity();
    }

    /**
     * Delete Bid Package Project Item
     *
     * @param bidPackageIdentity        - bid package identity
     * @param bidPackageProjectIdentity - bid package project identity
     * @param responseClass             - expected response class
     * @param httpStatus                - expected http status code
     * @param currentUser               - UserCredentials
     * @return ResponseWrapper[String]
     */
    public static <T> T deleteBidPackageProjectItem(String bidPackageIdentity, String bidPackageProjectIdentity, String bidPackageProjectItemIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT_ITEM, responseClass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity, bidPackageProjectItemIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).delete();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Get Bid Package Project Items
     *
     * @param <T>                       - response class type
     * @param bidPackageIdentity        - bid package identity
     * @param bidPackageProjectIdentity - bid package project identity
     * @param currentUser               - UserCredentials
     * @param klass                     - response class name
     * @param httpStatus                - Integer
     * @return klass object
     */
    public static <T> T getBidPackageProjectItems(String bidPackageIdentity, String bidPackageProjectIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT_ITEMS, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get Bid Package Project Item
     *
     * @param <T>                           - response class type
     * @param bidPackageIdentity            - bid package identity
     * @param bidPackageProjectIdentity     - bid package project identity
     * @param bidPackageProjectItemIdentity - bid package project item identity
     * @param currentUser                   - UserCredentials
     * @param klass                         - response class name
     * @param httpStatus                    - Integer
     * @return klass object
     */
    public static <T> T getBidPackageProjectItem(String bidPackageIdentity, String bidPackageProjectIdentity, String bidPackageProjectItemIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity, bidPackageProjectItemIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get Bid Package Project Item Users
     *
     * @param bidPackageProjectIdentity     - bid package project identity
     * @param bidPackageProjectItemIdentity - bid package project item identity
     * @param responseClass                 - expected response class
     * @param httpStatus                    - expected http status code
     * @param currentUser                   - UserCredentials
     * @return ResponseWrapper[String]
     */

    public static <T> T getBidPackageProjectItemUsers(String bidPackageProjectIdentity, String bidPackageProjectItemIdentity, UserCredentials currentUser, Class<T> responseClass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT_ITEM_USERS, responseClass)
            .inlineVariables(bidPackageProjectIdentity, bidPackageProjectItemIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }
}


