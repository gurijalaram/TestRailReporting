package com.apriori.utils;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.entity.response.BatchItem;
import com.apriori.entity.response.BatchItemsPost;
import com.apriori.entity.response.CustomProperties;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.CustomerUserProfile;
import com.apriori.entity.response.PostBatch;
import com.apriori.entity.response.Site;
import com.apriori.entity.response.UpdateUser;
import com.apriori.entity.response.UpdatedProfile;
import com.apriori.entity.response.ValidateSite;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

public class CasTestUtil extends TestUtil {

    private static String token = new JwtTokenUtil().retrieveJwtToken();
    private String url = String.format(PropertiesContext.get("${env}.cas.api_url"), "customers/");

    /**
     * POST call to add a customer
     *
     * @param name           - the customer name
     * @param cloudReference - the cloud reference name
     * @param description    - the description
     * @param email          - the email pattern
     * @return ResponseWrapper <SingleCustomer>
     */
    public ResponseWrapper<Customer> addCustomer(String name, String cloudReference, String description, String email) {

        RequestEntity requestEntity = RequestEntity.init(url, Customer.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("customer",
                Customer.builder().name(name)
                    .cloudReference(cloudReference)
                    .description(description)
                    .salesforceId(new GenerateStringUtil().generateSalesForceId())
                    .customerType("CLOUD_ONLY")
                    .active(true)
                    .mfaRequired(true)
                    .useExternalIdentityProvider(false)
                    .maxCadFileRetentionDays(584)
                    .maxCadFileSize(51)
                    .emailDomains(Arrays.asList(email + ".com", email + ".co.uk"))
                    .build());

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
                Customer.builder()
                    .emailDomains(Arrays.asList(email + ".com", email + ".co.uk"))
                    .build());

        return GenericRequestUtil.patch(requestEntity, new RequestAreaApi());
    }

    /**
     * @param url - the endpoint
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
                Site.builder().siteId(siteId)
                    .build());

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
                Site.builder().siteId(siteId)
                    .name(siteName)
                    .description("Site created by automation test")
                    .active(true)
                    .build());

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @param userName - username
     * @return ResponseWrapper <CustomerUser>
     */
    public ResponseWrapper<CustomerUser> addUser(String identity, String userName, String customerName) {
        String endpoint = url + identity + "/users/";

        RequestEntity requestEntity = RequestEntity.init(endpoint, CustomerUser.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("user",
                CustomerUser.builder().userType("AP_CLOUD_USER")
                    .email(userName.toLowerCase() + "@" + customerName.toLowerCase() + ".co.uk")
                    .username(userName)
                    .active(true)
                    .userProfile(new CustomerUserProfile().setGivenName(userName)
                        .setFamilyName("Automater")
                        .setJobTitle("Automation Engineer")
                        .setDepartment("Automation")
                        .setSupervisor("Ciene Frith")
                        .setTownCity("Brooklyn"))
                    .build());

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @param userName         - username
     * @param identity         - user identity
     * @param customerIdentity - customer identity
     * @param profileIdentity  - user profile identity
     * @return ResponseWrapper <UpdateUser>
     */
    public ResponseWrapper<UpdateUser> updateUser(String userName, String customerName, String identity, String customerIdentity, String profileIdentity) {
        LocalDateTime createdAt = LocalDateTime.parse("2020-11-23T10:15:30");
        LocalDateTime updatedAt = LocalDateTime.parse("2021-02-19T10:25");
        LocalDateTime profileCreatedAt = LocalDateTime.parse("2020-11-23T13:34");
        String endpoint = url + customerIdentity + "/users/" + identity;

        RequestEntity requestEntity = RequestEntity.init(endpoint, UpdateUser.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("user",
                UpdateUser.builder().userType("AP_CLOUD_USER")
                    .email(userName.toLowerCase() + "@" + customerName.toLowerCase() + ".co.uk")
                    .username(userName)
                    .active(true)
                    .identity(identity)
                    .createdAt(createdAt)
                    .createdBy("#SYSTEM00000")
                    .updatedAt(updatedAt)
                    .customerIdentity(customerIdentity)
                    .mfaRequired(true)
                    .customProperties(new CustomProperties())
                    .createdByName("SYSTEM")
                    .licenseAssignments(Collections.singletonList(""))
                    .userType("AP_CLOUD_USER")
                    .userProfile(new UpdatedProfile().setIdentity(profileIdentity)
                        .setCreatedAt(profileCreatedAt)
                        .setCreatedBy("#SYSTEM00000")
                        .setGivenName(userName)
                        .setFamilyName("Automater")
                        .setJobTitle("Automation Engineer")
                        .setDepartment("QA")
                        .setSupervisor("Ciene Frith"))
                    .build());

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
     * @param batchIdentity    - batch identity
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
     * @param batchIdentity    - batch identity
     * @param itemIdentity     - item identity
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