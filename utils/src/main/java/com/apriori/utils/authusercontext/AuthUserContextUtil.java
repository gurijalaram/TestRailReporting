package com.apriori.utils.authusercontext;

import com.apriori.utils.enums.AuthUserContextEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

@Slf4j
public class AuthUserContextUtil {

    public String getAuthUserContext() {
        RequestEntity requestEntity = RequestEntityUtil.init(AuthUserContextEnum.GET_AUTH_USER_CONTEXT, User.class)
            .inlineVariables("1EK3A4AD76G2");

        ResponseWrapper<User> response = HTTPRequest.build(requestEntity).get();
        return new String(Base64.encodeBase64(new Gson().toJson(response.getResponseEntity()).getBytes()), StandardCharsets.UTF_8);
    }
}
