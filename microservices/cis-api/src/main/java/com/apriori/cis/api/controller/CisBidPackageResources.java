package com.apriori.cis.api.controller;

import com.apriori.cis.api.enums.CisAPIEnum;
import com.apriori.cis.api.models.request.bidpackage.BidPackageParameters;
import com.apriori.cis.api.models.request.bidpackage.BidPackageRequest;
import com.apriori.cis.api.models.response.bidpackage.BidPackageResponse;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

/**
 * The type Cis bid package resources.
 */
@SuppressWarnings("unchecked")
public class CisBidPackageResources {

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

        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGES, BidPackageResponse.class)
            .body(bidPackageRequest)
            .token(currentUser.getToken())
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
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGES, responseClass)
            .body(bidPackageRequestBuilder)
            .token(currentUser.getToken())
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
                RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE, responseClass)
                    .inlineVariables(bidPackageIdentity)
                    .token(currentUser.getToken())
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
                RequestEntityUtil.init(CisAPIEnum.BID_PACKAGES, responseClass)
                    .token(currentUser.getToken())
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
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE, responseClass)
            .inlineVariables(bidPackageIdentity)
            .body(bidPackageRequestBuilder)
            .token(currentUser.getToken())
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
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE, responseClass)
            .inlineVariables(bidPackageIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).delete().getResponseEntity();
    }
}


