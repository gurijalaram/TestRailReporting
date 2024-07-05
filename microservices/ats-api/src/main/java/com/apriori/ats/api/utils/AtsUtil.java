package com.apriori.ats.api.utils;

import com.apriori.ats.api.models.request.AuthenticateRequest;
import com.apriori.ats.api.models.request.AuthorizeRequest;
import com.apriori.ats.api.models.request.CreateSamlUserRequest;
import com.apriori.ats.api.models.request.ResetAutoUsers;
import com.apriori.ats.api.models.request.ResetMFA;
import com.apriori.ats.api.models.response.AuthorizationResponse;
import com.apriori.ats.api.utils.enums.ATSAPIEnum;
import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.enums.TokenEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.interfaces.EndpointEnum;
import com.apriori.shared.util.models.request.TokenRequest;
import com.apriori.shared.util.models.response.Claims;
import com.apriori.shared.util.models.response.Token;
import com.apriori.shared.util.models.response.TokenInformation;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

@Slf4j
public class AtsUtil extends TestUtil {
    private RequestEntityUtil requestEntityUtil;

    public AtsUtil(RequestEntityUtil requestEntityUtil) {
        super.requestEntityUtil = requestEntityUtil;
        this.requestEntityUtil = requestEntityUtil;
    }

    /**
     * Authenticates a user
     *
     * @param email    - user email
     * @param password - user password
     * @return response object
     */
    public ResponseWrapper<User> authenticateUser(String email, String password) {
        RequestEntity requestEntity = requestEntityUtil.init(ATSAPIEnum.AUTHENTICATE, User.class)
            .expectedResponseCode(HttpStatus.SC_OK)
            .body(AuthenticateRequest.builder()
                .email(email)
                .password(password)
                .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Creates user with SAML
     *
     * @param email - user email
     * @return response object
     */
    public ResponseWrapper<User> samlProviders(String email) {
        GenerateStringUtil generator = new GenerateStringUtil();
        String userName = generator.generateUserName();

        RequestEntity requestEntity = requestEntityUtil.init(ATSAPIEnum.SAML_PROVIDERS, User.class)
            .expectedResponseCode(HttpStatus.SC_OK)
            .body(CreateSamlUserRequest.builder()
                .email(userName + "@" + email + ".com")
                .givenName(generator.getRandomStringSpecLength(5))
                .familyName(generator.getRandomStringSpecLength(5))
                .name(userName)
                .roles("APRIORI_USER")
                .build());

        return HTTPRequest.build(requestEntity).put();
    }

    /**
     * Resets customer users mfa or user mfa
     *
     * @param endpoint - customer users or particular user endpoint
     * @param identity - customer or user identity
     * @param status   - response status code
     * @return generic response object
     */
    public <T> ResponseWrapper<T> resetUserMFA(EndpointEnum endpoint, String identity, Integer status) {
        RequestEntity requestEntity = requestEntityUtil.init(endpoint, null)
            .inlineVariables(identity)
            .expectedResponseCode(status)
            .body(ResetMFA.builder()
                .resetBy("#SYSTEM00000")
                .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Changes a password of user
     *
     * @param email - user email
     * @return generic response object
     */
    public <T> ResponseWrapper<T> changePassword(String email) {
        RequestEntity requestEntity = requestEntityUtil.init(ATSAPIEnum.USER_PASSWORD_BY_EMAIL, null)
            .inlineVariables(email)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT)
            .body(ResetAutoUsers.builder()
                .password(new GenerateStringUtil().getRandomPassword())
                .build());

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * POST authorize user
     *
     * @param targetCloudContext - target cloud context
     * @return authorization object
     */
    public ResponseWrapper<AuthorizationResponse> authorizeUser(String targetCloudContext) {
        RequestEntity requestEntity = requestEntityUtil.init(ATSAPIEnum.POST_AUTHORIZE_BY_BASE_URL_SECRET, AuthorizationResponse.class)
            .body(AuthorizeRequest.builder().targetCloudContext(targetCloudContext)
                .token(requestEntityUtil.getEmbeddedUser().getToken())
                .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST to get a JWT token
     *
     * @return string
     */
    public synchronized ResponseWrapper<Token> getToken() {
        log.info("Getting ATS Token...");

        RequestEntity requestEntity = requestEntityUtil.init(TokenEnum.POST_TOKEN, Token.class)
            .body(TokenRequest.builder()
                .token(TokenInformation.builder()
                    .issuer(PropertiesContext.get("ats.token_issuer"))
                    .subject(SharedCustomerUtil.getTokenSubjectForCustomer())
                    .claims(Claims.builder()
                        .name(requestEntityUtil.getEmbeddedUser().getUsername())
                        .email(requestEntityUtil.getEmbeddedUser().getEmail())
                        .build())
                    .build())
                .build())
            .expectedResponseCode(org.apache.hc.core5.http.HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }
}