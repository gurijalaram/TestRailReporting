package com.apriori.edc.utils;

import com.apriori.TestUtil;
import com.apriori.edc.enums.EDCAPIEnum;
import com.apriori.edc.models.response.users.Users;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

public class UsersUtil extends TestUtil {

    /**
     * GET the current user
     *
     * @return users object
     */
    public Users getCurrentUser() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.CURRENT_USER, Users.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Users> getUserResponse = HTTPRequest.build(requestEntity).get();

        return getUserResponse.getResponseEntity();
    }
}
