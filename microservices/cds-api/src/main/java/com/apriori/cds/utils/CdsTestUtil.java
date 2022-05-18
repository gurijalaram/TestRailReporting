package com.apriori.cds.utils;

import com.apriori.apibase.services.cds.AttributeMappings;
import com.apriori.apibase.services.common.objects.IdentityProviderRequest;
import com.apriori.apibase.services.common.objects.IdentityProviderResponse;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.cds.entity.response.LicenseResponse;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.request.AccessControlRequest;
import com.apriori.cds.objects.request.AddDeployment;
import com.apriori.cds.objects.request.CustomAttributeRequest;
import com.apriori.cds.objects.request.License;
import com.apriori.cds.objects.request.LicenseRequest;
import com.apriori.cds.objects.response.AccessControlResponse;
import com.apriori.cds.objects.response.AssociationUserItems;
import com.apriori.cds.objects.response.CustomAttribute;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Customers;
import com.apriori.cds.objects.response.Deployment;
import com.apriori.cds.objects.response.InstallationItems;
import com.apriori.cds.objects.response.LicensedApplication;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.objects.response.SubLicenseAssociationUser;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.UserPreference;
import com.apriori.cds.objects.response.UserProfile;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CdsTestUtil extends TestUtil {
    /**
     * POST call to add a customer
     *
     * @param name           - the customer name
     * @param cloudReference - the cloud reference name
     * @param salesForceId   - the sales force id
     * @param email          - the email pattern
     * @return new object
     */
    public ResponseWrapper<Customer> addCustomer(String name, String customerType, String cloudReference, String salesForceId, String email) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.CUSTOMERS, Customer.class)
            .body("customer",
                Customer.builder().name(name)
                    .description("Add new customers api test")
                    .customerType(customerType)
                    .createdBy("#SYSTEM00000")
                    .cloudReference(cloudReference)
                    .salesforceId(salesForceId)
                    .active(true)
                    .mfaRequired(false)
                    .useExternalIdentityProvider(false)
                    .maxCadFileRetentionDays(1095)
                    .maxCadFileSize(51)
                    .emailRegexPatterns(Arrays.asList(email + ".com", email + ".co.uk"))
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Gets the special customer "aPriori Internal"
     *
     * @return The customer representing aPriori Internal
     */
    public Customer getAprioriInternal() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("name[EQ]", "aPriori Internal");

        Customer customer = findFirst(CDSAPIEnum.CUSTOMERS, Customers.class, filters, Collections.emptyMap());

        if (customer == null) {
            throw new IllegalStateException("Customer, aPriori Internal, is missing.  The data set is corrupted.");
        }

        return customer;
    }

    /**
     * POST call to add a customer
     *
     * @param customerIdentity - the customer id
     * @param userName         - the username
     * @param domain           - the customer name
     * @return new object
     */
    public ResponseWrapper<User> addUser(String customerIdentity, String userName, String domain) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.CUSTOMER_USERS, User.class)
            .inlineVariables(customerIdentity)
            .body("user",
                User.builder().username(userName)
                    .email(userName + "@" + domain + ".com")
                    .createdBy("#SYSTEM00000")
                    .active(true)
                    .userType("AP_CLOUD_USER")
                    .userProfile(UserProfile.builder().givenName(userName)
                        .familyName("Automater")
                        .jobTitle("Automation Engineer")
                        .department("Automation")
                        .supervisor("Ciene Frith")
                        .createdBy("#SYSTEM00000").build())
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * PATCH call to update a user
     *
     * @param user - the user
     * @return new object
     */
    public ResponseWrapper<User> patchUser(User user) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class)
            .inlineVariables(user.getCustomerIdentity(), user.getIdentity())
            .body("user",
                User.builder()
                    .identity(user.getIdentity())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .createdBy(user.getCreatedBy())
                    .active(user.getActive())
                    .userProfile(UserProfile.builder()
                        .department("Design Dept")
                        .supervisor("Moya Parker").build())
                    .build());

        return HTTPRequest.build(requestEntity).patch();
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
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.SITES_BY_CUSTOMER_ID, Site.class)
            .inlineVariables(customerIdentity)
            .body("site",
                Site.builder().name(siteName)
                    .description("Site created by automation test")
                    .siteId(siteID)
                    .createdBy("#SYSTEM00000")
                    .active(true)
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST call to add a deployment to a customer
     *
     * @param customerIdentity - the customer id
     * @param siteIdentity     - the site Identity
     * @return new object
     */
    public ResponseWrapper<Deployment> addDeployment(String customerIdentity, String deploymentName, String siteIdentity, String deploymentType) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.DEPLOYMENTS_BY_CUSTOMER_ID, Deployment.class)
            .inlineVariables(customerIdentity)
            .body("deployment",
                AddDeployment.builder()
                    .name(deploymentName)
                    .description("Deployment added by API automation")
                    .deploymentType(deploymentType)
                    .siteIdentity(siteIdentity)
                    .active("true")
                    .isDefault("true")
                    .createdBy("#SYSTEM00000")
                    .apVersion("2020 R1")
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST call to add an application to a site
     *
     * @param customerIdentity - the customer id
     * @param siteIdentity     - the site id
     * @return new object
     */
    public ResponseWrapper<LicensedApplication> addApplicationToSite(String customerIdentity, String siteIdentity, String appIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.APPLICATION_SITES_BY_CUSTOMER_SITE_IDS, LicensedApplication.class)
            .inlineVariables(customerIdentity, siteIdentity)
            .headers(new HashMap<String, String>() {
                {
                    put("Content-Type", "application/json");
                }
            })
            .body("licensedApplication",
                LicensedApplication.builder()
                    .applicationIdentity(appIdentity)
                    .createdBy("#SYSTEM00000")
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST call to add an installation to a customer
     *
     * @param customerIdentity   - the customer id
     * @param deploymentIdentity - the deployment id
     * @param siteIdentity       - the site Identity
     * @param realmKey           - the realm key
     * @param cloudReference     - the cloud reference
     * @return new object
     */
    public ResponseWrapper<InstallationItems> addInstallation(String customerIdentity, String deploymentIdentity, String realmKey, String cloudReference, String siteIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.INSTALLATIONS_BY_CUSTOMER_DEPLOYMENT_IDS, InstallationItems.class)
            .inlineVariables(customerIdentity, deploymentIdentity)
            .body("installation",
                InstallationItems.builder()
                    .name("Automation Installation")
                    .description("Installation added by API automation")
                    .active(true)
                    .region("na-1")
                    .realm(realmKey)
                    .url("https://na-1.qa.apriori.net")
                    .s3Bucket("apriori-qa-blue-fms")
                    .tenant("default")
                    .tenantGroup("default")
                    .clientId("apriori-web-cost")
                    .clientSecret("donotusethiskey")
                    .createdBy("#SYSTEM00000")
                    .cidGlobalKey("donotusethiskey")
                    .siteIdentity(siteIdentity)
                    .applications(Collections.singletonList(Constants.getApProApplicationIdentity()))
                    .cloudReference(cloudReference)
                    .apVersion("2020 R1")
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Patch installation
     *
     * @param customerIdentity     - the customer id
     * @param deploymentIdentity   - the deployment id
     * @param installationIdentity - the installation id
     * @return new object
     */
    public ResponseWrapper<InstallationItems> patchInstallation(String customerIdentity, String deploymentIdentity, String installationIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.INSTALLATION_BY_CUSTOMER_DEPLOYMENT_INSTALLATION_IDS, InstallationItems.class)
            .inlineVariables(customerIdentity, deploymentIdentity, installationIdentity)
            .body("installation",
                InstallationItems.builder()
                    .cloudReference("eu-1")
                    .build());

        return HTTPRequest.build(requestEntity).patch();

    }


    /**
     * POST call to add an apriori staff user association to a customer
     *
     * @param apCustomerIdentity  - the ap customer id
     * @param associationIdentity - the association id
     * @param userIdentity        - the aPriori Staff users identity
     * @return new object
     */
    public ResponseWrapper<AssociationUserItems> addAssociationUser(String apCustomerIdentity, String associationIdentity, String userIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.ASSOCIATIONS_BY_CUSTOMER_ASSOCIATIONS_IDS, AssociationUserItems.class)
            .inlineVariables(apCustomerIdentity, associationIdentity)
            .body("userAssociation",
                AssociationUserItems.builder()
                    .userIdentity(userIdentity)
                    .createdBy("#SYSTEM00000")
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST call to add a sub-license association user
     *
     * @param customerIdentity   - the customer id
     * @param siteIdentity       - the site id
     * @param licenseIdentity    - the license id
     * @param subLicenseIdentity - the sub-license id
     * @param userIdentity       - the user id
     * @return new object
     */
    public ResponseWrapper<SubLicenseAssociationUser> addSubLicenseAssociationUser(String customerIdentity, String siteIdentity, String licenseIdentity, String subLicenseIdentity, String userIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.SUBLICENSE_ASSOCIATIONS_USERS, SubLicenseAssociationUser.class)
            .inlineVariables(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity)
            .body("userAssociation",
                AssociationUserItems.builder()
                    .userIdentity(userIdentity)
                    .createdBy("#SYSTEM00000")
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to add SAML
     *
     * @param customerIdentity - the customer id
     * @param userIdentity     - the aPriori Staff users identity
     * @param customerName     - the customer name
     * @return new object
     */
    public ResponseWrapper<IdentityProviderResponse> addSaml(String customerIdentity, String userIdentity, String customerName) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.SAML_BY_CUSTOMER_ID, IdentityProviderResponse.class)
            .inlineVariables(customerIdentity)
            .body("identityProvider",
                IdentityProviderRequest.builder().contact(userIdentity)
                    .name(customerName + "-idp")
                    .displayName(customerName + "SAML")
                    .idpDomains(Collections.singletonList(customerName + ".com"))
                    .identityProviderPlatform("Azure AD")
                    .description("Create IDP using CDS automation")
                    .active(true)
                    .createdBy("#SYSTEM00000")
                    .signInUrl(Constants.SIGNIN_URL)
                    .signingCertificate(Constants.SIGNIN_CERT)
                    .signingCertificateExpiresAt("2030-07-22T22:45:45.245Z")
                    .signRequest(true)
                    .signRequestAlgorithm("RSA_SHA256")
                    .signRequestAlgorithmDigest("SHA256")
                    .protocolBinding("HTTP_POST")
                    .attributeMappings(AttributeMappings.builder()
                        .userId(Constants.SAML_ATTRIBUTE_NAME_IDENTIFIER)
                        .email(Constants.SAML_ATTRIBUTE_NAME_EMAIL)
                        .name(Constants.SAML_ATTRIBUTE_NAME)
                        .givenName(Constants.SAML_ATTRIBUTE_NAME_GIVEN_NAME)
                        .familyName(Constants.SAML_ATTRIBUTE_NAME_FAMILY_NAME).build())
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Patches and idp user
     *
     * @param customerIdentity - the customer id
     * @param idpIdentity      - the idp id
     * @param userIdentity     - the user id
     * @return new object
     */
    public ResponseWrapper<IdentityProviderResponse> patchIdp(String customerIdentity, String idpIdentity, String userIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.SAML_BY_CUSTOMER_PROVIDER_IDS, IdentityProviderResponse.class)
            .inlineVariables(customerIdentity, idpIdentity)
            .headers(new HashMap<String, String>() {
                {
                    put("Content-Type", "application/json");
                }
            })
            .body("identityProvider",
                IdentityProviderRequest.builder()
                    .description("patch IDP using Automation")
                    .contact(userIdentity)
                    .build());

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Post to add site license
     *
     * @param customerIdentity - the customer id
     * @param siteIdentity     - the site id
     * @param customerName     - the customer name
     * @param siteId           - the site id
     * @param licenseId        - the license id
     * @param subLicenseId     - the sublicense id
     * @return new object
     */
    public ResponseWrapper<LicenseResponse> addLicense(String customerIdentity, String siteIdentity, String customerName, String siteId, String licenseId, String subLicenseId) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.LICENSE_BY_CUSTOMER_SITE_IDS, LicenseResponse.class)
            .inlineVariables(customerIdentity, siteIdentity)
            .body(LicenseRequest.builder()
                .license(
                    License.builder()
                        .description("Test License")
                        .apVersion("2020 R1")
                        .createdBy("#SYSTEM00000")
                        .active("true")
                        .license(String.format(Constants.CDS_LICENSE, customerName, siteId, licenseId, subLicenseId))
                        .licenseTemplate(String.format(Constants.CDS_LICENSE_TEMPLATE, customerName))
                        .build())
                .build());

        return HTTPRequest.build(requestEntity).post();

    }

    /**
     * Post to add out of context access control
     *
     * @return new object
     */
    public ResponseWrapper<AccessControlResponse> addAccessControl(String customerIdentity, String userIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.ACCESS_CONTROLS, AccessControlResponse.class)
            .inlineVariables(customerIdentity, userIdentity)
            .body("accessControl",
                AccessControlRequest.builder()
                    .customerIdentity(Constants.getAPrioriInternalCustomerIdentity())
                    .deploymentIdentity(PropertiesContext.get("${env}.cds.apriori_production_deployment_identity"))
                    .installationIdentity(PropertiesContext.get("${env}.cds.apriori_core_services_installation_identity"))
                    .applicationIdentity(PropertiesContext.get("${env}.cds.apriori_cloud_home_identity"))
                    .createdBy("#SYSTEM00000")
                    .roleName("USER")
                    .roleIdentity(PropertiesContext.get("${env}.cds.identity_role"))
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Posts custom attribute
     *
     * @param customerIdentity - the customer id
     * @param userIdentity     - the user id
     * @return - new object
     */
    public ResponseWrapper<CustomAttribute> addCustomAttribute(String customerIdentity, String userIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.CUSTOM_ATTRIBUTES, CustomAttribute.class)
            .inlineVariables(customerIdentity, userIdentity)
            .body("customAttribute",
                CustomAttributeRequest.builder()
                    .key("department")
                    .name("department")
                    .value("TestDepartment")
                    .type("STRING")
                    .createdBy("#SYSTEM00000")
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Updates or adds custom attributes
     *
     * @param customerIdentity  - the customer id
     * @param userIdentity      - user id
     * @param updatedDepartment - updated department string
     * @return - new object
     */
    public ResponseWrapper<CustomAttribute> putCustomAttribute(String customerIdentity, String userIdentity, String updatedDepartment) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.CUSTOM_ATTRIBUTES, CustomAttribute.class)
            .inlineVariables(customerIdentity, userIdentity)
            .body("customAttribute",
                CustomAttributeRequest.builder()
                    .name("department")
                    .value(updatedDepartment)
                    .updatedBy("#SYSTEM00000")
                    .build());

        return HTTPRequest.build(requestEntity).put();
    }

    /**
     * Updates custom attribute
     *
     * @param customerIdentity  - the customer id
     * @param userIdentity      - user id
     * @param attributeIdentity - attribute id
     * @param updatedDepartment -  updated department string
     * @return - new object
     */
    public ResponseWrapper<CustomAttribute> updateAttribute(String customerIdentity, String userIdentity, String attributeIdentity, String updatedDepartment) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.CUSTOM_ATTRIBUTE_BY_ID, CustomAttribute.class)
            .inlineVariables(customerIdentity, userIdentity, attributeIdentity)
            .body("customAttribute",
                CustomAttributeRequest.builder()
                    .value(updatedDepartment)
                    .updatedBy("#SYSTEM00000")
                    .build());

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Adds new user preference
     *
     * @param customerIdentity - customer id
     * @param userIdentity - user id
     * @return - new object
     */
    public ResponseWrapper<UserPreference> addUserPreference(String customerIdentity, String userIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.USER_PREFERENCES, UserPreference.class)
            .inlineVariables(customerIdentity, userIdentity)
            .body("userPreference",
                UserPreference.builder()
                    .name("Test")
                    .value("1234")
                    .type("STRING")
                    .createdBy("#SYSTEM00000")
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Updates existing user preference
     *
     * @param customerIdentity - customer Id
     * @param userIdentity - user Id
     * @param updatedPreference - value of updated preference
     * @return -  new object
     */
    public ResponseWrapper<UserPreference> updatePreference(String customerIdentity, String userIdentity, String preferenceIdentity, String updatedPreference) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.PREFERENCE_BY_ID, UserPreference.class)
            .inlineVariables(customerIdentity, userIdentity, preferenceIdentity)
            .body("userPreference",
                UserPreference.builder()
                    .value(updatedPreference)
                    .updatedBy("#SYSTEM00000")
                    .build());

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Adds or replaces user preferences
     *
     * @param customerIdentity - customer id
     * @param userIdentity - user id
     * @param preferenceName - preference name
     * @return new object
     */
    public ResponseWrapper<UserPreference> putUserPreference(String customerIdentity, String userIdentity, String preferenceName) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.PREFERENCE_BY_ID, UserPreference.class)
            .inlineVariables(customerIdentity, userIdentity, preferenceName)
            .body("userPreference",
                UserPreference.builder()
                    .value("6548")
                    .type("INTEGER")
                    .createdBy("#SYSTEM00000")
                    .build());

        return HTTPRequest.build(requestEntity).put();
    }
}
