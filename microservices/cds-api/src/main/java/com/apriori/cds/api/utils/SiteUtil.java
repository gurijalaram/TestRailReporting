package com.apriori.cds.api.utils;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Site;

import org.apache.http.HttpStatus;

public class SiteUtil {
    private RequestEntityUtil requestEntityUtil;

    public SiteUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
    }

    /**
     * POST call to add a site to a customer
     *
     * @param customerIdentity - the customer id
     * @param siteName         - the site name
     * @param siteID           - the siteID
     * @return new object
     */
    public ResponseWrapper<Site> addSite(String customerIdentity, String siteName, String siteID) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.SITES_BY_CUSTOMER_ID, Site.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "site",
                Site.builder().name(siteName)
                    .description("Site created by automation test")
                    .siteId(siteID)
                    .createdBy("#SYSTEM00000")
                    .active(true)
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }
}
