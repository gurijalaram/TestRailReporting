package com.apriori.cds.api.utils;

import com.apriori.shared.util.http.utils.AwsParameterStoreUtil;
import com.apriori.shared.util.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Constants {

    public static final String SIGNIN_URL = "https://aprioritest.oktapreview.com/app/aprioritest_aprioritechnologies_1/exkuyvviKdbHl5IMa1d5/sso/sam";
    public static final String SIGNIN_CERT = "-----BEGIN CERTIFICATE-----MIIDpjCCAo6gAwIBAgIGAXN4tBWjMA0GCSqGSIb3DQEBCwUAMIGTMQswCQYDVQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2FuIEZyYW5jaXNjbzENMAsGA1UECgwET2t0YTEUMBIGA1UECwwLU1NPUHJvdmlkZXIxFDASBgNVBAMMC2Fwcmlvcml0ZXN0MRwwGgYJKoZIhvcNAQkBFg1pbmZvQG9rdGEuY29tMB4XDTIwMDcyMjIyNDQzNFoXDTMwMDcyMjIyNDUzNFowgZMxCzAJBgNVBAYTAlVTMRMwEQYDVQQIDApDYWxpZm9ybmlhMRYwFAYDVQQHDA1TYW4gRnJhbmNpc2NvMQ0wCwYDVQQKDARPa3RhMRQwEgYDVQQLDAtTU09Qcm92aWRlcjEUMBIGA1UEAwwLYXByaW9yaXRlc3QxHDAaBgkqhkiG9w0BCQEWDWluZm9Ab2t0YS5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC6bGJ+reDF+25VjGlhORysq9o13sZnEPeoIJ9LoQEmGvklVl8b1I50zYv4Pa7p6P8hZ/L2skhV2vCPIJw89mdQBElcQNsugWMP/wws8tmAd0tegn8wEOleSgm5NHiqNEr8Xk+uV6cqpDPITEaLXPBQ1NNgZPY4HA1x9Jyd9qAY3d26LcAEbII3VKx9Fzbgi65Z33ZuQaux87HyrUEfHYB+7pwt+MLBVZlFFzv/e/nkI8nZmY1wRy0kDMFwaMyvwGvl+UaYERzUiUK6KAmJEuLlRkBrc76Q3Vg5SgBDAczYGnNVi9okyB9eKPZfrJYrTuWmfl2I9LVwllNbhPzxkJxrAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAATXIZfcr93OtjT5l+925e35SuM4APUhl/c/qvmDtAoLimlHbX/9kKyLrh9ahf7F8WIX7QgnKLRWIUfWpkfcmVzQARHqWABPQVA0Hh3QvUVsmGInohq8DdRwh+1yz/ZC/lkGEACLRqN/PFH4ONQ90MM7EiWlzHXqb0tp5bNW0NcXjK0DfxL6AKYvdzooKc2tCTa7h9fhJD6WDenN4xZVkALSE96bGvF5zQkoLPQqE+xAIuaYd75Cpu9FLafSSc6eEEg3HpKbzGp0Ku5Du+UwqEatPhCrjc7y2tT5lOYHfsWboCCHmfn70oSMqaURZv2R1xoFRvL1P1apxxavljTHdiY=-----END CERTIFICATE-----";
    public static final String SAML_ATTRIBUTE_NAME_IDENTIFIER = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier";
    public static final String SAML_ATTRIBUTE_NAME_EMAIL = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress";
    public static final String SAML_ATTRIBUTE_NAME = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name";
    public static final String SAML_ATTRIBUTE_NAME_GIVEN_NAME = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name";
    public static final String SAML_ATTRIBUTE_NAME_FAMILY_NAME = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/surname";
    public static final String CLOUD_CUSTOMER = "CLOUD_ONLY";
    public static final String ON_PREM_CUSTOMER = "ON_PREMISE_ONLY";
    public static final String CDS_LICENSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><siteInfo objId=\"1\" class=\"com.fbc.datamodel.SiteInfo\">  <company value=\"%s\" />  <licenseModules objId=\"2\" class=\"java.util.TreeMap\">    <entry>      <key class=\"java.lang.String\" value=\"Access Control\" />      <value objId=\"3\" class=\"com.fbc.datamodel.LicenseModule\">        <expirationDate class=\"java.lang.Long\" value=\"1898467200000\" />        <name value=\"Access Control\" />        <signatureData value=\"302C02141D465701A3A02AA8D4EC762C6BF5386F2F869C6002143798DABDAEF0D1EAB24ACF36ABA6F3EFCA80F2BC\" />        <siteInfo refId=\"1\" />      </value>    </entry>    <entry>      <key class=\"java.lang.String\" value=\"All Manufacturing Processes\" />      <value objId=\"4\" class=\"com.fbc.datamodel.LicenseModule\">        <expirationDate class=\"java.lang.Long\" value=\"1898467200000\" />        <name value=\"All Manufacturing Processes\" />        <signatureData value=\"302C021439825EAFF126924A3D6B144C0C190D73E81B180E021423C8AE3F5DCA223F0B2F266A89A01096F83A5729\" />        <siteInfo refId=\"1\" />      </value>    </entry>    <entry>      <key class=\"java.lang.String\" value=\"Cost Model Workbench\" />      <value objId=\"5\" class=\"com.fbc.datamodel.LicenseModule\">        <expirationDate class=\"java.lang.Long\" value=\"1898467200000\" />        <name value=\"Cost Model Workbench\" />        <signatureData value=\"302C0214233401AB58D0758E48B22543CF9CA75EE7E864DD021451635DD3FC885AE4EBEB52B2C12CF626F5532C48\" />        <siteInfo refId=\"1\" />      </value>    </entry>    <entry>      <key class=\"java.lang.String\" value=\"MySQL Database Server\" />      <value objId=\"6\" class=\"com.fbc.datamodel.LicenseModule\">        <expirationDate class=\"java.lang.Long\" value=\"1898467200000\" />        <name value=\"MySQL Database Server\" />        <signatureData value=\"302D02140482C7488F026A40F4EF14F4674605BDCF019AD902150091589FA7BD4F35F415000954C67BEAB7C18C5759\" />        <siteInfo refId=\"1\" />      </value>    </entry>    <entry>      <key class=\"java.lang.String\" value=\"Parasolid CAD Connector\" />      <value objId=\"7\" class=\"com.fbc.datamodel.LicenseModule\">        <expirationDate class=\"java.lang.Long\" value=\"1898467200000\" />        <name value=\"Parasolid CAD Connector\" />        <signatureData value=\"302C02147B687BB624BA282EEA91D36A376B0E1590E4632F02146892DE4601E7CD2BBCCEEF9D64A5B5335874B5A9\" />        <siteInfo refId=\"1\" />      </value>    </entry>    <entry>      <key class=\"java.lang.String\" value=\"Standard CAD Connector\" />      <value objId=\"8\" class=\"com.fbc.datamodel.LicenseModule\">        <expirationDate class=\"java.lang.Long\" value=\"1898467200000\" />        <name value=\"Standard CAD Connector\" />        <signatureData value=\"302C021453FD20A1ECC31F3F8250ADBC3AEEF4199FD341BE0214240B6060FA18341CBF15830FA139C853611E1039\" />        <siteInfo refId=\"1\" />      </value>    </entry>    <entry>      <key class=\"java.lang.String\" value=\"VPE &amp; Scenario Sync\" />      <value objId=\"9\" class=\"com.fbc.datamodel.LicenseModule\">        <expirationDate class=\"java.lang.Long\" value=\"1898467200000\" />        <name value=\"VPE &amp; Scenario Sync\" />        <signatureData value=\"302C02141FF51189398CC8D78F923FA9F44D3A6CAE913AC602143788E6FF06A1A0BEC6EC8AC495D6541A7BC74755\" />        <siteInfo refId=\"1\" />      </value>    </entry>    <entry>      <key class=\"java.lang.String\" value=\"VPE Creator\" />      <value objId=\"10\" class=\"com.fbc.datamodel.LicenseModule\">        <expirationDate class=\"java.lang.Long\" value=\"1898467200000\" />        <name value=\"VPE Creator\" />        <signatureData value=\"302C0214752B4BAEC768662D68CD1AE2C11FE244CEDAE24E0214542F9C4B54603FE649A3D5B1081C39856CE16168\" />        <siteInfo refId=\"1\" />      </value>    </entry>    <entry>      <key class=\"java.lang.String\" value=\"aPriori PCBA USA\" />      <value objId=\"11\" class=\"com.fbc.datamodel.LicenseModule\">        <expirationDate class=\"java.lang.Long\" value=\"1898467200000\" />        <name value=\"aPriori PCBA USA\" />        <signatureData value=\"302B02130E5595EC11F5973A4BED91F137A23162F179FB02144BC40A87C832DA4358D3E5BCE8804233B38E71D4\" />        <siteInfo refId=\"1\" />      </value>    </entry>    <entry>      <key class=\"java.lang.String\" value=\"aPriori USA\" />      <value objId=\"12\" class=\"com.fbc.datamodel.LicenseModule\">        <expirationDate class=\"java.lang.Long\" value=\"1898467200000\" />        <name value=\"aPriori USA\" />        <signatureData value=\"302C021436EDBFEEEAA1F20A291F402D6C612A57B3DD180C021471D50B776D344D7B6946FB1FAB2A524301432638\" />        <siteInfo refId=\"1\" />      </value>    </entry>    <entry>      <key class=\"java.lang.String\" value=\"aPriori Wire Harness USA\" />      <value objId=\"13\" class=\"com.fbc.datamodel.LicenseModule\">        <expirationDate class=\"java.lang.Long\" value=\"1898467200000\" />        <name value=\"aPriori Wire Harness USA\" />        <signatureData value=\"302D02150089520F3C56374A3B2B0D90AD5B2C3BB863A170DA021413B24C134FEC588BF3EE17D5E4B8E03D86395DC9\" />        <siteInfo refId=\"1\" />      </value>    </entry>  </licenseModules>  <signatureData value=\"302C02150083EBAFB892CC3851268347BDBBAAB3F58C47B9E102137235095B5C10900B68FBFC40BDD784DA215514\" />  <siteId value=\"%s\" />  <userLicenses objId=\"14\" class=\"java.util.TreeMap\">    <entry>      <key class=\"java.lang.String\" value=\"28a43e98-6e8d-49e1-9019-ed485baa95f7\" />      <value objId=\"15\" class=\"com.fbc.datamodel.UserLicense\">        <expirationDate class=\"java.lang.Long\" value=\"1898467200000\" />        <licenseId value=\"%s\" />        <licenseModuleNames isNull=\"true\" />        <licenseName value=\"masterLicense\" />        <numPurchasedUsers value=\"1\" />        <numUsers value=\"1\" />        <signatureData value=\"302C02144A745616B14B2200EE60B32C7A7BD1423012E118021430B989C187020BD22DCA09449409FB891C1B90C3\" />        <siteInfo refId=\"1\" />      </value>    </entry>    <entry>      <key class=\"java.lang.String\" value=\"ec7cc170-466e-400f-bf49-982893426893\" />      <value objId=\"16\" class=\"com.fbc.datamodel.UserLicense\">        <expirationDate class=\"java.lang.Long\" value=\"1898467200000\" />        <licenseId value=\"%s\" />        <licenseModuleNames>\"Standard CAD Connector,Parasolid CAD Connector,aPriori USA,aPriori PCBA USA,aPriori Wire Harness USA,MySQL Database Server,Cost Model Workbench,VPE &amp; Scenario Sync,Access Control,All Manufacturing Processes\"</licenseModuleNames>        <licenseName value=\"Customer Sub License\" />        <numPurchasedUsers value=\"5\" />        <numUsers value=\"8\" />        <signatureData value=\"302C02140CF810A73F7A8212478DF1955576516EE746058C02144DEA10E0A0A60F6211B2FFB8DA6F58D3B9ABF4CB\" />        <siteInfo refId=\"1\" />      </value>    </entry>  </userLicenses>  <wsMode value=\"false\" /></siteInfo>";
    public static final String CDS_LICENSE_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><aPrioriSiteLicense company=\"%s\" aPrioriVersion=\"2020 R1\" expirationDate=\"02/28/2030\" renewalDate=\"02/28/2030\" numUsers=\"10\" numPurchasedUsers=\"5\">  <processGroups>    <processGroup name=\"VPE Creator\"/>    <processGroup name=\"Standard CAD Connector\"/>    <processGroup name=\"Parasolid CAD Connector\"/>    <processGroup name=\"aPriori USA\"/>    <processGroup name=\"aPriori PCBA USA\"/>    <processGroup name=\"aPriori Wire Harness USA\"/>    <processGroup name=\"MySQL Database Server\"/>    <processGroup name=\"Cost Model Workbench\"/>    <processGroup name=\"VPE &amp; Scenario Sync\"/>    <processGroup name=\"Access Control\"/>    <processGroup name=\"All Manufacturing Processes\"/>  </processGroups>  <userLicense numPurchasedUsers=\"1\" numUsers=\"1\" licenseName=\"masterLicense\"/>  <userLicense numPurchasedUsers=\"5\" numUsers=\"8\" licenseName=\"Customer Sub License\" processGroups=\"Standard CAD Connector,Parasolid CAD Connector,aPriori USA,aPriori PCBA USA,aPriori Wire Harness USA,MySQL Database Server,Cost Model Workbench,VPE &amp; Scenario Sync,Access Control,All Manufacturing Processes\"/></aPrioriSiteLicense>";
    public static final String USERS_BATCH = "loginID,email,firstName,lastName,fullName,isAdmin,isVPEAdmin,isJasperAdmin,AppStream,ReportUser,defaultPassword,resetPassword,userLicenseName,preferredCurrency,schemaPrivileges,defaultSchema,roles,defaultRole,roleName,applicationList,prefix,suffix,jobTitle,department,city/town,state/province,county,countryCode,timezone";

    /**
     * Get customer identity
     *
     * @return string
     */
    public static String getAPrioriInternalCustomerIdentity() {
        return PropertiesContext.get("customer_identity");
    }

    /**
     * Get identity application
     *
     * @return string
     */
    public static String getApProApplicationIdentity() {
        return PropertiesContext.get("cds.appro_application_identity");
    }

    /**
     * Get identity application
     *
     * @return string
     */
    public static String getCiaApplicationIdentity() {
        return PropertiesContext.get("cds.cia_application_identity");
    }

    /**
     * Get identity CIR application
     *
     * @return string
     */
    public static String getCirAppIdentity() {
        return PropertiesContext.get("cds.cir_application_identity");
    }

    /**
     * Get identity ACS application
     *
     * @return string
     */
    public static String getACSAppIdentity() {
        return PropertiesContext.get("cds.acs_application_identity");
    }

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

        return PropertiesContext.get("cds.api_url").concat("%s?key=").concat(secretKey);
    }
}
