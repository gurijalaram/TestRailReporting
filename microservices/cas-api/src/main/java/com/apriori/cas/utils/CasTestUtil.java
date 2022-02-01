package com.apriori.cas.utils;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.services.cas.Customers;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cds.objects.request.License;
import com.apriori.cds.objects.request.LicenseRequest;
import com.apriori.cds.objects.response.AssociationUserItems;
import com.apriori.entity.response.BatchItem;
import com.apriori.entity.response.BatchItemsPost;
import com.apriori.entity.response.CustomProperties;
import com.apriori.entity.response.CustomerAssociation;
import com.apriori.entity.response.CustomerAssociationUser;
import com.apriori.entity.response.CustomerAssociationUsers;
import com.apriori.entity.response.CustomerAssociations;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.CustomerUserProfile;
import com.apriori.entity.response.CustomerUsers;
import com.apriori.entity.response.LicenseResponse;
import com.apriori.entity.response.PostBatch;
import com.apriori.entity.response.Site;
import com.apriori.entity.response.UpdateUser;
import com.apriori.entity.response.UpdatedProfile;
import com.apriori.entity.response.ValidateSite;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CasTestUtil extends TestUtil {
    private static final String token = new AuthorizationUtil().getTokenAsString();

    /**
     * Gets the special customer "aPriori Internal"
     *
     * @return The customer representing aPriori Internal
     */
    public Customer getAprioriInternal() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("name[EQ]", "aPriori Internal");

        Customer customer = findFirst(CASAPIEnum.CUSTOMERS, Customers.class, filters, Collections.emptyMap());

        if (customer == null) {
            throw new IllegalStateException("Customer, aPriori Internal, is missing.  The data set is corrupted.");
        }

        return customer;
    }

    /**
     * Creates a random customer.
     *
     * @return The response for the customer created.
     */
    public ResponseWrapper<Customer> createCustomer() {
        GenerateStringUtil generator = new GenerateStringUtil();
        return createCustomer(
            generator.generateCustomerName(),
            generator.generateCloudReference(),
            generator.getRandomString(),
            "apriori.com",
            "apriori.co.uk",
            "test.com",
            "test.co.uk");
    }

    /**
     * Creates a pseudo random customer.
     *
     * @param name           The name of the customer.
     * @param cloudReference The customer cloud reference.
     * @param description    Customer description.
     * @param domains        The email domains for the customer.
     * @return The response for the customer created.
     */
    public ResponseWrapper<Customer> createCustomer(String name, String cloudReference, String description, String... domains) {
        Customer customer = Customer.builder().name(name)
            .cloudReference(cloudReference)
            .description(description)
            .salesforceId(new GenerateStringUtil().generateSalesForceId())
            .customerType("CLOUD_ONLY")
            .active(true)
            .mfaRequired(true)
            .useExternalIdentityProvider(false)
            .maxCadFileRetentionDays(584)
            .maxCadFileSize(51)
            .emailDomains(Arrays.asList(domains))
            .build();
        return create(CASAPIEnum.CUSTOMERS, Customer.class, customer);
    }

    /**
     * Searches for a specific customer association.
     *
     * @param source The source customer.
     * @param target The target customer.
     * @return The customer association for the target->source relationship.  Returns null
     * if there is no association.
     */
    public CustomerAssociation findCustomerAssociation(Customer source, Customer target) {
        return findFirst(
            CASAPIEnum.CUSTOMER_ASSOCIATIONS,
            CustomerAssociations.class,
            Collections.singletonMap("targetCustomer.identity[EQ]", target.getIdentity()),
            Collections.emptyMap(),
            source.getIdentity());
    }

    /**
     * @param source
     * @param association
     * @param identity
     * @return
     */
    public CustomerAssociationUser findCustomerAssociationUser(Customer source, CustomerAssociation association, String identity) {
        return findFirst(
            CASAPIEnum.CUSTOMER_ASSOCIATIONS_USERS,
            CustomerAssociationUsers.class,
            Collections.singletonMap("identity[EQ]", identity),
            Collections.emptyMap(),
            source.getIdentity(),
            association.getIdentity()
        );
    }

    /**
     * @param source
     * @param association
     * @return
     */
    public ResponseWrapper<CustomerAssociationUsers> findCustomerAssociationUsers(Customer source, CustomerAssociation association) {
        return find(
            CASAPIEnum.CUSTOMER_ASSOCIATIONS_USERS,
            CustomerAssociationUsers.class,
            Collections.emptyMap(),
            Collections.emptyMap(),
            1,
            1000,
            source.getIdentity(),
            association.getIdentity()
        );
    }

    /**
     * @param source
     * @param association
     * @return
     */
    public ResponseWrapper<CustomerUsers> findCustomerAssociationCandidates(Customer source, CustomerAssociation association) {
        return find(
            CASAPIEnum.CUSTOMER_ASSOCIATION_CANDIDATES,
            CustomerUsers.class,
            Collections.emptyMap(),
            Collections.emptyMap(),
            1,
            1000,
            source.getIdentity(),
            association.getIdentity()
        );
    }

    /**
     * Associates a user to another customer and creates an out of context relationship.
     *
     * @param user        The user to associate in the source customer.
     * @param association The association that contains the target customer.
     * @return The response.
     */
    public ResponseWrapper<CustomerAssociationUser> createCustomerAssociationUser(CustomerUser user, CustomerAssociation association) {
        CustomerAssociationUser body = CustomerAssociationUser.builder()
            .userIdentity(user.getIdentity())
            .build();
        return create(
            CASAPIEnum.CUSTOMER_ASSOCIATIONS_USERS,
            CustomerAssociationUser.class,
            body,
            user.getCustomerIdentity(),
            association.getIdentity()
        );
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
        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER, Customer.class)
            .token(token)
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

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * PATCH call to update a customer
     *
     * @param email - the email pattern
     * @return ResponseWrapper <Customer>
     */
    public static ResponseWrapper<Customer> updateCustomer(String identity, String email) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER_ID, Customer.class)
            .token(token)
            .body("customer",
                Customer.builder()
                    .emailDomains(Arrays.asList(email + ".com", email + ".co.uk"))
                    .build())
            .inlineVariables(identity);

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * @param identity - the identity
     * @return <T>ResponseWrapper <T>
     */
    public static <T> ResponseWrapper<T> resetMfa(String identity) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.POST_MFA, null)
            .token(token)
            .inlineVariables(identity);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param identity - the identity
     * @return <T>ResponseWrapper <T>
     */
    public static <T> ResponseWrapper<T> resetMfa(String customerIdentity, String identity) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.POST_MFA, null)
            .token(token)
            .inlineVariables(customerIdentity, "users", identity);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param siteId - site ID
     * @return ResponseWrapper <ValidateSite>
     */
    public static ResponseWrapper<ValidateSite> validateSite(String identity, String siteId) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.POST_SITES, ValidateSite.class)
            .token(token)
            .body("site",
                Site.builder().siteId(siteId)
                    .build())
            .inlineVariables(identity);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param siteId   - site ID
     * @param siteName - site name
     * @return ResponseWrapper <Site>
     */
    public static ResponseWrapper<Site> addSite(String identity, String siteId, String siteName) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.POST_SITES, Site.class)
            .token(token)
            .body("site",
                Site.builder().siteId(siteId)
                    .name(siteName)
                    .description("Site created by automation test")
                    .active(true)
                    .build())
            .inlineVariables(identity);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param source
     * @return
     */
    public List<CustomerUser> findUsers(Customer source) {
        return findAll(CASAPIEnum.USERS, CustomerUsers.class, Collections.emptyMap(), Collections.emptyMap(), source.getIdentity());
    }

    /**
     * @param customer
     * @return
     */
    public ResponseWrapper<CustomerUser> createUser(Customer customer) {
        String domain = customer.getEmailDomains().stream().findFirst().orElseThrow(() -> new IllegalStateException("This customer has no email domains"));
        return createUser(customer.getIdentity(), domain);
    }

    /**
     * @param customerIdentity
     * @param domain
     * @return
     */
    public ResponseWrapper<CustomerUser> createUser(String customerIdentity, String domain) {
        GenerateStringUtil generator = new GenerateStringUtil();
        return createUser(customerIdentity, generator.generateUserName(), domain);
    }

    /**
     * @param customerIdentity
     * @param userName
     * @param domain
     * @return
     */
    public ResponseWrapper<CustomerUser> createUser(final String customerIdentity, final String userName, final String domain) {
        String email = String.format("%s@%s", userName.toLowerCase(), domain);

        CustomerUserProfile profile = CustomerUserProfile.builder().givenName("Robot")
            .familyName("Automator")
            .jobTitle("Automation Engineer")
            .department("Automation")
            .supervisor("Ciene Frith")
            .townCity("Brooklyn")
            .build();

        CustomerUser user = CustomerUser.builder()
            .userType("AP_CLOUD_USER")
            .email(email)
            .username(userName)
            .active(true)
            .userProfile(profile)
            .build();

        return create(CASAPIEnum.USERS, CustomerUser.class, user, customerIdentity);
    }

    /**
     * @param userName - username
     * @return ResponseWrapper <CustomerUser>
     */
    public static ResponseWrapper<CustomerUser> addUser(String identity, String userName, String customerName) {

        String domain = String.format("%s.co.uk", customerName.toLowerCase());
        CasTestUtil util = new CasTestUtil();
        RequestEntityUtil.useTokenForRequests(token);
        return util.createUser(identity, userName, domain);
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

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.PATCH_USERS, UpdateUser.class)
            .token(token)
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

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * @return ResponseWrapper <PostBatch>
     */
    public static ResponseWrapper<PostBatch> addBatchFile(String customerIdentity) {

        final File batchFile = FileResourceUtil.getResourceAsFile("users.csv");

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMERS, PostBatch.class)
            .token(token)
            .multiPartFiles(new MultiPartFiles().use("multiPartFile", batchFile))
            .inlineVariables(customerIdentity, "batches/");

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @return <T>ResponseWrapper <T>
     */
    public static <T> ResponseWrapper<T> deleteBatch(String customerIdentity, String batchIdentity) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.CUSTOMER_BATCHES, null)
            .token(token)
            .inlineVariables(customerIdentity, "batches", batchIdentity);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * @param customerIdentity - the customer identity
     * @param batchIdentity    - batch identity
     * @return <T>ResponseWrapper <T>
     */
    public static <T> ResponseWrapper<T> newUsersFromBatch(String customerIdentity, String batchIdentity) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.GET_BATCHES, null)
            .token(token)
            .body(BatchItemsPost.builder().batchItems(Collections.singletonList(batchIdentity))
                .build())
            .inlineVariables(customerIdentity, "batches", batchIdentity, "items");

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param customerIdentity - the customer identity
     * @param batchIdentity    - batch identity
     * @param itemIdentity     - item identity
     * @return ResponseWrapper <BatchItem>
     */
    public static ResponseWrapper<BatchItem> updateBatchItem(String customerIdentity, String batchIdentity, String itemIdentity) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.GET_BATCHES, BatchItem.class)
            .token(token)
            .body("batchItem",
                BatchItem.builder().userName("maggie")
                    .givenName("Maggie")
                    .familyName("Simpsons")
                    .prefix("Miss")
                    .cityTown("Springfield")
                    .jobTitle("QA"))
            .inlineVariables(customerIdentity, "batches", batchIdentity, "items", itemIdentity);

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * @param customerIdentity
     * @param siteIdentity
     * @param customerName
     * @param siteId
     * @param licenseId
     * @param subLicenseId
     * @return
     */
    public ResponseWrapper<LicenseResponse> addLicense(String customerIdentity, String siteIdentity, String customerName, String siteId, String licenseId, String subLicenseId) {
        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.POST_LICENSE_BY_CUSTOMER_SITE_IDS, LicenseResponse.class)
                .inlineVariables(customerIdentity, siteIdentity)
                .headers(new HashMap<String, String>() {
                    {
                        put("Content-Type", "");
                    }
                })
                .body(LicenseRequest.builder()
                        .license(
                                License.builder()
                                        .description("Test License")
                                        .apVersion("2020 R1")
                                        .createdBy("#SYSTEM00000")
                                        .active("true")
                                        .license(String.format(Constants.CAS_EXPIRED_LICENSE, customerName, siteId, licenseId, subLicenseId))
                                        .licenseTemplate(String.format(Constants.CAS_EXPIRED_LICENSE_TEMPLATE, customerName))
                                        .build())
                        .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param klass
     * @param customerIdentity
     * @param siteIdentity
     * @param licenseIdentity
     * @param subLicenseIdentity
     * @param userIdentity
     * @param <T>
     * @return
     */
    public static <T> ResponseWrapper<T> addSubLicenseAssociationUser(Class<T> klass, String customerIdentity, String siteIdentity, String licenseIdentity, String subLicenseIdentity, String userIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.POST_SUBLICENSE_ASSOCIATIONS, klass)
                .inlineVariables(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity)
                .body("userAssociation",
                        AssociationUserItems.builder()
                                .userIdentity(userIdentity)
                                .createdBy("#SYSTEM00000")
                                .build());

        return HTTPRequest.build(requestEntity).post();
    }
}