package com.apriori.cds.api.utils;


import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;

import com.apriori.cds.api.enums.CASCustomerEnum;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.request.AccessAuthorizationRequest;
import com.apriori.cds.api.models.request.AddDeployment;
import com.apriori.cds.api.models.request.CASCustomerRequest;
import com.apriori.cds.api.models.request.CustomAttributeRequest;
import com.apriori.cds.api.models.request.PostBatch;
import com.apriori.cds.api.models.request.UpdateCredentials;
import com.apriori.cds.api.models.response.AccessAuthorization;
import com.apriori.cds.api.models.response.AssociationUserItems;
import com.apriori.cds.api.models.response.AttributeMappings;
import com.apriori.cds.api.models.response.CredentialsItems;
import com.apriori.cds.api.models.response.CustomAttribute;
import com.apriori.cds.api.models.response.ErrorResponse;
import com.apriori.cds.api.models.response.IdentityProviderRequest;
import com.apriori.cds.api.models.response.IdentityProviderResponse;
import com.apriori.cds.api.models.response.Roles;
import com.apriori.cds.api.models.response.UserPreference;
import com.apriori.cds.api.models.response.UserRole;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.MultiPartFiles;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.json.JsonManager;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.Enablements;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.UserProfile;
import com.apriori.shared.util.models.response.Users;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


public class CdsTestUtil extends TestUtil {

    private RequestEntityUtil requestEntityUtil;

    public CdsTestUtil(RequestEntityUtil requestEntityUtil) {
        super.requestEntityUtil = requestEntityUtil;
        this.requestEntityUtil = requestEntityUtil;
    }

    // TODO: 14/06/2024 cn - remove in next iteration
    // this empty constructor is needed just for now to avoid multiple errors.
    public CdsTestUtil() {
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
    public ResponseWrapper<Customer> addCustomer(
        String name,
        String customerType,
        String cloudReference,
        String salesForceId,
        String email) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOMERS, Customer.class)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "customer",
                Customer.builder().name(name)
                    .description("Add new customers api test")
                    .customerType(customerType)
                    .createdBy("#SYSTEM00000")
                    .cloudReference(cloudReference)
                    .salesforceId(salesForceId)
                    .active(true)
                    .mfaRequired(false)
                    .useExternalIdentityProvider(false)
                    .maxCadFileRetentionDays(1095)
                    .maxCadFileSize(51)
                    .emailRegexPatterns(Arrays.asList(email + ".com", email + ".co.uk"))
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Creates customer with random data
     *
     * @param rcd random customer data
     * @return new object
     */
    public ResponseWrapper<Customer> createCustomer(RandomCustomerData rcd) {
        return addCustomer(rcd.getCustomerName(), rcd.getCustomerType(), rcd.getCloudRef(), rcd.getSalesForceId(), rcd.getEmailPattern());
    }

