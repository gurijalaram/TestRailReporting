package com.apriori.cds.tests.utils;

import com.apriori.apibase.services.cds.AttributeMappings;
import com.apriori.apibase.services.common.objects.IdentityProviderRequest;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.cds.objects.request.AccessControlRequest;
import com.apriori.cds.objects.request.AddDeployment;
import com.apriori.cds.objects.request.License;
import com.apriori.cds.objects.request.LicenseRequest;
import com.apriori.cds.objects.response.AssociationUserItems;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.InstallationItems;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.UserProfile;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import java.util.Arrays;

public class CdsTestUtil extends TestUtil {

    protected <T> ResponseWrapper<T> getCommonRequest(String url, boolean urlEncoding, Class klass) {
        return GenericRequestUtil.get(
            RequestEntity.init(url, klass).setUrlEncodingEnabled(urlEncoding),
            new RequestAreaApi()
        );
    }

    // TODO: 11/02/2021 ciene - all methods below to be moved into a util class

    /**
     * POST call to add a customer
     *
     * @param url            - the endpoint
     * @param klass          - the response class
     * @param name           - the customer name
     * @param cloudReference - the cloud reference name
     * @param salesForceId   - the sales force id
     * @param email          - the email pattern
     * @return <T>ResponseWrapper<T>
     */
    public <T> ResponseWrapper<T> addCustomer(String url, Class klass, String name, String cloudReference, String salesForceId, String email) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
            .setBody("customer",
                new Customer().setName(name)
                    .setDescription("Add new customers api test")
                    .setCustomerType("CLOUD_ONLY")
                    .setCreatedBy("#SYSTEM00000")
                    .setCloudReference(cloudReference)
                    .setSalesforceId(salesForceId)
                    .setActive(true)
                    .setMfaRequired(false)
                    .setUseExternalIdentityProvider(false)
                    .setMaxCadFileRetentionDays(1095)
                    .setEmailRegexPatterns(Arrays.asList(email + ".com", email + ".co.uk")));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * POST call to add a customer
     *
     * @param url          - the endpoint
     * @param klass        - the response class
     * @param userName     - the user name
     * @param customerName - the customer name
     * @return <T>ResponseWrapper<T>
     */
    public <T> ResponseWrapper<T> addUser(String url, Class klass, String userName, String customerName) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
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
     * @param url   - the endpoint
     * @param klass - the response class
     * @return <T>ResponseWrapper<T>
     */
    public <T> ResponseWrapper<T> patchUser(String url, Class klass) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
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
     * @param url      - the endpoint
     * @param klass    - the response class
     * @param siteName - the site name
     * @param siteID   - the siteID
     * @return <T>ResponseWrapper<T>
     */
    public <T> ResponseWrapper<T> addSite(String url, Class klass, String siteName, String siteID) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
            .setBody("site",
                new Site().setName(siteName)
                    .setDescription("Site created by automation test")
                    .setSiteId(siteID)
                    .setCreatedBy("#SYSTEM00000")
                    .setActive(true));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * POST call to add a deployment to a customer
     *
     * @param url          - the endpoint
     * @param klass        - the response class
     * @param siteIdentity - the site Identity
     * @return <T>ResponseWrapper<T>
     */
    public <T> ResponseWrapper<T> addDeployment(String url, Class klass, String siteIdentity) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
            .setBody("deployment",
                new AddDeployment().setName("Production Deployment")
                    .setDescription("Deployment added by API automation")
                    .setDeploymentType("PRODUCTION")
                    .setSiteIdentity(siteIdentity)
                    .setActive("true")
                    .setIsDefault("true")
                    .setCreatedBy("#SYSTEM00000")
                    .setApVersion("2020 R1")
                    .setApplications(Arrays.asList("1J8M416FBJBK")));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * POST call to add an installation to a customer
     *
     * @param url            - the endpoint
     * @param klass          - the response class
     * @param realmKey       - the realm key
     * @param cloudReference - the cloud reference
     * @return <T>ResponseWrapper<T>
     */
    public <T> ResponseWrapper<T> addInstallation(String url, Class klass, String realmKey, String cloudReference) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
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
                    .setCloudReference(cloudReference));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * POST call to add an apriori staff user association to a customer
     *
     * @param url          - the endpoint
     * @param klass        - the response class
     * @param userIdentity - the aPriori Staff users identity
     * @return <T>ResponseWrapper<T>
     */
    public <T> ResponseWrapper<T> addAssociationUser(String url, Class klass, String userIdentity) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
            .setBody("userAssociation",
                new AssociationUserItems().setUserIdentity(userIdentity)
                    .setCreatedBy("#SYSTEM00000"));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * Calls the delete method
     *
     * @param deleteEndpoint - the endpoint to delete
     * @return responsewrapper
     */
    public ResponseWrapper<String> delete(String deleteEndpoint) {
        RequestEntity requestEntity = RequestEntity.init(deleteEndpoint, null)
            .setHeaders("Content-Type", "application/json");

        return GenericRequestUtil.delete(requestEntity, new RequestAreaApi());
    }

    /**
     * Post to add SAML
     *
     * @param url          - the url
     * @param klass        - the response class
     * @param userIdentity - the aPriori Staff users identity
     * @param customerName     - the customer name
     * @return <T> ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> addSaml(String url, Class klass, String userIdentity, String customerName) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
            .setBody("identityProvider",
                new IdentityProviderRequest().setContact(userIdentity)
                    .setName(customerName + "-idp")
                    .setDisplayName(customerName + "SAML")
                    .setIdpDomains(Arrays.asList(customerName + ".com"))
                    .setIdentityProviderPlatform("AZURE AD")
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
     * Post to add site license
     *
     * @param url          - the url
     * @param klass        - the response class
     * @param customerName - the customer name
     * @param siteId       - the site id
     * @param licenseId    - the license id
     * @param subLicenseId - the sublicense id
     * @return <T>ResponseWrapper<T>
     */
    public <T> ResponseWrapper<T> addLicense(String url, Class klass, String customerName, String siteId, String licenseId, String subLicenseId) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
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
     * @param url   - the url
     * @param klass - the class
     * @param <T>   - generic return type
     * @return <T>ResponseWrapper</T>
     */
    public <T> ResponseWrapper<T> addAccessControl(String url, Class klass) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
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
