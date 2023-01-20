package com.apriori.cisapi.utils;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cisapi.entity.enums.CisAPIEnum;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageParameters;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageRequest;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageResponse;
import com.apriori.entity.enums.CssSearch;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.utils.CssComponent;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;

import java.io.File;

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

    //BID PACKAGE ITEMS

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
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_ITEMS, klass)
            .inlineVariables(bidPackageIdentity)
            .body(bidPackageItemRequestBuilder)
            .token(currentUser.getToken())
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
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .body(bidPackageItemRequestBuilder)
            .token(currentUser.getToken())
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
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .token(currentUser.getToken())
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
     * @return klass object
     */
    public static <T> T getBidPackageItems(String bidPackageIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_ITEMS, klass)
            .inlineVariables(bidPackageIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Delete bid package item
     *
     * @param bidPackageIdentity     - bid package identity
     * @param bidPackageItemIdentity - bid package item identity
     * @param httpStatus             - expected http status code
     * @param currentUser            - UserCredentials class object
     * @param klass                  - response class name
     * @param <T>                    - response class tupe
     * @return klass object
     */
    public static <T> T deleteBidPackageItem(String bidPackageIdentity, String bidPackageItemIdentity, Integer httpStatus, UserCredentials currentUser, Class<T> klass) {
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).delete().getResponseEntity();
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
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.BID_PACKAGE_ITEM, null)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .token(currentUser.getToken())
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
     * Create Component
     *
     * @param processGroupEnum - ProcessGroupEnum
     * @param componentName    - component name
     * @param currentUser      - UserCredentials
     * @return ScenarioItem object
     */
    public static ScenarioItem createAndQueryComponent(ProcessGroupEnum processGroupEnum, String componentName, UserCredentials currentUser) {
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        ComponentInfoBuilder componentInfoBuilder = ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build();

        new ComponentsUtil().postComponent(componentInfoBuilder);
        return new CssComponent().getBaseCssComponents(currentUser,
            CssSearch.COMPONENT_NAME_EQ.getKey() + componentName,
            CssSearch.SCENARIO_NAME_EQ.getKey() + scenarioName).get(0);
    }
}


