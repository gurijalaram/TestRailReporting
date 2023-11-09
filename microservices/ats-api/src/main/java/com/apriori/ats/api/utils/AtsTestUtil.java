package com.apriori.ats.api.utils;

import com.apriori.ats.api.models.request.AuthenticateRequest;
import com.apriori.ats.api.models.request.CreateSamlUserRequest;
import com.apriori.ats.api.models.request.ResetAutoUsers;
import com.apriori.ats.api.models.request.ResetMFA;
import com.apriori.ats.api.utils.enums.ATSAPIEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.interfaces.EndpointEnum;
import com.apriori.shared.util.models.response.User;

import org.apache.http.HttpStatus;

public class AtsTestUtil extends TestUtil {

    /**
     * Authenticates a user
     *
     * @param email - user email
     * @param password - user password
     * @return ResponseWrapper <UserByEmail>
     */
    public ResponseWrapper<User> authenticateUser(String email, String password) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(ATSAPIEnum.AUTHENTICATE, User.class)
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
    public ResponseWrapper<User> putSAMLProviders(String email) {
        GenerateStringUtil generator = new GenerateStringUtil();
        String userName = generator.generateUserName();
        RequestEntity requestEntity = RequestEntityUtil_Old.init(ATSAPIEnum.SAML_PROVIDERS, User.class)
            .expectedResponseCode(HttpStatus.SC_OK)
            .body(CreateSamlUserRequest.builder()
                .email(userName + email + ".com")
                .given_name(generator.getRandomStringSpecLength(5))
                .family_name(generator.getRandomStringSpecLength(5))
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
     * @param status - response status code
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> resetUserMFA(EndpointEnum endpoint, String identity, Integer status) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(endpoint, null)
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
        RequestEntity requestEntity = RequestEntityUtil_Old.init(ATSAPIEnum.USER_PASSWORD_BY_EMAIL, null)
            .inlineVariables(email)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT)
            .body(ResetAutoUsers.builder()
                .password(new GenerateStringUtil().getRandomPassword())
                .build());

        return HTTPRequest.build(requestEntity).patch();
    }
}