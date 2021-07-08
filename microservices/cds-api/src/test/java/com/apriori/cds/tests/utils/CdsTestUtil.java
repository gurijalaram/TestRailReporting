package com.apriori.cds.tests.utils;

import com.apriori.apibase.services.cds.AttributeMappings;
import com.apriori.apibase.services.common.objects.IdentityProviderRequest;
import com.apriori.apibase.services.common.objects.IdentityProviderResponse;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.cds.entity.response.LicenseResponse;
import com.apriori.cds.objects.request.AccessControlRequest;
import com.apriori.cds.objects.request.AddDeployment;
import com.apriori.cds.objects.request.License;
import com.apriori.cds.objects.request.LicenseRequest;
import com.apriori.cds.objects.response.AccessControlResponse;
import com.apriori.cds.objects.response.AssociationUserItems;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Deployment;
import com.apriori.cds.objects.response.InstallationItems;
import com.apriori.cds.objects.response.LicensedApplication;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.objects.response.SubLicenseAssociationUser;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.UserProfile;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import java.util.Arrays;

public class CdsTestUtil extends TestUtil {

    private String url;
    private String serviceUrl = Constants.getServiceUrl();

    /**
     * @param url   - the url
     * @param klass - the class
     * @param <T>   - generic object
     * @return generic object
     */
    public <T> ResponseWrapper<T> getCommonRequest(String url, Class klass) {
        return GenericRequestUtil.get(
            RequestEntity.init(url, klass).setUrlEncodingEnabled(true),
            new RequestAreaApi()
        );
    }

    /**
     * Calls the delete method
     *
     * @param deleteEndpoint - the endpoint to delete
     * @return responsewrapper
     */
    public ResponseWrapper<String> delete(String deleteEndpoint) {
        RequestEntity requestEntity = RequestEntity.init(deleteEndpoint, null);

        return GenericRequestUtil.delete(requestEntity, new RequestAreaApi());
    }

