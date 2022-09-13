package com.apriori.edcapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.response.users.Users;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

public class UsersUtil extends TestUtil {

    /**
     * GET the current user
     *
     * @return users object
     */
    public Users getCurrentUser() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.GET_CURRENT_USER, Users.class);

        ResponseWrapper<Users> getUserResponse = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, getUserResponse.getStatusCode());

        return getUserResponse.getResponseEntity();
    }
}
