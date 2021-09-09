package com.apriori.tests.utils;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cas.enums.CASAPIEnum;
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
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class CasTestUtil extends TestUtil {

    private static String token = new JwtTokenUtil().retrieveJwtToken();

    /**
     * Gets a common request
     *
     * @param endpointEnum - end point enum
     * @param urlEncoding  - encoding
     * @param klass        - the return type
     * @param headers      - the header
     * @return request entity
     */
    public static RequestEntity getCommonRequest(EndpointEnum endpointEnum, boolean urlEncoding, Class klass, Map<String, String> headers) {
        return new RequestEntity().endpoint(endpointEnum)
            .returnType(klass)
            .urlEncodingEnabled(urlEncoding)
            .headers(headers);
    }

    /**
     * POST call to add a customer
     *
     * @param name           - the customer name
     * @param cloudReference - the cloud reference name
     * @param description    - the description
     * @param email          - the email pattern
     * @return ResponseWrapper <SingleCustomer>
     */
    public static ResponseWrapper<Customer> addCustomer(String name, String cloudReference, String description, String email) {

        RequestEntity requestEntity = new RequestEntity().endpoint(CASAPIEnum.GET_CUSTOMER)
            .returnType(Customer.class)
            .headers(new APIAuthentication().initAuthorizationHeaderContent(token))
            .body("customer",
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

        return HTTP2Request.build(requestEntity).post();
    }

    /**
     * PATCH call to update a customer
     *
     * @param email - the email pattern
     * @return ResponseWrapper <Customer>
     */
    public static ResponseWrapper<Customer> updateCustomer(String identity, String email) {

        RequestEntity requestEntity = new RequestEntity().endpoint(CASAPIEnum.GET_CUSTOMER_ID)
            .returnType(Customer.class)
            .headers(new APIAuthentication().initAuthorizationHeaderContent(token))
            .body("customer",
                Customer.builder()
                    .emailDomains(Arrays.asList(email + ".com", email + ".co.uk"))
                    .build())
            .inlineVariables(identity);

        return HTTP2Request.build(requestEntity).patch();
    }

    /**
     * @param identity - the identity
     * @return <T>ResponseWrapper <T>
     */
    public static <T> ResponseWrapper<T> resetMfa(String identity) {

        RequestEntity requestEntity = new RequestEntity().endpoint(CASAPIEnum.POST_MFA)
            .returnType(null)
            .headers(new APIAuthentication().initAuthorizationHeaderContent(token))
            .inlineVariables(identity);

        return HTTP2Request.build(requestEntity).post();
    }

    /**
     * @param identity - the identity
     * @return <T>ResponseWrapper <T>
     */
    public static <T> ResponseWrapper<T> resetMfa(String customerIdentity, String identity) {

        RequestEntity requestEntity = new RequestEntity().endpoint(CASAPIEnum.POST_MFA)
            .returnType(null)
            .headers(new APIAuthentication().initAuthorizationHeaderContent(token))
            .inlineVariables(customerIdentity, "users", identity);

        return HTTP2Request.build(requestEntity).post();
    }

    /**
     * @param siteId - site ID
     * @return ResponseWrapper <ValidateSite>
     */
    public static ResponseWrapper<ValidateSite> validateSite(String identity, String siteId) {

        RequestEntity requestEntity = new RequestEntity().endpoint(CASAPIEnum.POST_SITES)
            .returnType(ValidateSite.class)
            .headers(new APIAuthentication().initAuthorizationHeaderContent(token))
            .body("site",
                Site.builder().siteId(siteId)
                    .build())
            .inlineVariables(identity);

        return HTTP2Request.build(requestEntity).post();
    }

    /**
     * @param siteId   - site ID
     * @param siteName - site name
     * @return ResponseWrapper <Site>
     */
    public static ResponseWrapper<Site> addSite(String identity, String siteId, String siteName) {

        RequestEntity requestEntity = new RequestEntity().endpoint(CASAPIEnum.POST_SITES)
            .returnType(Site.class)
            .headers(new APIAuthentication().initAuthorizationHeaderContent(token))
            .body("site",
                Site.builder().siteId(siteId)
                    .name(siteName)
                    .description("Site created by automation test")
                    .active(true)
                    .build())
            .inlineVariables(identity);

        return HTTP2Request.build(requestEntity).post();
    }


    /**
     * @param userName - username
     * @return ResponseWrapper <CustomerUser>
     */
    public static ResponseWrapper<CustomerUser> addUser(String identity, String userName, String customerName) {

        RequestEntity requestEntity = new RequestEntity().endpoint(CASAPIEnum.POST_USERS)
            .returnType(CustomerUser.class)
            .headers(new APIAuthentication().initAuthorizationHeaderContent(token))
            .body("user",
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
                    .build())
            .inlineVariables(identity);

        return HTTP2Request.build(requestEntity).post();
    }

    /**
     * @param userName         - username
     * @param identity         - user identity
     * @param customerIdentity - customer identity
     * @param profileIdentity  - user profile identity
     * @return ResponseWrapper <UpdateUser>
     */
    public static ResponseWrapper<UpdateUser> updateUser(String userName, String customerName, String identity, String customerIdentity, String profileIdentity) {
        LocalDateTime createdAt = LocalDateTime.parse("2020-11-23T10:15:30");
        LocalDateTime updatedAt = LocalDateTime.parse("2021-02-19T10:25");
        LocalDateTime profileCreatedAt = LocalDateTime.parse("2020-11-23T13:34");

        RequestEntity requestEntity = new RequestEntity().endpoint(CASAPIEnum.PATCH_USERS)
            .returnType(UpdateUser.class)
            .headers(new APIAuthentication().initAuthorizationHeaderContent(token))
            .body("user",
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

        return HTTP2Request.build(requestEntity).patch();
    }


    /**
     * @return ResponseWrapper <PostBatch>
     */
    public static ResponseWrapper<PostBatch> addBatchFile(String customerIdentity) {

        final File batchFile = FileResourceUtil.getResourceAsFile("users.csv");

        RequestEntity requestEntity = new RequestEntity().endpoint(CASAPIEnum.GET_CUSTOMERS)
            .returnType(PostBatch.class)
            .headers(new APIAuthentication().initAuthorizationHeaderContent(token))
            .multiPartFiles(new MultiPartFiles().use("multiPartFile", batchFile))
            .inlineVariables(customerIdentity, "batches/");

        return HTTP2Request.build(requestEntity).post();
    }

    /**
     * @return <T>ResponseWrapper <T>
     */
    public static <T> ResponseWrapper<T> deleteBatch(String customerIdentity, String batchIdentity) {

        RequestEntity requestEntity = new RequestEntity().endpoint(CASAPIEnum.CUSTOMER_BATCHES)
            .returnType(null)
            .headers(new APIAuthentication().initAuthorizationHeaderContent(token))
            .inlineVariables(customerIdentity, "batches", batchIdentity);

        return HTTP2Request.build(requestEntity).delete();
    }


    /**
     * @param customerIdentity - the customer identity
     * @param batchIdentity    - batch identity
     * @return <T>ResponseWrapper <T>
     */
    public static <T> ResponseWrapper<T> newUsersFromBatch(String customerIdentity, String batchIdentity) {

        RequestEntity requestEntity = new RequestEntity().endpoint(CASAPIEnum.GET_BATCHES)
            .returnType(null)
            .headers(new APIAuthentication().initAuthorizationHeaderContent(token))
            .body(new BatchItemsPost().setBatchItems(Arrays.asList(batchIdentity)))
            .inlineVariables(customerIdentity, "batches", batchIdentity, "items");

        return HTTP2Request.build(requestEntity).post();
    }

    /**
     * @param customerIdentity - the customer identity
     * @param batchIdentity    - batch identity
     * @param itemIdentity     - item identity
     * @return ResponseWrapper <BatchItem>
     */
    public static ResponseWrapper<BatchItem> updateBatchItem(String customerIdentity, String batchIdentity, String itemIdentity) {

        RequestEntity requestEntity = new RequestEntity().endpoint(CASAPIEnum.GET_BATCHES)
            .returnType(BatchItem.class)
            .headers(new APIAuthentication().initAuthorizationHeaderContent(token))
            .body("batchItem",
                BatchItem.builder().userName("maggie")
                    .givenName("Maggie")
                    .familyName("Simpsons")
                    .prefix("Miss")
                    .cityTown("Springfield")
                    .jobTitle("QA"))
            .inlineVariables(customerIdentity, "batches", batchIdentity, "items", itemIdentity);

        return HTTP2Request.build(requestEntity).patch();
    }
}