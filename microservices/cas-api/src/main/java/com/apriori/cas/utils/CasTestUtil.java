package com.apriori.cas.utils;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.models.requests.BulkAccessControlRequest;
import com.apriori.cas.models.response.AccessAuthorization;
import com.apriori.cas.models.response.AccessControl;
import com.apriori.cas.models.response.AssociationUser;
import com.apriori.cas.models.response.BatchItem;
import com.apriori.cas.models.response.BatchItemsPost;
import com.apriori.cas.models.response.CustomProperties;
import com.apriori.cas.models.response.Customer;
import com.apriori.cas.models.response.CustomerAssociation;
import com.apriori.cas.models.response.CustomerAssociationUser;
import com.apriori.cas.models.response.CustomerAssociationUsers;
import com.apriori.cas.models.response.CustomerAssociations;
import com.apriori.cas.models.response.CustomerUser;
import com.apriori.cas.models.response.CustomerUserProfile;
import com.apriori.cas.models.response.CustomerUsers;
import com.apriori.cas.models.response.Customers;
import com.apriori.cas.models.response.LicenseResponse;
import com.apriori.cas.models.response.PostBatch;
import com.apriori.cas.models.response.Site;
import com.apriori.cas.models.response.UpdateUser;
import com.apriori.cas.models.response.UpdatedProfile;
import com.apriori.cas.models.response.ValidateSite;
import com.apriori.cds.objects.request.License;
import com.apriori.cds.objects.request.LicenseRequest;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.MultiPartFiles;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.properties.PropertiesContext;

