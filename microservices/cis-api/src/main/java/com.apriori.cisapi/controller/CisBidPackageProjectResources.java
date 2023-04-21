package com.apriori.cisapi.controller;

import com.apriori.cisapi.entity.enums.CisAPIEnum;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageProjectParameters;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageProjectProfile;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.cisapi.entity.request.bidpackage.CommentReminder;
import com.apriori.cisapi.entity.request.bidpackage.EmailReminder;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

public class CisBidPackageProjectResources {

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
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECTS, responseClass)
            .inlineVariables(bidPackageIdentity)
            .body(projectRequest)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
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
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECTS, klass)
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
     * @param responseClass             - expected response class
     * @param httpStatus                - expected http status code
     * @param currentUser               - UserCredentials
     * @return ResponseWrapper[String]
     */
    public static <T> T deleteBidPackageProject(String bidPackageIdentity, String bidPackageProjectIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT, responseClass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).delete();
        return responseWrapper.getResponseEntity();
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
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT, klass)
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
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_PROJECT, klass)
            .inlineVariables(bidPackageIdentity, bidPackageProjectIdentity)
            .body(bidPackageProjectRequestBuilder)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * BidPackageProjectRequestBuilder
     *
     * @param projectName        -project name
     * @param projectDescription - project description
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
}


