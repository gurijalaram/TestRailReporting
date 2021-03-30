package com.apriori.tests.utils;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;

import com.apriori.entity.response.BatchItem;
import com.apriori.entity.response.BatchItemsPost;
import com.apriori.entity.response.CustomProperties;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.CustomerUserProfile;
import com.apriori.entity.response.PostBatch;
import com.apriori.entity.response.SingleCustomer;
import com.apriori.entity.response.Site;
import com.apriori.entity.response.UpdateUser;
import com.apriori.entity.response.UpdatedProfile;
import com.apriori.entity.response.ValidateSite;

import com.apriori.utils.Constants;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

public class CasTestUtil extends TestUtil {

    private static String token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
            Constants.getCasServiceHost(),
            HttpStatus.SC_CREATED,
            Constants.getCasTokenUsername(),
            Constants.getCasTokenEmail(),
            Constants.getCasTokenIssuer(),
            Constants.getCasTokenSubject());
    private String url = String.format(Constants.getApiUrl(), "customers/");

    /**
     * POST call to add a customer
     *
     * @param name           - the customer name
     * @param cloudReference - the cloud reference name
     * @param description    - the description
     * @param email          - the email pattern
     * @return ResponseWrapper <SingleCustomer>
     */
    public ResponseWrapper<SingleCustomer> addCustomer(String name, String cloudReference, String description, String email) {

        RequestEntity requestEntity = RequestEntity.init(url, SingleCustomer.class)
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
     * @param email - the email pattern
     * @return ResponseWrapper <Customer>
     */
    public ResponseWrapper<Customer> updateCustomer(String identity, String email) {
        String endpoint = url + identity;

        RequestEntity requestEntity = RequestEntity.init(endpoint, Customer.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("customer",
                new Customer().setEmailDomains(Arrays.asList(email + ".com", email + ".co.uk")));

        return GenericRequestUtil.patch(requestEntity, new RequestAreaApi());
    }

    /**
     * @param url   - the endpoint
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> resetMfa(String url) {
        RequestEntity requestEntity = RequestEntity.init(url, null)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @param siteId - site ID
     * @return ResponseWrapper <ValidateSite>
     */
    public ResponseWrapper<ValidateSite> validateSite(String identity, String siteId) {
        String endpoint = url + identity + "/sites/validate";

        RequestEntity requestEntity = RequestEntity.init(endpoint, ValidateSite.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("site",
                new Site().setSiteId(siteId));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @param siteId   - site ID
     * @param siteName - site name
     * @return ResponseWrapper <Site>
     */
    public ResponseWrapper<Site> addSite(String identity, String siteId, String siteName) {
        String endpoint = url + identity + "/sites";

        RequestEntity requestEntity = RequestEntity.init(endpoint, Site.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("site",
                new Site().setSiteId(siteId)
                    .setName(siteName)
                    .setDescription("Site created by automation test")
                    .setActive(true));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @param userName - username
     * @return ResponseWrapper <CustomerUser>
     */
    public ResponseWrapper<CustomerUser> addUser(String identity, String userName) {
        String endpoint = url + identity + "/users/";

        RequestEntity requestEntity = RequestEntity.init(endpoint, CustomerUser.class)
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
     * @param userName         - username
     * @param identity         - user identity
     * @param customerIdentity - customer identity
     * @param profileIdentity  - user profile identity
     * @return ResponseWrapper <UpdateUser>
     */
    public ResponseWrapper<UpdateUser> updateUser(String userName, String identity, String customerIdentity, String profileIdentity) {
        LocalDateTime createdAt = LocalDateTime.parse("2020-11-23T10:15:30");
        LocalDateTime updatedAt = LocalDateTime.parse("2021-02-19T10:25");
        LocalDateTime profileCreatedAt = LocalDateTime.parse("2020-11-23T13:34");
        String endpoint = url + customerIdentity + "/users/" + identity;

        RequestEntity requestEntity = RequestEntity.init(endpoint, UpdateUser.class)
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
     * @return ResponseWrapper <PostBatch>
     */
    public ResponseWrapper<PostBatch> addBatchFile(String customerIdentity) {
        String endpoint = url + customerIdentity + "/batches/";

        final File batchFile = FileResourceUtil.getResourceAsFile("users.csv");
        RequestEntity requestEntity = RequestEntity.init(endpoint, PostBatch.class)
                .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
                .setMultiPartFiles(new MultiPartFiles().use("multiPartFile", batchFile));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> deleteBatch(String customerIdentity, String batchIdentity) {
        String endpoint = url + customerIdentity + "/batches/" + batchIdentity;

        RequestEntity requestEntity = RequestEntity.init(endpoint, null)
                .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.delete(requestEntity, new RequestAreaApi());
    }

    /**
     * @param customerIdentity - the customer identity
     * @param batchIdentity - batch identity
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> newUsersFromBatch(String customerIdentity, String batchIdentity) {
        String endpoint = url + customerIdentity + "/batches/" + batchIdentity + "/items";

        RequestEntity requestEntity = RequestEntity.init(endpoint, null)
                .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
                .setBody(new BatchItemsPost().setBatchItems(Arrays.asList(batchIdentity)));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @param customerIdentity - the customer identity
     * @param batchIdentity - batch identity
     * @param itemIdentity - item identity
     * @return ResponseWrapper <BatchItem>
     */
    public ResponseWrapper<BatchItem> updateBatchItem(String customerIdentity, String batchIdentity, String itemIdentity) {
        String endpoint = url + customerIdentity + "/batches/" + batchIdentity + "/items/" + itemIdentity;

        RequestEntity requestEntity = RequestEntity.init(endpoint, BatchItem.class)
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