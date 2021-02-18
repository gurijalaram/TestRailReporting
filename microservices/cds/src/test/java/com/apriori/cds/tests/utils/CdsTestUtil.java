package com.apriori.cds.tests.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cds.objects.request.AddDeployment;
import com.apriori.cds.objects.request.UpdateCredentials;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.InstallationItems;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.UserProfile;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

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
     * @param url          - the endpoint
     * @param klass        - the response class
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
     * PATCH call to update a users credentials
     *
     * @param url          - the endpoint
     * @param klass        - the response class
     * @param currentPasswordHash - the users current password hash
     * @return <T>ResponseWrapper<T>
     */
    public <T> ResponseWrapper<T> patchCredentials(String url, Class klass, String currentPasswordHash) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
            .setBody("userCredential",
                new UpdateCredentials().setCurrentPasswordHash(currentPasswordHash)
                    .setNewEncryptedPassword("k02b1983edad20a0d7b13e5d9dd6bd502d193eff7d1a2768d2ac0766c69b73ba")
                    .setNewPasswordHash("32521452457252")
                    .setNewPasswordSalt("7848110ce8a800bfe583f4cc79b6143ad760ae7b5768e112"));

        return GenericRequestUtil.patch(requestEntity, new RequestAreaApi());
    }

    /**
     * Delete an api customer/user
     *
     * @param deleteEndpoint - the endpoint to delete a customer/user
     */
    public void delete(String deleteEndpoint) {
        RequestEntity requestEntity = RequestEntity.init(deleteEndpoint, null)
            .setHeaders("Content-Type", "application/json");

        ResponseWrapper<String> responseWrapper = GenericRequestUtil.delete(requestEntity, new RequestAreaApi());

        assertThat(responseWrapper.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