import org.apache.http.HttpStatus;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CasTestUtil extends TestUtil {

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
        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.CUSTOMERS, Customer.class)
            .expectedResponseCode(HttpStatus.SC_CREATED)
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

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.CUSTOMER, Customer.class)
            .expectedResponseCode(HttpStatus.SC_OK)
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

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.MFA, null)
            .expectedResponseCode(HttpStatus.SC_ACCEPTED)
            .inlineVariables(identity);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param identity - the identity
     * @return <T>ResponseWrapper <T>
     */
    public static <T> ResponseWrapper<T> resetUserMfa(String customerIdentity, String identity) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.MFA, null)
            .inlineVariables(customerIdentity, "users", identity)
            .expectedResponseCode(HttpStatus.SC_ACCEPTED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param siteId - site ID
     * @return ResponseWrapper <ValidateSite>
     */
    public static ResponseWrapper<ValidateSite> validateSite(String identity, String siteId) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.CUSTOMER, ValidateSite.class)
            .body("site",
                Site.builder().siteId(siteId)
                    .build())
            .inlineVariables(identity + "/sites/validate")
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param siteId   - site ID
     * @param siteName - site name
     * @return ResponseWrapper <Site>
     */
    public static ResponseWrapper<Site> addSite(String identity, String siteId, String siteName) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.SITES, Site.class)
            .body("site",
                Site.builder().siteId(siteId)
                    .name(siteName)
                    .description("Site created by automation test")
                    .active(true)
                    .build())
            .inlineVariables(identity)
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param userName - username
     * @return ResponseWrapper <CustomerUser>
     */
    public static ResponseWrapper<CustomerUser> addUser(String identity, String userName, String customerName) {

        String domain = String.format("%s.co.uk", customerName.toLowerCase());
        CasTestUtil util = new CasTestUtil();
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

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.USER, UpdateUser.class)
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
                    .userProfile(UpdatedProfile.builder()
                        .identity(profileIdentity)
                        .createdAt(profileCreatedAt)
                        .createdBy("#SYSTEM00000")
                        .givenName(userName)
                        .familyName("Automater")
                        .jobTitle("Automation Engineer")
                        .department("QA")
                        .supervisor("Ciene Frith")
                        .build())
                    .build())
            .inlineVariables(customerIdentity, identity)
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * @return ResponseWrapper <PostBatch>
     */
    public static ResponseWrapper<PostBatch> addBatchFile(String customerIdentity) {

        final File batchFile = FileResourceUtil.getResourceAsFile("users.csv");

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.CUSTOMER, PostBatch.class)
            .multiPartFiles(new MultiPartFiles().use("multiPartFile", batchFile))
            .inlineVariables(customerIdentity + "/batches/")
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @return <T>ResponseWrapper <T>
     */
    public static <T> ResponseWrapper<T> deleteBatch(String customerIdentity, String batchIdentity) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.BATCH, null)
            .inlineVariables(customerIdentity, batchIdentity)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * @param customerIdentity - the customer identity
     * @param batchIdentity    - batch identity
     * @return <T>ResponseWrapper <T>
     */
    public static <T> ResponseWrapper<T> newUsersFromBatch(String customerIdentity, String batchIdentity) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.BATCH_ITEMS, null)
            .body(BatchItemsPost.builder().batchItems(Collections.singletonList(batchIdentity))
                .build())
            .inlineVariables(customerIdentity, batchIdentity)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param customerIdentity - the customer identity
     * @param batchIdentity    - batch identity
     * @param itemIdentity     - item identity
     * @return ResponseWrapper <BatchItem>
     */
    public static ResponseWrapper<BatchItem> updateBatchItem(String customerIdentity, String batchIdentity, String itemIdentity) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.BATCH_ITEM, BatchItem.class)
            .body("batchItem",
                BatchItem.builder().userName("maggie")
                    .givenName("Maggie")
                    .familyName("Simpsons")
                    .prefix("Miss")
                    .cityTown("Springfield")
                    .jobTitle("QA"))
            .inlineVariables(customerIdentity, batchIdentity, itemIdentity)
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).patch();
    }

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
        return create(CASAPIEnum.CUSTOMERS, Customer.class, customer, HttpStatus.SC_CREATED);
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
     * @param source      - the customer source
     * @param association - The association that contains the target customer.
     * @param identity    - the identity
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
     * @param source      - the customer source
     * @param association - The association that contains the target customer.
     * @return ResponseWrapper <CustomerAssociationUsers>
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
     * @param source      - the customer source
     * @param association The association that contains the target customer.
     * @return ResponseWrapper <CustomerUsers>
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
            HttpStatus.SC_CREATED,
            user.getCustomerIdentity(),
            association.getIdentity()
        );
    }

    /**
     * Looking for On Prem customer
     *
     * @return Customer class
     */
    public Customer findOnPremCustomer() {
        return find(
            CASAPIEnum.CUSTOMERS,
            Customers.class,
            Collections.emptyMap(),
            Collections.emptyMap(),
            1,
            1000,
            null)
            .getResponseEntity()
            .getItems()
            .stream()
            .filter(customer -> customer.getCustomerType().equals("ON_PREMISE_ONLY"))
            .collect(Collectors.toList())
            .get(0);
    }

    /**
     * Creates a customer with On Premise customer type
     *
     * @param name  - customer name
     * @param email - customer email
     * @return ResponseWrapper<Customer>
     */
    public ResponseWrapper<Customer> createOnPremCustomer(String name, String email) {
        GenerateStringUtil generator = new GenerateStringUtil();
        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.CUSTOMERS, Customer.class)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("customer",
                Customer.builder()
                    .name(name)
                    .cloudReference(null)
                    .description(generator.getRandomString())
                    .salesforceId(generator.generateSalesForceId())
                    .customerType("ON_PREMISE_ONLY")
                    .active(true)
                    .mfaRequired(false)
                    .useExternalIdentityProvider(false)
                    .maxCadFileRetentionDays(584)
                    .maxCadFileSize(51)
                    .emailDomains(Arrays.asList(email + ".com", email + ".co.uk"))
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param source - the customer source
     * @return ResponseWrapper <CustomerUser>
     */
    public List<CustomerUser> findUsers(Customer source) {
        return findAll(CASAPIEnum.USERS, CustomerUsers.class, Collections.emptyMap(), Collections.emptyMap(), source.getIdentity());
    }

    /**
     * @param customer - the customer
     * @return ResponseWrapper <CustomerUser>
     */
    public ResponseWrapper<CustomerUser> createUser(Customer customer) {
        String domain = customer.getEmailDomains().stream().findFirst().orElseThrow(() -> new IllegalStateException("This customer has no email domains"));
        return createUser(customer.getIdentity(), domain);
    }

    /**
     * @param customerIdentity - customer identity
     * @param domain           - domain
     * @return ResponseWrapper <CustomerUser>
     */
    public ResponseWrapper<CustomerUser> createUser(String customerIdentity, String domain) {
        GenerateStringUtil generator = new GenerateStringUtil();
        return createUser(customerIdentity, generator.generateUserName(), domain);
    }

    /**
     * @param customerIdentity - customer identity
     * @param userName         - user name
     * @param domain           - domain
     * @return ResponseWrapper <CustomerUser>
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

        return create(CASAPIEnum.USERS, CustomerUser.class, user, HttpStatus.SC_CREATED, customerIdentity);
    }

    /**
     * @param casLicense       - license text
     * @param customerIdentity - the customer identity
     * @param siteIdentity     - the site identity
     * @param customerName     - the customer name
     * @param siteId           - the site id
     * @param subLicenseId     - the sublicense id
     * @return ResponseWrapper <LicenseResponse>
     */
    public ResponseWrapper<LicenseResponse> addLicense(String casLicense, String customerIdentity, String siteIdentity, String customerName, String siteId, String subLicenseId) {

        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.LICENSE_BY_CUSTOMER_SITE_IDS, LicenseResponse.class)
            .inlineVariables(customerIdentity, siteIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(LicenseRequest.builder()
                .license(
                    License.builder()
                        .description("Test License")
                        .apVersion("2020 R1")
                        .createdBy("#SYSTEM00000")
                        .license(String.format(casLicense, customerName, siteId, subLicenseId, subLicenseId))
                        .build())
                .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * @param klass              - class
     * @param customerIdentity   - the customer identity
     * @param siteIdentity       - the site identity
     * @param licenseIdentity    - the license identity
     * @param subLicenseIdentity - the sublicense identity
     * @param userIdentity       - the user identity
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> addSubLicenseAssociationUser(Class<T> klass, String customerIdentity, String siteIdentity, String licenseIdentity, String subLicenseIdentity, String userIdentity, Integer expectedResponseCode) {
        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.SUBLICENSE_ASSOCIATIONS, klass)
            .inlineVariables(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity)
            .expectedResponseCode(expectedResponseCode)
            .body("userAssociation",
                AssociationUser.builder()
                    .userIdentity(userIdentity)
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Creates new access control for user
     *
     * @param customerIdentity - customer identity
     * @param userIdentity     - user identity
     * @return ResponseWrapper <AccessControl>
     */
    public ResponseWrapper<AccessControl> addAccessControl(String customerIdentity, String userIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.ACCESS_CONTROLS, AccessControl.class)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("accessControl",
                AccessControl.builder()
                    .customerIdentity(PropertiesContext.get("customer_identity"))
                    .applicationIdentity(PropertiesContext.get("cds.apriori_cloud_home_identity"))
                    .deploymentIdentity(PropertiesContext.get("cds.apriori_production_deployment_identity"))
                    .installationIdentity(PropertiesContext.get("cds.apriori_core_services_installation_identity"))
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Grants bulk access to customer application
     *
     * @param aPInternalIdentity   - identity of aP Internal customer
     * @param siteIdentity         - site identity
     * @param deploymentIdentity   - deployment identity
     * @param installationIdentity - installation identity
     * @param appIdentity          - application identity
     * @param sourceCustomerId     - source customer identity
     * @return ResponseWrapper <String>
     */
    public ResponseWrapper<String> grantDenyAll(String aPInternalIdentity, String siteIdentity, String deploymentIdentity, String installationIdentity, String appIdentity, String grantOrDeny, String sourceCustomerId) {
        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.GRANT_DENY_ALL, null)
            .inlineVariables(aPInternalIdentity, siteIdentity, deploymentIdentity, installationIdentity, appIdentity, grantOrDeny)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT)
            .body(BulkAccessControlRequest.builder()
                .sourceCustomerIdentity(sourceCustomerId)
                .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Changes activation state of a License.
     *
     * @param klas             - class
     * @param customerIdentity - customer identity
     * @param siteIdentity     - site identity
     * @param licenseIdentity  - license identity
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> activateLicense(Class<T> klas, String customerIdentity, String siteIdentity, String licenseIdentity, Integer expectedResponseCode) {
        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.ACTIVATE_LICENSE, klas)
            .inlineVariables(customerIdentity, siteIdentity, licenseIdentity)
            .expectedResponseCode(expectedResponseCode)
            .body(null);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Requests access for a customer
     *
     * @param klas                 - class
     * @param customerIdentity     - customer identity
     * @param userIdentity         - user identity
     * @param serviceAccount       - service account name
     * @param expectedResponseCode - expected response code
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> addAccessAuthorization(Class<T> klas, String customerIdentity, String userIdentity, String serviceAccount, Integer expectedResponseCode) {
        RequestEntity requestEntity = RequestEntityUtil.init(CASAPIEnum.ACCESS_AUTHORIZATIONS, klas)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(expectedResponseCode)
            .body("accessAuthorization",
                AccessAuthorization.builder()
                    .userIdentity(userIdentity)
                    .serviceAccount(serviceAccount)
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }
}