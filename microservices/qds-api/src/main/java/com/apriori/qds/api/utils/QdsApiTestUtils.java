package com.apriori.qds.api.utils;

import com.apriori.qds.api.models.request.bidpackage.BidPackageParameters;
import com.apriori.qds.api.models.request.bidpackage.BidPackageRequest;

import java.util.HashMap;
import java.util.Map;

public class QdsApiTestUtils {


    /**
     * setup header information for QDS API Authorization
     *
     * @return Map
     */
    public static Map<String, String> setUpHeader(String userContext) {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("ap-user-context", userContext);
        header.put("Content-Type","application/json");
        return header;
    }

    public static Map<String, String> setUpHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Content-Type","application/json");
        return header;
    }

    public static BidPackageRequest getBidPackageRequest(String userIdentity, String bidPackageName, String description) {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description(description)
                .name(bidPackageName)
                .status("NEW")
                .assignedTo(userIdentity)
                .build())
            .build();
        return bidPackageRequest;
    }

}
