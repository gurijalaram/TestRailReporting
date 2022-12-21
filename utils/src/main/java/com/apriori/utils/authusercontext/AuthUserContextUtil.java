package com.apriori.utils.authusercontext;

import com.apriori.utils.enums.AuthUserContextEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

@Slf4j
public class AuthUserContextUtil {

    /**
     * GET authorisation User Context from cds
     *
     * @param email - the email
     * @return string
     */
    public String getAuthUserContext(String email) {
        RequestEntity userEntity = RequestEntityUtil.init(AuthUserContextEnum.GET_AUTH_USER_CONTEXT, Users.class)
            .queryParams(new QueryParams().use("email[EQ]", email));

        ResponseWrapper<Users> userResponse = HTTPRequest.build(userEntity).get();

        String identity = userResponse.getResponseEntity().getItems().get(0).getIdentity();

        RequestEntity idEntity = RequestEntityUtil.init(AuthUserContextEnum.GET_AUTH_USER_CONTEXT_BY_USERID, User.class)
            .inlineVariables(identity);

        ResponseWrapper<User> userIdResponse = HTTPRequest.build(idEntity).get();

        //Get the actual [User] object and store it as bytes. At this point we don't want the root name [response] to be included
        byte[] userIdBytes = new Gson().toJson(userIdResponse.getResponseEntity()).getBytes(StandardCharsets.UTF_8);

        //Encode the previously stored object in Base64
        byte[] encodedUserIdBytes = Base64.encodeBase64(userIdBytes);

        //Repackage the object into a string format
        return new String(encodedUserIdBytes);
    }


    /**
     * Get Authorization user Identity
     * @param email - the email
     * @return user Identity
     */
    public String getAuthUserIdentity(String email) {
        RequestEntity userEntity = RequestEntityUtil.init(AuthUserContextEnum.GET_AUTH_USER_CONTEXT, Users.class)
            .queryParams(new QueryParams().use("email[EQ]", email));

        ResponseWrapper<Users> userResponse = HTTPRequest.build(userEntity).get();

        return userResponse.getResponseEntity().getItems().get(0).getIdentity();
    }
}
