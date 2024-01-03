package com.apriori.qds.api.utils;

import com.apriori.qds.api.models.request.bidpackage.BidPackageParameters;
import com.apriori.qds.api.models.request.bidpackage.BidPackageRequest;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;
import com.apriori.shared.util.http.utils.TestUtil;

import org.junit.jupiter.api.BeforeAll;

import java.util.HashMap;
import java.util.Map;

public class QdsApiTestUtils extends TestUtil {

    protected static UserCredentials testingUser;
    protected static RequestEntityUtil requestEntityUtil;

    @BeforeAll
    public static void init() {
        requestEntityUtil = RequestEntityUtilBuilder.useRandomUser("admin")
            .useApUserContextInRequests()
            .useTokenInRequests();

        testingUser = requestEntityUtil
            .getEmbeddedUser();
    }

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
