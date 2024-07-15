package com.apriori.cds.api.utils;


import static org.apache.http.HttpStatus.SC_CREATED;

import com.apriori.cds.api.enums.CASCustomerEnum;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.request.AccessAuthorizationRequest;
import com.apriori.cds.api.models.request.AddDeployment;
import com.apriori.cds.api.models.request.PostBatch;
import com.apriori.cds.api.models.response.AccessAuthorization;
import com.apriori.cds.api.models.response.IdentityProviderRequest;
import com.apriori.cds.api.models.response.IdentityProviderResponse;
import com.apriori.cds.api.models.response.Roles;
import com.apriori.cds.api.models.response.UserPreference;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.MultiPartFiles;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.Enablements;
import com.apriori.shared.util.models.response.User;

import org.apache.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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