    /**
     * Creates customer via CAS API with service accounts
     *
     * @param name           - the customer name
     * @param cloudReference - the cloud reference name
     * @param email          - the email pattern
     * @return new object
     */
    public ResponseWrapper<Customer> addCASCustomer(
        String name,
        String cloudReference,
        String email,
        UserCredentials currentUser) {

        RequestEntity requestEntity = requestEntityUtil.init(CASCustomerEnum.CUSTOMERS, Customer.class)
            .token(currentUser.getToken())
            .body(
                "customer",
                CASCustomerRequest.builder().name(name)
                    .cloudReference(cloudReference)
                    .description("Add new customers api test")
                    .salesforceId(new GenerateStringUtil().generateNumericString("SFID", 10))
                    .customerType("CLOUD_ONLY")
                    .active(true)
                    .mfaRequired(true)
                    .mfaAuthenticator("ONE_TIME_PASSWORD")
                    .useExternalIdentityProvider(false)
                    .maxCadFileRetentionDays(584)
                    .maxCadFileSize(51)
                    .emailDomains(Arrays.asList(email + ".com", email + ".co.uk"))
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Updates customer by customer id
     *
     * @param customerIdentity    - customer identity
     * @param updatedEmailPattern - new value of email pattern
     * @return new object
     */
    public ResponseWrapper<Customer> updateCustomer(String customerIdentity, String updatedEmailPattern) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOMER_BY_ID, Customer.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_OK)
            .headers(new HashMap<String, String>() {

                {
                    put("Content-Type", "application/json");
                }
            })
            .body(
                "customer",
                Customer.builder()
                    .emailRegexPatterns(Arrays.asList(updatedEmailPattern + ".com", updatedEmailPattern + ".co.uk"))
                    .build()
            );

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * POST call to add a customer
     *
     * @param customerIdentity - the customer id
     * @param userName         - the username
     * @param domain           - the customer name
     * @return new object
     */
    public ResponseWrapper<User> addUser(String customerIdentity, String userName, String domain) {

        User requestBody = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("CreateUserData.json").getPath(), User.class);
        requestBody.setUsername(userName);
        requestBody.setEmail(userName + "@" + domain + ".com");
        requestBody.getUserProfile().setGivenName(userName);
        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOMER_USERS, User.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("user", requestBody);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Creates user with set of enablements
     *
     * @param customerIdentity     - the customer id
     * @param userName             - the username
     * @param domain               - the customer name
     * @param customerAssignedRole - the customer assigned role
     * @return new object
     */
    public ResponseWrapper<User> addUserWithEnablements(String customerIdentity, String userName, String domain, String customerAssignedRole) {

        User requestBody = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("CreateUserWithEnablementsData.json").getPath(), User.class);
        requestBody.setUsername(userName);
        requestBody.setEmail(userName + "@" + domain + ".com");
        requestBody.getUserProfile().setGivenName(userName);
        requestBody.getEnablements().setCustomerAssignedRole(customerAssignedRole);
        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOMER_USERS, User.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("user", requestBody);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * PATCH call to update a user
     *
     * @param user - the user
     * @return new object
     */
    public ResponseWrapper<User> patchUser(User user) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class)
            .inlineVariables(user.getCustomerIdentity(), user.getIdentity())
            .expectedResponseCode(HttpStatus.SC_OK)
            .body(
                "user",
                User.builder()
                    .identity(user.getIdentity())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .createdBy(user.getCreatedBy())
                    .active(user.getActive())
                    .userProfile(UserProfile.builder()
                        .department("Design Dept")
                        .supervisor("Moya Parker").build())
                    .build()
            );

