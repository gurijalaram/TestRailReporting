package com.apriori.ats.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.entity.request.AuthenticateRequest;
import com.apriori.ats.entity.request.CreateSamlUserRequest;
import com.apriori.ats.entity.request.ResetAutoUsers;
import com.apriori.ats.entity.request.ResetMFA;
import com.apriori.ats.entity.response.UserByEmail;
import com.apriori.ats.utils.enums.ATSAPIEnum;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

public class AtsTestUtil extends TestUtil {

    /**
     * Authenticates a user
     *
     * @param email - user email
     * @param password - user password
     * @return ResponseWrapper <UserByEmail>
     */
    public ResponseWrapper<UserByEmail> authenticateUser(String email, String password) {
        RequestEntity requestEntity = RequestEntityUtil.init(ATSAPIEnum.AUTHENTICATE, UserByEmail.class)
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
     * @return ResponseWrapper <UserByEmail>
     */
    public ResponseWrapper<UserByEmail> putSAMLProviders(String email) {
        GenerateStringUtil generator = new GenerateStringUtil();
        String userName = generator.generateUserName();
        RequestEntity requestEntity = RequestEntityUtil.init(ATSAPIEnum.UPDATE_SAML_PROVIDERS, UserByEmail.class)
            .expectedResponseCode(HttpStatus.SC_OK)
            .body(CreateSamlUserRequest.builder()
                .email(userName + email + ".com")
                .given_name(generator.getRandomStringSpecLength(5))
                .family_name(generator.getRandomStringSpecLength(5))
                .name(userName)
                .build());

        return HTTPRequest.build(requestEntity).put();
    }

    /**
     * Resets customer users mfa or user mfa
     *
     * @param endpoint - customer users or particular user endpoint
     * @param identity - customer or user identity
     * @param status - response status code
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> resetUserMFA(EndpointEnum endpoint, String identity, Integer status) {
        RequestEntity requestEntity = RequestEntityUtil.init(endpoint, null)
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
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> changePassword(String email) {
        RequestEntity requestEntity = RequestEntityUtil.init(ATSAPIEnum.PATCH_USER_PASSWORD_BY_EMAIL, null)
            .inlineVariables(email)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT)
            .body(ResetAutoUsers.builder()
                .password(new GenerateStringUtil().getRandomPassword())
                .build());

        return HTTPRequest.build(requestEntity).patch();
    }
}