package com.apriori.cds.api.utils;

import com.apriori.shared.util.http.utils.GenerateStringUtil;

import lombok.Getter;

@Getter
public class RandomCustomerData {
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerName = generateStringUtil.generateCustomerName();
    private String cloudRef = generateStringUtil.generateCloudReference();
    private String salesForceId = generateStringUtil.generateSalesForceId();
    private String emailPattern = "\\S+@".concat(customerName);
    private String siteName = generateStringUtil.generateSiteName();
    private String siteID = generateStringUtil.generateSiteID();
    private String realmKey = generateStringUtil.generateRealmKey();
    private String customerType = Constants.CLOUD_CUSTOMER;
}