        return HTTPRequest.build(requestEntity).patch();
    }

    public <T> ResponseWrapper<T> patchUser(
        Class<T> klass,
        String customerIdentity,
        String userIdentity,
        Integer expectedResponseCode,
        JsonNode user) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, klass)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(expectedResponseCode)
            .body("user", user);

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * PATCH call to update the user credentials
     *
     * @param customerIdentity    - the customer id
     * @param userIdentity        - the user id
     * @param passwordHashCurrent - current hash password
     * @param passwordSalt        - the salt password
     * @return new object
     */
    public ResponseWrapper<CredentialsItems> updateUserCredentials(
        String customerIdentity,
        String userIdentity,
        String passwordHashCurrent,
        String passwordSalt) {

        RequestEntity requestEntity = requestEntityUtil
            .init(CDSAPIEnum.USER_CREDENTIALS_BY_CUSTOMER_USER_IDS, CredentialsItems.class)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(HttpStatus.SC_OK)
            .body(
                "userCredential",
                UpdateCredentials.builder()
                    .currentPasswordHash(passwordHashCurrent)
                    .newPasswordHash(new GenerateStringUtil().getHashPassword())
                    .newPasswordSalt(passwordSalt)
                    .newEncryptedPassword(new GenerateStringUtil().getRandomStringSpecLength(12).toLowerCase())
                    .build()
            );

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * POST call to add a deployment to a customer
     *
     * @param customerIdentity - the customer id
     * @param siteIdentity     - the site Identity
     * @return new object
     */
    public ResponseWrapper<Deployment> addDeployment(
        String customerIdentity,
        String deploymentName,
        String siteIdentity,
        String deploymentType) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.DEPLOYMENTS_BY_CUSTOMER_ID, Deployment.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "deployment",
                AddDeployment.builder()
                    .name(deploymentName)
                    .description("Deployment added by API automation")
                    .deploymentType(deploymentType)
                    .siteIdentity(siteIdentity)
                    .active("true")
                    .isDefault("true")
                    .createdBy("#SYSTEM00000")
                    .apVersion("2020 R1")
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST call to add an apriori staff user association to a customer
     *
     * @param apCustomerIdentity  - the ap customer id
     * @param associationIdentity - the association id
     * @param userIdentity        - the aPriori Staff users identity
     * @return new object
     */
    public ResponseWrapper<AssociationUserItems> addAssociationUser(
        String apCustomerIdentity,
        String associationIdentity,
        String userIdentity) {

        RequestEntity requestEntity = requestEntityUtil
            .init(CDSAPIEnum.ASSOCIATIONS_BY_CUSTOMER_ASSOCIATIONS_IDS, AssociationUserItems.class)
            .inlineVariables(apCustomerIdentity, associationIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "userAssociation",
                AssociationUserItems.builder()
                    .userIdentity(userIdentity)
                    .createdBy("#SYSTEM00000")
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to add SAML
     *
     * @param customerIdentity - the customer id
     * @param userIdentity     - the aPriori Staff users identity
     * @param customerName     - the customer name
     * @return new object
     */
    @SneakyThrows
    public ResponseWrapper<IdentityProviderResponse> addSaml(
        String customerIdentity,
        String userIdentity,
        String customerName) {

        String signingCertificate = new String(FileResourceUtil.getResourceFileStream("SigningCert.txt").readAllBytes(), StandardCharsets.UTF_8);

        RequestEntity requestEntity = requestEntityUtil
            .init(CDSAPIEnum.SAML_BY_CUSTOMER_ID, IdentityProviderResponse.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "identityProvider",
                IdentityProviderRequest.builder().contact(userIdentity)
                    .name(customerName + "-idp")
                    .displayName(customerName + "SAML")
                    .idpDomains(Collections.singletonList(customerName + ".com"))
                    .identityProviderPlatform("Azure AD")
                    .description("Create IDP using CDS automation")
                    .active(true)
                    .createdBy("#SYSTEM00000")
                    .signInUrl(Constants.SIGNIN_URL)
                    .signingCertificate(signingCertificate)
                    .signingCertificateExpiresAt("2030-07-22T22:45:45.245Z")
                    .signRequest(true)
                    .signRequestAlgorithm("RSA_SHA256")
                    .signRequestAlgorithmDigest("SHA256")
                    .protocolBinding("HTTP_POST")
                    .authenticationType("IDENTITY_PROVIDER_INITIATED_SSO")
                    .attributeMappings(AttributeMappings.builder()
                        .userId(Constants.SAML_ATTRIBUTE_NAME_IDENTIFIER)
                        .email(Constants.SAML_ATTRIBUTE_NAME_EMAIL)
                        .name(Constants.SAML_ATTRIBUTE_NAME)
                        .givenName(Constants.SAML_ATTRIBUTE_NAME_GIVEN_NAME)
                        .familyName(Constants.SAML_ATTRIBUTE_NAME_FAMILY_NAME).build())
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Patches and idp user
     *
     * @param customerIdentity - the customer id
     * @param idpIdentity      - the idp id
     * @param userIdentity     - the user id
     * @return new object
     */
    public ResponseWrapper<IdentityProviderResponse> patchIdp(
        String customerIdentity,
        String idpIdentity,
        String userIdentity) {

        RequestEntity requestEntity = requestEntityUtil
            .init(CDSAPIEnum.SAML_BY_CUSTOMER_PROVIDER_IDS, IdentityProviderResponse.class)
            .inlineVariables(customerIdentity, idpIdentity)
            .expectedResponseCode(HttpStatus.SC_OK)
            .headers(new HashMap<>() {

                {
                    put("Content-Type", "application/json");
                }
            })
            .body(
                "identityProvider",
                IdentityProviderRequest.builder()
                    .description("patch IDP using Automation")
                    .contact(userIdentity)
                    .identityProviderPlatform("Azure AD")
                    .updatedBy("#SYSTEM00000")
                    .build()
            );

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Posts custom attribute
     *
     * @param customerIdentity - the customer id
     * @param userIdentity     - the user id
     * @return - new object
     */
    public ResponseWrapper<CustomAttribute> addCustomAttribute(String customerIdentity, String userIdentity) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOM_ATTRIBUTES, CustomAttribute.class)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "customAttribute",
                CustomAttributeRequest.builder()
                    .key("department")
                    .name("department")
                    .value("TestDepartment")
                    .type("STRING")
                    .createdBy("#SYSTEM00000")
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Updates or adds custom attributes
     *
     * @param customerIdentity  - the customer id
     * @param userIdentity      - user id
     * @param updatedDepartment - updated department string
     * @return - new object
     */
    public ResponseWrapper<CustomAttribute> putCustomAttribute(
        String customerIdentity,
        String userIdentity,
        String updatedDepartment) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOM_ATTRIBUTES, CustomAttribute.class)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "customAttribute",
                CustomAttributeRequest.builder()
                    .name("department")
                    .value(updatedDepartment)
                    .updatedBy("#SYSTEM00000")
                    .build()
            );

        return HTTPRequest.build(requestEntity).put();
    }

    /**
     * Updates custom attribute
     *
     * @param customerIdentity  - the customer id
     * @param userIdentity      - user id
     * @param attributeIdentity - attribute id
     * @param updatedDepartment -  updated department string
     * @return - new object
     */
    public ResponseWrapper<CustomAttribute> updateAttribute(
        String customerIdentity,
        String userIdentity,
        String attributeIdentity,
        String updatedDepartment) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOM_ATTRIBUTE_BY_ID, CustomAttribute.class)
            .inlineVariables(customerIdentity, userIdentity, attributeIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "customAttribute",
                CustomAttributeRequest.builder()
                    .value(updatedDepartment)
                    .updatedBy("#SYSTEM00000")
                    .build()
            );

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Adds new user preference
     *
     * @param customerIdentity - customer id
     * @param userIdentity     - user id
     * @return - new object
     */
    public ResponseWrapper<UserPreference> addUserPreference(String customerIdentity, String userIdentity) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.USER_PREFERENCES, UserPreference.class)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "userPreference",
                UserPreference.builder()
                    .name("Test")
                    .value("1234")
                    .type("STRING")
                    .createdBy("#SYSTEM00000")
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Updates existing user preference
     *
     * @param customerIdentity  - customer Id
     * @param userIdentity      - user Id
     * @param updatedPreference - value of updated preference
     * @return -  new object
     */
    public ResponseWrapper<UserPreference> updatePreference(
        String customerIdentity,
        String userIdentity,
        String preferenceIdentity,
        String updatedPreference) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.PREFERENCE_BY_ID, UserPreference.class)
            .inlineVariables(customerIdentity, userIdentity, preferenceIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "userPreference",
                UserPreference.builder()
                    .value(updatedPreference)
                    .updatedBy("#SYSTEM00000")
                    .build()
            );

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Adds or replaces user preferences
     *
     * @param customerIdentity - customer id
     * @param userIdentity     - user id
     * @param preferenceName   - preference name
     * @return new object
     */
    public ResponseWrapper<UserPreference> putUserPreference(
        String customerIdentity,
        String userIdentity,
        String preferenceName) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.PREFERENCE_BY_ID, UserPreference.class)
            .inlineVariables(customerIdentity, userIdentity, preferenceName)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "userPreference",
                UserPreference.builder()
                    .value("6548")
                    .type("INTEGER")
                    .createdBy("#SYSTEM00000")
                    .build()
            );

        return HTTPRequest.build(requestEntity).put();
    }

    /**
     * @param customerIdentity customer id
     * @param userIdentity     user id
     * @param serviceAccount   service account
     * @return new object
     */
    public ResponseWrapper<AccessAuthorization> addAccessAuthorization(
        String customerIdentity,
        String userIdentity,
        String serviceAccount) {

        RequestEntity requestEntity = requestEntityUtil
            .init(CDSAPIEnum.ACCESS_AUTHORIZATIONS, AccessAuthorization.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "accessAuthorization",
                AccessAuthorizationRequest.builder()
                    .userIdentity(userIdentity)
                    .serviceAccount(serviceAccount)
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Uploads batch users file via CAS API
     *
     * @param users            - string of users file
     * @param email            - email
     * @param customerIdentity - customerIdentity
     * @return new object
     */
    public ResponseWrapper<PostBatch> addCASBatchFile(
        String users,
        String email,
        String customerIdentity,
        UserCredentials currentUser) {

        StringBuilder sb = new StringBuilder(users);
        sb.append("\r\n");
        String userRecord = "User%sTest,user%s@%s.com,Test%s,User%s,,,,,,,,,,,,,,,,,,,,,,,,,,";
        for (int i = 0; i < 23; i++) {
            sb.append(String.format(userRecord, i, i, email, i, i));
            sb.append("\r\n");
        }

        InputStream usersBatch = new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));

        RequestEntity requestEntity = requestEntityUtil.init(CASCustomerEnum.CUSTOMERS_BATCH, PostBatch.class)
            .token(currentUser.getToken())
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .multiPartFiles(new MultiPartFiles().use(
                "multiPartFile",
                FileResourceUtil.copyIntoTempFile(usersBatch, null, "testUsersBatch.csv")
            ))
            .inlineVariables(customerIdentity);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Uploads batch users file via CAS API
     *
     * @param customerIdentity - customerIdentity
     * @param fileName         - name of file
     * @return new object
     */
    public ResponseWrapper<PostBatch> addInvalidBatchFile(
        String customerIdentity,
        String fileName,
        UserCredentials currentUser) {

        RequestEntity requestEntity = requestEntityUtil.init(CASCustomerEnum.CUSTOMERS_BATCH, PostBatch.class)
            .token(currentUser.getToken())
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .multiPartFiles(new MultiPartFiles().use("multiPartFile", FileResourceUtil.getResourceAsFile(fileName)))
            .inlineVariables(customerIdentity);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Creates role for a user
     *
     * @param customerIdentity - customer identity
     * @param userIdentity     - user identity
     * @return new object
     */
    public ResponseWrapper<UserRole> createRoleForUser(String customerIdentity, String userIdentity, String role) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.USER_ROLES, UserRole.class)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "role",
                UserRole.builder()
                    .role(role)
                    .createdBy("#SYSTEM00000")
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Creates invalid role for a user and get error response
     *
     * @param customerIdentity - customer identity
     * @param userIdentity     - user identity
     * @return new object
     */
    public ErrorResponse createInvalidRoleForUser(String customerIdentity, String userIdentity, String role) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.USER_ROLES, ErrorResponse.class)
            .inlineVariables(customerIdentity, userIdentity)
            .body(
                "role",
                UserRole.builder()
                    .role(role)
                    .createdBy("#SYSTEM00000")
                    .build()
            )
            .expectedResponseCode(SC_NOT_FOUND);
        ResponseWrapper<ErrorResponse> errorResponse = HTTPRequest.build(requestEntity).post();

        return errorResponse.getResponseEntity();
    }

    /**
     * call the endpoint /roles with param pageSize=20 - to get all roles
     *
     * @param inlineVariables
     * @return object ResponseWrapper
     */
    public Roles getRoles(QueryParams queryParams, String... inlineVariables) {

        final RequestEntity requestEntity = requestEntityUtil
            .init(CDSAPIEnum.ROLES, Roles.class)
            .inlineVariables(inlineVariables)
            .queryParams(queryParams)
            .expectedResponseCode(HttpStatus.SC_OK);
        return (Roles) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * GET user by email
     *
     * @param email email of the user
     * @return response object
     */
    public ResponseWrapper<Users> getUserByEmail(String email) {

        final RequestEntity requestEntity =
            requestEntityUtil.init(CDSAPIEnum.USERS, Users.class)
                .queryParams(new QueryParams().use("email[EQ]", email))
                .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * GET enablement
     *
     * @return response object
     */
    public ResponseWrapper<Enablements> getEnablement(User user) {

        final RequestEntity requestEntity =
            requestEntityUtil.init(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class)
                .inlineVariables(user.getCustomerIdentity(), user.getIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Creates or updates user enablements
     *
     * @param customerIdentity     - customer identity
     * @param userIdentity         - user identity
     * @param customerAssignedRole - customerAssignedRole
     * @param highMem              - true or false
     * @return new object
     */
    public ResponseWrapper<Enablements> createUpdateEnablements(
        String customerIdentity,
        String userIdentity,
        String customerAssignedRole,
        boolean highMem,
        boolean sandbox,
        boolean preview) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class)
            .inlineVariables(customerIdentity, userIdentity)
            .body(
                "enablements",
                Enablements.builder()
                    .customerAssignedRole(customerAssignedRole)
                    .highMemEnabled(highMem)
                    .sandboxEnabled(sandbox)
                    .previewEnabled(preview)
                    .createdBy("#SYSTEM00000")
                    .updatedBy("#SYSTEM00000")
                    .build()
            )
            .expectedResponseCode(SC_CREATED);

        return HTTPRequest.build(requestEntity).put();
    }
}