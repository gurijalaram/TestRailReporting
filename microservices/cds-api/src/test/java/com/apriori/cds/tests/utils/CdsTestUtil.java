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
                new User().setUsername(userName)
                    .setEmail(userName + "@" + customerName + ".com")
                    .setCreatedBy("#SYSTEM00000")
                    .setActive(true)
                    .setUserType("AP_CLOUD_USER")
                    .setUserProfile(new UserProfile().setGivenName(userName)
                        .setFamilyName("Automater")
                        .setJobTitle("Automation Engineer")
                        .setDepartment("Automation")
                        .setSupervisor("Ciene Frith")
                        .setCreatedBy("#SYSTEM00000")));

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
                new User()
                    .setUserProfile(new UserProfile()
                        .setDepartment("Design Dept")
                        .setSupervisor("Moya Parker")));

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
    public ResponseWrapper<Deployment> addDeployment(String customerIdentity, String siteIdentity, String deploymentType) {
        url = String.format(serviceUrl, String.format("customers/%s/deployments", customerIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, Deployment.class)
            .setBody("deployment",
                new AddDeployment().setName("Production Deployment")
                    .setDescription("Deployment added by API automation")
                    .setDeploymentType(deploymentType)
                    .setSiteIdentity(siteIdentity)
                    .setActive("true")
                    .setIsDefault("true")
                    .setCreatedBy("#SYSTEM00000")
                    .setApVersion("2020 R1"));

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
                new LicensedApplication().setApplicationIdentity(Constants.getApProApplicationIdentity())
                    .setCreatedBy("#SYSTEM00000"));

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
                new InstallationItems().setName("Automation Installation")
                    .setDescription("Installation added by API automation")
                    .setActive(true)
                    .setRegion("na-1")
                    .setRealm(realmKey)
                    .setUrl("https://na-1.qa.apriori.net")
                    .setS3Bucket("apriori-qa-blue-fms")
                    .setTenant("default")
                    .setTenantGroup("default")
                    .setClientId("apriori-web-cost")
                    .setClientSecret("donotusethiskey")
                    .setCreatedBy("#SYSTEM00000")
                    .setCidGlobalKey("donotusethiskey")
                    .setSiteIdentity(siteIdentity)
                    .setApplications(Arrays.asList(Constants.getApProApplicationIdentity()))
                    .setCloudReference(cloudReference));

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
                new InstallationItems().setCloudReference("eu-1"));

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
                new AssociationUserItems().setUserIdentity(userIdentity)
                    .setCreatedBy("#SYSTEM00000"));

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
                new AssociationUserItems().setUserIdentity(userIdentity)
                    .setCreatedBy("#SYSTEM00000"));

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
                new IdentityProviderRequest().setContact(userIdentity)
                    .setName(customerName + "-idp")
                    .setDisplayName(customerName + "SAML")
                    .setIdpDomains(Arrays.asList(customerName + ".com"))
                    .setIdentityProviderPlatform("Azure AD")
                    .setDescription("Create IDP using CDS automation")
                    .setActive(true)
                    .setCreatedBy("#SYSTEM00000")
                    .setSignInUrl(Constants.getSignInUrl())
                    .setSigningCertificate(Constants.getSignInCert())
                    .setSigningCertificateExpiresAt("2030-07-22T22:45Z")
                    .setSignRequest(true)
                    .setSignRequestAlgorithm("RSA_SHA256")
                    .setSignRequestAlgorithmDigest("SHA256")
                    .setProtocolBinding("HTTP_POST")
                    .setAttributeMappings(new AttributeMappings().setUserId(Constants.getSamlNameIdentifier())
                        .setEmail(Constants.getSamlAttributeEmail())
                        .setName(Constants.getSamlAttributeName())
                        .setGivenName(Constants.getSamlAttributeGivenName())
                        .setFamilyName(Constants.getSamlAttributeFamilyName())));

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
                new IdentityProviderRequest().setDescription("patch IDP using Automation")
                    .setContact(userIdentity));
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
            .setBody(new LicenseRequest().setLicense(
                new License().setDescription("Test License")
                    .setApVersion("2020 R1")
                    .setCreatedBy("#SYSTEM00000")
                    .setActive("true")
                    .setLicense(String.format(Constants.getLicense(), customerName, siteId, licenseId, subLicenseId))
                    .setLicenseTemplate(String.format(Constants.getLicenseTemplate(), customerName))));

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
                new AccessControlRequest().setCustomerIdentity(Constants.getAPrioriInternalCustomerIdentity())
                    .setDeploymentIdentity(Constants.getApProductionDeploymentIdentity())
                    .setInstallationIdentity(Constants.getApCoreInstallationIdentity())
                    .setApplicationIdentity(Constants.getApCloudHomeApplicationIdentity())
                    .setCreatedBy("#SYSTEM00000")
                    .setRoleName("USER")
                    .setRoleIdentity(Constants.getCdsIdentityRole()));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }
}
