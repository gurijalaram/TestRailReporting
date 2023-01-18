package com.apriori.cisapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cisapi.entity.enums.CisAPIEnum;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageParameters;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageRequest;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;

public class CisBidPackageResources {

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
                .assignedTo("David Ogunseyin")
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
     * @param bidPackageRequestBuilder BidPackageRequest Data builder
     * @param responseClass            Expected response class
     * @param httpStatus               Expected http status code
     * @param currentUser              UserCredentials class object
     * @param <T>                      Response class type
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
     * @param bidPackageIdentity Bid package identity
     * @param responseClass      Expected response class
     * @param httpStatus         expected http status code
     * @param currentUser        UserCredentials
     * @param <T>                response class type
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
     * @param responseClass expected response class
     * @param httpStatus    expected http status code
     * @param currentUser   UserCredentials
     * @param <T>           expected response class type
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
     * @param bidPackageIdentity       Bid Package Identity
     * @param bidPackageRequestBuilder BidPackageRequest request data builder
     * @param responseClass            expected response class
     * @param httpStatus               expected http status code
     * @param currentUser              UserCredentials
     * @param <T>                      expected response class type
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
     * @param bidPackageIdentity bid package identity
     * @param responseClass      expected class name
     * @param httpStatus         expected http status code
     * @param currentUser        UserCredentials class object
     * @param <T>                response class tupe
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


