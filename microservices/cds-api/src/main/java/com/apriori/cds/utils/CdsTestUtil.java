package com.apriori.cds.utils;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;

import com.apriori.cds.enums.CASCustomerEnum;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.request.AccessAuthorizationRequest;
import com.apriori.cds.models.request.AccessControlRequest;
import com.apriori.cds.models.request.ActivateLicense;
import com.apriori.cds.models.request.ActivateLicenseRequest;
import com.apriori.cds.models.request.AddDeployment;
import com.apriori.cds.models.request.ApplicationInstallationRequest;
import com.apriori.cds.models.request.CASCustomerRequest;
import com.apriori.cds.models.request.CustomAttributeRequest;
import com.apriori.cds.models.request.FeatureRequest;
import com.apriori.cds.models.request.License;
import com.apriori.cds.models.request.LicenseRequest;
import com.apriori.cds.models.request.PostBatch;
import com.apriori.cds.models.request.UpdateCredentials;
import com.apriori.cds.models.response.AccessAuthorization;
import com.apriori.cds.models.response.AccessControlResponse;
import com.apriori.cds.models.response.AssociationUserItems;
import com.apriori.cds.models.response.AttributeMappings;
import com.apriori.cds.models.response.CredentialsItems;
import com.apriori.cds.models.response.CustomAttribute;
import com.apriori.cds.models.response.ErrorResponse;
import com.apriori.cds.models.response.FeatureResponse;
import com.apriori.cds.models.response.IdentityProviderRequest;
import com.apriori.cds.models.response.IdentityProviderResponse;
import com.apriori.cds.models.response.InstallationItems;
import com.apriori.cds.models.response.LicenseResponse;
import com.apriori.cds.models.response.Roles;
import com.apriori.cds.models.response.SubLicenseAssociationUser;
import com.apriori.cds.models.response.User;
import com.apriori.cds.models.response.UserPreference;
import com.apriori.cds.models.response.UserProfile;
import com.apriori.cds.models.response.UserRole;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.MultiPartFiles;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.http.utils.TestUtil;
import com.apriori.json.JsonManager;
import com.apriori.models.response.Customer;
import com.apriori.models.response.Customers;
import com.apriori.models.response.Deployment;
import com.apriori.models.response.Enablements;
import com.apriori.models.response.Features;
import com.apriori.models.response.LicensedApplications;
import com.apriori.models.response.Site;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
            .expectedResponseCode(HttpStatus.SC_CREATED)
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
     * Creates customer via CAS API with service accounts
     *
     * @param name           - the customer name
     * @param cloudReference - the cloud reference name
     * @param email          - the email pattern
     * @return new object
     */
    public ResponseWrapper<Customer> addCASCustomer(String name, String cloudReference, String email, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(CASCustomerEnum.CUSTOMERS, Customer.class)
            .token(currentUser.getToken())
            .body("customer",
                CASCustomerRequest.builder().name(name)
                    .cloudReference(cloudReference)
                    .description("Add new customers api test")
                    .salesforceId(new GenerateStringUtil().generateSalesForceId())
                    .customerType("CLOUD_ONLY")
                    .active(true)
                    .mfaRequired(true)
                    .mfaAuthenticator("ONE_TIME_PASSWORD")
                    .useExternalIdentityProvider(false)
                    .maxCadFileRetentionDays(584)
                    .maxCadFileSize(51)
                    .emailDomains(Arrays.asList(email + ".com", email + ".co.uk"))
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Updates customer by customer id
     *
     * @param customerIdentity    - customer identity
     * @param updatedEmailPattern - new value of email pattern
     * @return new object
     */
    public ResponseWrapper<Customer> updateCustomer(String customerIdentity, String updatedEmailPattern) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.CUSTOMER_BY_ID, Customer.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_OK)
            .headers(new HashMap<String, String>() {
                {
                    put("Content-Type", "application/json");
                }
            })
            .body("customer",
                Customer.builder()
                    .emailRegexPatterns(Arrays.asList(updatedEmailPattern + ".com", updatedEmailPattern + ".co.uk"))
                    .build());

        return HTTPRequest.build(requestEntity).patch();
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
            .expectedResponseCode(HttpStatus.SC_CREATED)
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
            .expectedResponseCode(HttpStatus.SC_OK)
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
     * PATCH call to update the user credentials
     *
     * @param customerIdentity    - the customer id
     * @param userIdentity        - the user id
     * @param passwordHashCurrent - current hash password
     * @param passwordSalt        - the salt password
     * @return new object
     */
    public ResponseWrapper<CredentialsItems> updateUserCredentials(String customerIdentity, String userIdentity, String passwordHashCurrent, String passwordSalt) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.USER_CREDENTIALS_BY_CUSTOMER_USER_IDS, CredentialsItems.class)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(HttpStatus.SC_OK)
            .body("userCredential",
                UpdateCredentials.builder()
                    .currentPasswordHash(passwordHashCurrent)
                    .newPasswordHash(new GenerateStringUtil().getHashPassword())
                    .newPasswordSalt(passwordSalt)
                    .newEncryptedPassword(new GenerateStringUtil().getRandomString().toLowerCase())
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
            .expectedResponseCode(HttpStatus.SC_CREATED)
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
            .expectedResponseCode(HttpStatus.SC_CREATED)
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
    public ResponseWrapper<LicensedApplications> addApplicationToSite(String customerIdentity, String siteIdentity, String appIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.APPLICATION_SITES_BY_CUSTOMER_SITE_IDS, LicensedApplications.class)
            .inlineVariables(customerIdentity, siteIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .headers(new HashMap<String, String>() {
                {
                    put("Content-Type", "application/json");
                }
            })
            .body("licensedApplication",
                LicensedApplications.builder()
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
    public ResponseWrapper<InstallationItems> addInstallation(String customerIdentity, String deploymentIdentity, String name, String realmKey, String cloudReference, String siteIdentity, Boolean highMem) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.INSTALLATIONS_BY_CUSTOMER_DEPLOYMENT_IDS, InstallationItems.class)
            .inlineVariables(customerIdentity, deploymentIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("installation",
                InstallationItems.builder()
                    .name(name)
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
                    .cloudReference(cloudReference)
                    .apVersion("2023 R1")
                    .highMem(highMem)
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST call to add an installation with feature to a customer
     *
     * @param customerIdentity              - the customer id
     * @param deploymentIdentity            - the deployment id
     * @param siteIdentity                  - the site Identity
     * @param realmKey                      - the realm key
     * @param cloudReference                - the cloud reference
     * @param workOrderStatusUpdatesEnabled - boolean for feature
     * @return new object
     */
    public ResponseWrapper<InstallationItems> addInstallationWithFeature(String customerIdentity, String deploymentIdentity, String realmKey, String cloudReference, String siteIdentity, Boolean workOrderStatusUpdatesEnabled, Boolean bulkCostingEnabled) {
        InstallationItems installationItems = JsonManager.deserializeJsonFromInputStream(
            FileResourceUtil.getResourceFileStream("InstallationItems" + ".json"), InstallationItems.class);
        installationItems.setRealm(realmKey);
        installationItems.setSiteIdentity(siteIdentity);
        installationItems.setCloudReference(cloudReference);
        installationItems.setHighMem(false);
        installationItems.setFeatures(Features.builder().workOrderStatusUpdatesEnabled(workOrderStatusUpdatesEnabled).bulkCostingEnabled(bulkCostingEnabled).build());

        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.INSTALLATIONS_BY_CUSTOMER_DEPLOYMENT_IDS, InstallationItems.class)
            .inlineVariables(customerIdentity, deploymentIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("installation", installationItems);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST call to add a feature to Installation
     *
     * @return new object
     */
    public ResponseWrapper<FeatureResponse> addFeature(String customerIdentity, String deploymentIdentity, String installationIdentity, Boolean workOrderStatusUpdatesEnabled, Boolean bulkCostingEnabled) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.INSTALLATION_FEATURES, FeatureResponse.class)
            .inlineVariables(customerIdentity, deploymentIdentity, installationIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(FeatureRequest.builder()
                .features(Features.builder()
                    .workOrderStatusUpdatesEnabled(workOrderStatusUpdatesEnabled)
                    .bulkCostingEnabled(bulkCostingEnabled)
                    .build())
                .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST call trying to add invalid feature to Installation
     *
     * @return ErrorResponse
     */
    public ErrorResponse addFeatureWrongResponse(String customerIdentity, String deploymentIdentity, String installationIdentity, Boolean workOrderStatusUpdatesEnabled, Boolean bulkCostingEnabled) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.INSTALLATION_FEATURES, ErrorResponse.class)
            .inlineVariables(customerIdentity, deploymentIdentity, installationIdentity)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
            .body(FeatureRequest.builder()
                .features(Features.builder()
                    .workOrderStatusUpdatesEnabled(workOrderStatusUpdatesEnabled)
                    .bulkCostingEnabled(bulkCostingEnabled)
                    .build())
                .build());

        ResponseWrapper<ErrorResponse> errorResponse = HTTPRequest.build(requestEntity).post();

        return errorResponse.getResponseEntity();
    }

    /**
     * PUT call to update a feature to Installation
     *
     * @return new object
     */
    public ResponseWrapper<FeatureResponse> updateFeature(String customerIdentity, String deploymentIdentity, String installationIdentity, boolean workOrderStatusUpdatesEnabled) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.INSTALLATION_FEATURES, FeatureResponse.class)
            .inlineVariables(customerIdentity, deploymentIdentity, installationIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(FeatureRequest.builder()
                .features(Features.builder()
                    .workOrderStatusUpdatesEnabled(workOrderStatusUpdatesEnabled)
                    .build())
                .build());

        return HTTPRequest.build(requestEntity).put();
    }

    /**
     * PUT call to update a feature to Installation - wrong response
     *
     * @return new ErrorResponse
     */
    public ErrorResponse updateFeatureWrongResponse(String customerIdentity, String deploymentIdentity, String installationIdentity, boolean workOrderStatusUpdatesEnabled) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.INSTALLATION_FEATURES, ErrorResponse.class)
            .inlineVariables(customerIdentity, deploymentIdentity, installationIdentity)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
            .body(FeatureRequest.builder()
                .features(Features.builder()
                    .workOrderStatusUpdatesEnabled(workOrderStatusUpdatesEnabled)
                    .build())
                .build());

        ResponseWrapper<ErrorResponse> errorResponse = HTTPRequest.build(requestEntity).put();

        return errorResponse.getResponseEntity();
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
            .expectedResponseCode(HttpStatus.SC_OK)
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
            .expectedResponseCode(HttpStatus.SC_CREATED)
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
            .expectedResponseCode(HttpStatus.SC_CREATED)
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
            .expectedResponseCode(HttpStatus.SC_CREATED)
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
                    .authenticationType("IDENTITY_PROVIDER_INITIATED_SSO")
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
            .expectedResponseCode(HttpStatus.SC_OK)
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
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(LicenseRequest.builder()
                .license(
                    License.builder()
                        .description("Test License")
                        .apVersion("2020 R1")
                        .createdBy("#SYSTEM00000")
                        .active("false")
                        .license(String.format(Constants.CDS_LICENSE, customerName, siteId, licenseId, subLicenseId))
                        .licenseTemplate(String.format(Constants.CDS_LICENSE_TEMPLATE, customerName))
                        .build())
                .build());

        return HTTPRequest.build(requestEntity).post();

    }

    /**
     * Post request to activate license
     *
     * @param customerIdentity - the customer id
     * @param siteIdentity     - the site id
     * @param licenseIdentity  - the license identity
     * @param userIdentity     - the user identity
     */
    public void activateLicense(String customerIdentity, String siteIdentity, String licenseIdentity, String userIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.LICENSE_ACTIVATE, LicenseResponse.class)
            .inlineVariables(customerIdentity, siteIdentity, licenseIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(ActivateLicenseRequest.builder()
                .license(ActivateLicense.builder()
                    .active(true)
                    .updatedBy(userIdentity)
                    .build())
                .build());

        HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to add out of context access control
     *
     * @return new object
     */
    public ResponseWrapper<AccessControlResponse> addAccessControl(String customerIdentity, String userIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.ACCESS_CONTROLS, AccessControlResponse.class)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("accessControl",
                AccessControlRequest.builder()
                    .customerIdentity(Constants.getAPrioriInternalCustomerIdentity())
                    .deploymentIdentity(PropertiesContext.get("cds.apriori_production_deployment_identity"))
                    .installationIdentity(PropertiesContext.get("cds.apriori_core_services_installation_identity"))
                    .applicationIdentity(PropertiesContext.get("cds.ap_workspace_application_identity"))
                    .createdBy("#SYSTEM00000")
                    .roleName("USER")
                    .roleIdentity(PropertiesContext.get("cds.identity_role"))
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
            .expectedResponseCode(HttpStatus.SC_CREATED)
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
            .expectedResponseCode(HttpStatus.SC_CREATED)
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
            .expectedResponseCode(HttpStatus.SC_CREATED)
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
     * @param userIdentity     - user id
     * @return - new object
     */
    public ResponseWrapper<UserPreference> addUserPreference(String customerIdentity, String userIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.USER_PREFERENCES, UserPreference.class)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
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
     * @param customerIdentity  - customer Id
     * @param userIdentity      - user Id
     * @param updatedPreference - value of updated preference
     * @return -  new object
     */
    public ResponseWrapper<UserPreference> updatePreference(String customerIdentity, String userIdentity, String preferenceIdentity, String updatedPreference) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.PREFERENCE_BY_ID, UserPreference.class)
            .inlineVariables(customerIdentity, userIdentity, preferenceIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
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
     * @param userIdentity     - user id
     * @param preferenceName   - preference name
     * @return new object
     */
    public ResponseWrapper<UserPreference> putUserPreference(String customerIdentity, String userIdentity, String preferenceName) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.PREFERENCE_BY_ID, UserPreference.class)
            .inlineVariables(customerIdentity, userIdentity, preferenceName)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("userPreference",
                UserPreference.builder()
                    .value("6548")
                    .type("INTEGER")
                    .createdBy("#SYSTEM00000")
                    .build());

        return HTTPRequest.build(requestEntity).put();
    }

    /**
     * @param customerIdentity customer id
     * @param userIdentity     user id
     * @param serviceAccount   service account
     * @return new object
     */
    public ResponseWrapper<AccessAuthorization> addAccessAuthorization(String customerIdentity, String userIdentity, String serviceAccount) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.ACCESS_AUTHORIZATIONS, AccessAuthorization.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("accessAuthorization",
                AccessAuthorizationRequest.builder()
                    .userIdentity(userIdentity)
                    .serviceAccount(serviceAccount)
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param customerIdentity     customer id
     * @param deploymentIdentity   deployment id
     * @param installationIdentity installation id
     * @param appIdentity          application id
     * @param siteIdentity         site id
     * @return new object
     */
    public ResponseWrapper<InstallationItems> addApplicationInstallation(String customerIdentity, String deploymentIdentity, String installationIdentity, String appIdentity, String siteIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.APPLICATION_INSTALLATION, InstallationItems.class)
            .inlineVariables(customerIdentity, deploymentIdentity, installationIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("installation",
                ApplicationInstallationRequest.builder()
                    .applicationIdentity(appIdentity)
                    .siteIdentity(siteIdentity)
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Uploads batch users file via CAS API
     *
     * @param users            - string of users file
     * @param email            - email
     * @param customerIdentity - customerIdentity
     * @return new object
     */
    public ResponseWrapper<PostBatch> addCASBatchFile(String users, String email, String customerIdentity, UserCredentials currentUser) {

        StringBuilder sb = new StringBuilder(users);
        String userRecord = "User%s,user%s@%s.com,Test%s,User%s,,,,,,,,,,,,,,,,,,,,,,,,,";
        for (int i = 0; i < 23; i++) {
            sb.append(String.format(userRecord, i, i, email, i, i));
        }

        InputStream usersBatch = new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));

        RequestEntity requestEntity = RequestEntityUtil.init(CASCustomerEnum.CUSTOMERS_BATCH, PostBatch.class)
            .token(currentUser.getToken())
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .multiPartFiles(new MultiPartFiles().use("multiPartFile", FileResourceUtil.copyIntoTempFile(usersBatch, null, "testUsersBatch.csv")))
            .inlineVariables(customerIdentity);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Uploads batch users file via CAS API
     *
     * @param customerIdentity - customerIdentity
     * @param fileName         - name of file
     * @return new object
     */
    public ResponseWrapper<PostBatch> addInvalidBatchFile(String customerIdentity, String fileName, UserCredentials currentUser) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASCustomerEnum.CUSTOMERS_BATCH, PostBatch.class)
            .token(currentUser.getToken())
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .multiPartFiles(new MultiPartFiles().use("multiPartFile", FileResourceUtil.getResourceAsFile(fileName)))
            .inlineVariables(customerIdentity);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Creates role for a user
     *
     * @param customerIdentity - customer identity
     * @param userIdentity     - user identity
     * @return new object
     */
    public ResponseWrapper<UserRole> createRoleForUser(String customerIdentity, String userIdentity, String role) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.USER_ROLES, UserRole.class)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("role",
                UserRole.builder()
                    .role(role)
                    .createdBy("#SYSTEM00000")
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Creates invalid role for a user and get error response
     *
     * @param customerIdentity - customer identity
     * @param userIdentity     - user identity
     * @return new object
     */
    public ErrorResponse createInvalidRoleForUser(String customerIdentity, String userIdentity, String role) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.USER_ROLES, ErrorResponse.class)
            .inlineVariables(customerIdentity, userIdentity)
            .body("role",
                UserRole.builder()
                    .role(role)
                    .createdBy("#SYSTEM00000")
                    .build())
            .expectedResponseCode(SC_NOT_FOUND);
        ResponseWrapper<ErrorResponse> errorResponse = HTTPRequest.build(requestEntity).post();

        return errorResponse.getResponseEntity();
    }

    /**
     * call the endpoint /roles with param pageSize=20 - to get all roles
     *
     * @param inlineVariables
     * @return object ResponseWrapper
     */
    public Roles getRoles(String... inlineVariables) {
        final RequestEntity requestEntity = RequestEntityUtil
            .init(CDSAPIEnum.ROLES, Roles.class)
            .inlineVariables(inlineVariables)
            .queryParams(new QueryParams().use("pageSize", "20"))
            .expectedResponseCode(HttpStatus.SC_OK);
        return (Roles) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Creates or updates user enablements
     *
     * @param customerIdentity - customer identity
     * @param userIdentity - user identity
     * @param customerAssignedRole - customerAssignedRole
     * @param highMem - true or false
     * @return new object
     */
    public ResponseWrapper<Enablements> createUpdateEnablements(String customerIdentity, String userIdentity, String customerAssignedRole, Boolean highMem) {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class)
            .inlineVariables(customerIdentity, userIdentity)
            .body("enablements",
                Enablements.builder()
                     .customerAssignedRole(customerAssignedRole)
                     .highMemEnabled(highMem)
                     .createdBy("#SYSTEM00000")
                     .updatedBy("#SYSTEM00000")
                     .build())
            .expectedResponseCode(SC_CREATED);

        return HTTPRequest.build(requestEntity).put();
    }
}
