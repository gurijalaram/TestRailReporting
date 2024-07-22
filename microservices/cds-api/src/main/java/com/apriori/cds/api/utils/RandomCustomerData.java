package com.apriori.cds.api.utils;

import com.apriori.shared.util.http.utils.GenerateStringUtil;

import lombok.Getter;

@Getter
public class RandomCustomerData {
    private final GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private final String customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
    private final String emailPattern = "\\S+@".concat(customerName);
    private final String cloudRef = generateStringUtil.generateCloudReference();
    private final String salesForceId = generateStringUtil.generateNumericString("SFID", 10);
    private final String siteName = generateStringUtil.generateAlphabeticString("Site", 5);
    private final String siteID = generateStringUtil.generateSiteID();
    private final String realmKey = generateStringUtil.generateNumericString("RealmKey", 26);
    private final String customerType = Constants.CLOUD_CUSTOMER;
}
