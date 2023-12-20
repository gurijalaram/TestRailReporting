package com.apriori.cis.api.controller;

import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cis.api.enums.CisAPIEnum;
import com.apriori.cis.api.models.request.bidpackage.BidPackageItemParameters;
import com.apriori.cis.api.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.css.api.enums.CssSearch;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.models.response.component.ScenarioItem;

import org.apache.http.HttpStatus;

import java.io.File;

/**
 * The type Cis bid package item resources.
 */
@SuppressWarnings("unchecked")
public class CisBidPackageItemResources {

    /**
     * Create Bid Package Item
     *
     * @param <T>                          - response class type
     * @param bidPackageItemRequestBuilder - BidPackageItemRequest request data builder
     * @param bidPackageIdentity           - bid package identity
     * @param currentUser                  - UserCredentials
     * @param klass                        - response class name
     * @param httpStatus                   - Integer
     * @return klass object
     */
    public static <T> T createBidPackageItem(BidPackageItemRequest bidPackageItemRequestBuilder, String bidPackageIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(CisAPIEnum.BID_PACKAGE_ITEMS, klass)
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
     * @param bidPackageItemRequestBuilder - BidPackageItemRequest request data builder
     * @param bidPackageIdentity           - bid package identity
     * @param bidPackageItemIdentity       - bid package item identity
     * @param klass                        - response class name
     * @param httpStatus                   - Integer
     * @param currentUser                  - UserCredentials
     * @return klass object
     */
    public static <T> T updateBidPackageItem(BidPackageItemRequest bidPackageItemRequestBuilder, String bidPackageIdentity, String bidPackageItemIdentity, Class<T> klass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(CisAPIEnum.BID_PACKAGE_ITEM, klass)
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
     * @param bidPackageIdentity     - bid package identity
     * @param bidPackageItemIdentity - bid package item identity
     * @param currentUser            - UserCredentials
     * @param klass                  - response class name
     * @param httpStatus             - Integer
     * @return klass object
     */
    public static <T> T getBidPackageItem(String bidPackageIdentity, String bidPackageItemIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(CisAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get List of all bid package items
     *
     * @param <T>                - response class type
     * @param bidPackageIdentity - bid package identity
     * @param currentUser        - UserCredentials
     * @param klass              - response class name
     * @param httpStatus         the http status
     * @return klass object
     */
    public static <T> T getBidPackageItems(String bidPackageIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(CisAPIEnum.BID_PACKAGE_ITEMS, klass)
            .inlineVariables(bidPackageIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Delete bid package item
     *
     * @param <T>                    - response class type
     * @param bidPackageIdentity     - bid package identity
     * @param bidPackageItemIdentity - bid package item identity
     * @param httpStatus             - expected http status code
     * @param currentUser            - UserCredentials class object
     * @param klass                  - response class name
     * @return klass object
     */
    public static <T> T deleteBidPackageItem(String bidPackageIdentity, String bidPackageItemIdentity, Integer httpStatus, UserCredentials currentUser, Class<T> klass) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(CisAPIEnum.BID_PACKAGE_ITEM, klass)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).delete().getResponseEntity();
    }

    /**
     * Delete bid package item
     *
     * @param bidPackageIdentity     - bid package identity
     * @param bidPackageItemIdentity - bid package item identity
     * @param currentUser            - UserCredentials class object
     */
    public static void deleteBidPackageItem(String bidPackageIdentity, String bidPackageItemIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(CisAPIEnum.BID_PACKAGE_ITEM, null)
            .inlineVariables(bidPackageIdentity, bidPackageItemIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }


    /**
     * Create and get BidPackageItemRequestBuilder
     *
     * @param componentIdentity - the component identity
     * @param scenarioIdentity  - the scenario identity
     * @param iterationIdentity - the iteration identity
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

        new ComponentsUtil().postComponents(componentInfoBuilder);
        return new CssComponent().getBaseCssComponents(currentUser,
            CssSearch.COMPONENT_NAME_EQ.getKey() + componentName,
            CssSearch.SCENARIO_NAME_EQ.getKey() + scenarioName).get(0);
    }
}


