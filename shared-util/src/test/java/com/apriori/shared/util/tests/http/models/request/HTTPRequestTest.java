package com.apriori.shared.util.tests.http.models.request;

import com.apriori.shared.util.enums.AuthUserContextEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Users;

import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Test;


public class HTTPRequestTest {

    @Test
    public void getHttpRequest() {
        RequestEntity requestEntity = RequestEntityUtil.init(AuthUserContextEnum.GET_AUTH_USER_CONTEXT, Users.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper response = HTTPRequest.build(requestEntity).get();
    }

}
