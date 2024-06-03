package com.apriori.cds.api.utils;

import com.apriori.shared.util.http.utils.AwsParameterStoreUtil;
import com.apriori.shared.util.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Constants {

    public static final String SIGNIN_URL = "https://aprioritest.oktapreview.com/app/aprioritest_aprioritechnologies_1/exkuyvviKdbHl5IMa1d5/sso/sam";
    public static final String SAML_ATTRIBUTE_NAME_IDENTIFIER = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier";
    public static final String SAML_ATTRIBUTE_NAME_EMAIL = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress";
    public static final String SAML_ATTRIBUTE_NAME = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name";
    public static final String SAML_ATTRIBUTE_NAME_GIVEN_NAME = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name";
    public static final String SAML_ATTRIBUTE_NAME_FAMILY_NAME = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/surname";
    public static final String CLOUD_CUSTOMER = "CLOUD_ONLY";
    public static final String ON_PREM_CUSTOMER = "ON_PREMISE_ONLY";
    public static final String USERS_BATCH = "loginID,email,firstName,lastName,fullName,isAdmin,isVPEAdmin,isJasperAdmin,AppStream,ReportUser,defaultPassword,resetPassword,userLicenseName,preferredCurrency,schemaPrivileges,defaultSchema,roles,defaultRole,roleName,applicationList,prefix,suffix,jobTitle,department,city/town,state/province,county,countryCode,timezone";

    /**
     * Builds the service url
     *
     * @return string
     */
    // TODO : should be updated to a new approach without secret_key property
    public static String getServiceUrl() {
        String secretKey;

        try {
            secretKey = PropertiesContext.get("secret_key");
        } catch (IllegalArgumentException e) {
            secretKey = AwsParameterStoreUtil.getSystemParameter("/" + PropertiesContext.get("aws_parameter_store_name") + "/shared/environment-secret-key");
        }

        return PropertiesContext.get("cds.api_url").concat("/%s?key=").concat(secretKey);
    }
}
