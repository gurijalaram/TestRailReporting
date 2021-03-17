package com.apriori.tests.utils;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.services.common.objects.ErrorMessage;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.TestUtil;

import com.apriori.entity.response.BatchItem;
import com.apriori.entity.response.BatchItemsPost;
import com.apriori.entity.response.CustomProperties;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.CustomerUserProfile;
import com.apriori.entity.response.Site;
import com.apriori.entity.response.UpdateUser;
import com.apriori.entity.response.UpdatedProfile;

import com.apriori.utils.Constants;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

public class CasTestUtil extends TestUtil {

    /**
     * POST call to add a customer
     *
     * @param url            - the endpoint
     * @param klass          - the response class
     * @param token          - the token
     * @param name           - the customer name
     * @param cloudReference - the cloud reference name
     * @param description    - the description
     * @param email          - the email pattern
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> addCustomer(String url, Class klass, String token, String name, String cloudReference, String description, String email) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("customer",
                new Customer().setName(name)
                    .setCloudReference(cloudReference)
                    .setDescription(description)
                    .setCustomerType("CLOUD_ONLY")
                    .setActive(true)
                    .setMfaRequired(true)
                    .setUseExternalIdentityProvider(false)
                    .setMaxCadFileRetentionDays(584)
                    .setEmailDomains(Arrays.asList(email + ".com", "gmail.com")));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * PATCH call to update a customer
     *
     * @param url   - the endpoint
     * @param klass - the response class
     * @param token - the token
     * @param email - the email pattern
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> updateCustomer(String url, Class klass, String token, String email) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("customer",
                new Customer().setEmailDomains(Arrays.asList(email + ".com", email + ".co.uk")));

        return GenericRequestUtil.patch(requestEntity, new RequestAreaApi());
    }

    /**
     * @param url   - the endpoint
     * @param token - token
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> resetMfa(String url, String token) {
        RequestEntity requestEntity = RequestEntity.init(url, null)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @param url    - the endpoint
     * @param klass  - the response class
     * @param token  - token
     * @param siteId - site ID
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> validateSite(String url, Class klass, String token, String siteId) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("site",
                new Site().setSiteId(siteId));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @param url      - the endpoint
     * @param klass    - the response class
     * @param token    - token
     * @param siteId   - site ID
     * @param siteName - site name
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> addSite(String url, Class klass, String token, String siteId, String siteName) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("site",
                new Site().setSiteId(siteId)
                    .setName(siteName)
                    .setDescription("Site created by automation test")
                    .setActive(true));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @param url      - the endpoint
     * @param klass    - the response class
     * @param token    - token
     * @param userName - username
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> addUser(String url, Class klass, String token, String userName) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("user",
                new CustomerUser().setUserType("AP_CLOUD_USER")
                    .setEmail(userName.toLowerCase() + "@gmail.com")
                    .setUsername(userName)
                    .setActive(true)
                    .setUserProfile(new CustomerUserProfile().setGivenName(userName)
                        .setFamilyName("Automater")
                        .setJobTitle("Automation Engineer")
                        .setDepartment("Automation")
                        .setSupervisor("Ciene Frith")
                        .setTownCity("Brooklyn")));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @param url              - the endpoint
     * @param klass            - the response class
     * @param token            - token
     * @param userName         - username
     * @param identity         - user identity
     * @param customerIdentity - customer identity
     * @param profileIdentity  - user profile identity
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> updateUser(String url, Class klass, String token, String userName, String identity, String customerIdentity, String profileIdentity) {
        LocalDateTime createdAt = LocalDateTime.parse("2020-11-23T10:15:30");
        LocalDateTime updatedAt = LocalDateTime.parse("2021-02-19T10:25");
        LocalDateTime profileCreatedAt = LocalDateTime.parse("2020-11-23T13:34");

        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("user",
                new UpdateUser().setUserType("AP_CLOUD_USER")
                    .setEmail(userName.toLowerCase() + "@gmail.com")
                    .setUsername(userName)
                    .setActive(true)
                    .setIdentity(identity)
                    .setCreatedAt(createdAt)
                    .setCreatedBy("#SYSTEM00000")
                    .setUpdatedAt(updatedAt)
                    .setCustomerIdentity(customerIdentity)
                    .setMfaRequired(true)
                    .setCustomProperties(new CustomProperties())
                    .setCreatedByName("SYSTEM")
                    .setLicenseAssignments(Collections.singletonList(""))
                    .setUserType("AP_CLOUD_USER")
                    .setUserProfile(new UpdatedProfile().setIdentity(profileIdentity)
                        .setCreatedAt(profileCreatedAt)
                        .setCreatedBy("#SYSTEM00000")
                        .setGivenName(userName)
                        .setFamilyName("Automater")
                        .setJobTitle("Automation Engineer")
                        .setDepartment("QA")
                        .setSupervisor("Ciene Frith")));

        return GenericRequestUtil.patch(requestEntity, new RequestAreaApi());
    }

    /**
     * @param url - the endpoint
     * @param klass - the response class
     * @param token - token
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> addBatchFile(String url, Class klass, String token) {
        final File batchFile = FileResourceUtil.getResourceAsFile("users.csv");
        RequestEntity requestEntity = RequestEntity.init(url, klass)
                .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
                .setMultiPartFiles(new MultiPartFiles().use("multiPartFile", batchFile));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @param url - the endpoint
     * @param token - token
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> deleteBatch(String url, String token) {
        RequestEntity requestEntity = RequestEntity.init(url, null)
                .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.delete(requestEntity, new RequestAreaApi());
    }

    /**
     * @param token - token
     * @param customerIdentity - the customer identity
     * @param batchIdentity - batch identity
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> newUsersFromBatch(String token, String customerIdentity, String batchIdentity) {
        String url = String.format(Constants.getApiUrl(), "customers/" + customerIdentity + "/batches/" + batchIdentity + "/items");
        RequestEntity requestEntity = RequestEntity.init(url, null)
                .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
                .setBody(new BatchItemsPost().setBatchItems(Arrays.asList(batchIdentity)));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @param customerIdentity - the customer identity
     * @param batchIdentity - batch identity
     * @param itemIdentity - item identity
     * @param token - token
     * @return ResponseWrapper <ErrorMessage>
     */
    public ResponseWrapper<ErrorMessage> updateBatchItem(String customerIdentity, String batchIdentity, String itemIdentity, String token) {
        String url = String.format(Constants.getApiUrl(), "customers/" + customerIdentity + "/batches/" + batchIdentity + "/items/" + itemIdentity);
        RequestEntity requestEntity = RequestEntity.init(url, ErrorMessage.class)
                .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
                .setBody("batchItem",
                    new BatchItem().setUserName("maggie")
                        .setGivenName("Maggie")
                        .setFamilyName("Simpsons")
                        .setPrefix("Miss")
                        .setCityTown("Springfield")
                        .setJobTitle("QA"));

        return GenericRequestUtil.patch(requestEntity, new RequestAreaApi());
    }
}