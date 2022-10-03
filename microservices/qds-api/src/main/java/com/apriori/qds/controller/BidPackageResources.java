package com.apriori.qds.controller;

import com.apriori.qds.entity.request.bidpackage.BidPackageParameters;
import com.apriori.qds.entity.request.bidpackage.BidPackageRequest;
import com.apriori.qds.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qds.enums.QDSAPIEnum;
import com.apriori.qds.utils.QdsApiTestUtils;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

public class BidPackageResources {

    /**
     * Create bid package
     *
     * @return response object on BidPackageResponse class
     */
    public static ResponseWrapper<BidPackageResponse> createBidPackage(String bidPackageName, String userContext) {
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
            .apUserContext(userContext);

        ResponseWrapper<BidPackageResponse> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        return bidPackagesResponse;
    }

    /**
     * delete bid package
     * @param bidPackageIdentity
     * @param userContext
     * @return Response object of string
     */
    public static ResponseWrapper<String> deleteBidPackage(String bidPackageIdentity, String userContext) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, null)
            .inlineVariables(bidPackageIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        return HTTPRequest.build(requestEntity).delete();
    }
}