    /**
     * POST call to add a customer
     *
     * @param name           - the customer name
     * @param cloudReference - the cloud reference name
     * @param salesForceId   - the sales force id
     * @param email          - the email pattern
     * @return new object
     */
    public ResponseWrapper<Customer> addCustomer(String name, String cloudReference, String salesForceId, String email) {
        url = String.format(serviceUrl, "customers");

        RequestEntity requestEntity = RequestEntity.init(url, Customer.class)
            .setBody("customer",
                Customer.builder().name(name)
                    .description("Add new customers api test")
                    .customerType("CLOUD_ONLY")
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

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * POST call to add a customer
     *
     * @param customerIdentity - the customer id
     * @param userName         - the user name
     * @param customerName     - the customer name
     * @return new object
     */
    public ResponseWrapper<User> addUser(String customerIdentity, String userName, String customerName) {
        url = String.format(serviceUrl, String.format("customers/%s/users", customerIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, User.class)
            .setBody("user",
                User.builder().username(userName)
                    .email(userName + "@" + customerName + ".com")
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

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * PATCH call to update a user
     *
     * @param customerIdentity - the customer id
     * @param userIdentity     - the user id
     * @return new object
     */
    public ResponseWrapper<User> patchUser(String customerIdentity, String userIdentity) {
        url = String.format(serviceUrl, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, User.class)
            .setBody("user",
                User.builder()
                    .userProfile(UserProfile.builder()
                        .department("Design Dept")
                        .supervisor("Moya Parker").build())
                    .build());

        return GenericRequestUtil.patch(requestEntity, new RequestAreaApi());
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
        url = String.format(serviceUrl, String.format("customers/%s/sites", customerIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, Site.class)
            .setBody("site",
                Site.builder().name(siteName)
                    .description("Site created by automation test")
                    .siteId(siteID)
                    .createdBy("#SYSTEM00000")
                    .active(true)
                    .build());

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * POST call to add a deployment to a customer
     *
     * @param customerIdentity - the customer id
     * @param siteIdentity     - the site Identity
     * @return new object
     */
    public ResponseWrapper<Deployment> addDeployment(String customerIdentity, String deploymentName, String siteIdentity, String deploymentType) {
        url = String.format(serviceUrl, String.format("customers/%s/deployments", customerIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, Deployment.class)
            .setBody("deployment",
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

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * POST call to add an application to a site
     *
     * @param customerIdentity - the customer id
     * @param siteIdentity     - the site id
     * @return new object
     */
    public ResponseWrapper<LicensedApplication> addApplicationToSite(String customerIdentity, String siteIdentity) {
        url = String.format(serviceUrl, String.format("customers/%s/sites/%s/licensed-applications", customerIdentity, siteIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, LicensedApplication.class)
            .setHeaders("Content-Type", "application/json")
            .setBody("licensedApplication",
                LicensedApplication.builder()
                    .applicationIdentity(Constants.getApProApplicationIdentity())
                    .createdBy("#SYSTEM00000")
                    .build());

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
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
        url = String.format(serviceUrl, String.format("customers/%s/deployments/%s/installations", customerIdentity, deploymentIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, InstallationItems.class)
            .setBody("installation",
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
                    .applications(Arrays.asList(Constants.getApProApplicationIdentity()))
                    .cloudReference(cloudReference)
                    .build());

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
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
        url = String.format(serviceUrl, String.format("customers/%s/deployments/%s/installations/%s", customerIdentity, deploymentIdentity, installationIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, InstallationItems.class)
            .setBody("installation",
                InstallationItems.builder()
                    .cloudReference("eu-1")
                    .build());

        return GenericRequestUtil.patch(requestEntity, new RequestAreaApi());
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
        url = String.format(serviceUrl, String.format("customers/%s/customer-associations/%s/customer-association-users", apCustomerIdentity, associationIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, AssociationUserItems.class)
            .setBody("userAssociation",
                AssociationUserItems.builder()
                    .userIdentity(userIdentity)
                    .createdBy("#SYSTEM00000")
                    .build());

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * POST call to add sub license association user
     *
     * @param customerIdentity   - the customer id
     * @param siteIdentity       - the site id
     * @param licenseIdentity    - the license id
     * @param subLicenseIdentity - the sub license id
     * @param userIdentity       - the user id
     * @return new object
     */
    public ResponseWrapper<SubLicenseAssociationUser> addSubLicenseAssociationUser(String customerIdentity, String siteIdentity, String licenseIdentity, String subLicenseIdentity, String userIdentity) {
        url = String.format(serviceUrl, String.format("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users", customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, SubLicenseAssociationUser.class)
            .setBody("userAssociation",
                AssociationUserItems.builder()
                    .userIdentity(userIdentity)
                    .createdBy("#SYSTEM00000")
                    .build());

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
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
        url = String.format(serviceUrl, String.format("customers/%s/identity-providers", customerIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, IdentityProviderResponse.class)
            .setBody("identityProvider",
                IdentityProviderRequest.builder().contact(userIdentity)
                    .name(customerName + "-idp")
                    .displayName(customerName + "SAML")
                    .idpDomains(Arrays.asList(customerName + ".com"))
                    .identityProviderPlatform("Azure AD")
                    .description("Create IDP using CDS automation")
                    .active(true)
                    .createdBy("#SYSTEM00000")
                    .signInUrl(Constants.getSignInUrl())
                    .signingCertificate(Constants.getSignInCert())
                    .signingCertificateExpiresAt("2030-07-22T22:45Z")
                    .signRequest(true)
                    .signRequestAlgorithm("RSA_SHA256")
                    .signRequestAlgorithmDigest("SHA256")
                    .protocolBinding("HTTP_POST")
                    .attributeMappings(AttributeMappings.builder()
                        .userId(Constants.getSamlNameIdentifier())
                        .email(Constants.getSamlAttributeEmail())
                        .name(Constants.getSamlAttributeName())
                        .givenName(Constants.getSamlAttributeGivenName())
                        .familyName(Constants.getSamlAttributeFamilyName()).build())
                    .build());

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
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
        url = String.format(serviceUrl, String.format("customers/%s/identity-providers/%s", customerIdentity, idpIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, IdentityProviderResponse.class)
            .setHeaders("Content-Type", "application/json")
            .setBody("identityProvider",
                IdentityProviderRequest.builder()
                    .description("patch IDP using Automation")
                    .contact(userIdentity)
                    .build());
        return GenericRequestUtil.patch(requestEntity, new RequestAreaApi());
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
        url = String.format(serviceUrl, String.format("customers/%s/sites/%s/licenses", customerIdentity, siteIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, LicenseResponse.class)
            .setBody(LicenseRequest.builder()
                .license(
                    License.builder()
                        .description("Test License")
                        .apVersion("2020 R1")
                        .createdBy("#SYSTEM00000")
                        .active("true")
                        .license(String.format(Constants.getLicense(), customerName, siteId, licenseId, subLicenseId))
                        .licenseTemplate(String.format(Constants.getLicenseTemplate(), customerName))
                        .build())
                .build());

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * Post to add out of context access control
     *
     * @return new object
     */
    public ResponseWrapper<AccessControlResponse> addAccessControl(String customerIdentity, String userIdentity) {
        url = String.format(serviceUrl, String.format("customers/%s/users/%s/access-controls", customerIdentity, userIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, AccessControlResponse.class)
            .setBody("accessControl",
                AccessControlRequest.builder()
                    .customerIdentity(Constants.getAPrioriInternalCustomerIdentity())
                    .deploymentIdentity(Constants.getApProductionDeploymentIdentity())
                    .installationIdentity(Constants.getApCoreInstallationIdentity())
                    .applicationIdentity(Constants.getApCloudHomeApplicationIdentity())
                    .createdBy("#SYSTEM00000")
                    .roleName("USER")
                    .roleIdentity(Constants.getCdsIdentityRole())
                    .build());

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }
}
