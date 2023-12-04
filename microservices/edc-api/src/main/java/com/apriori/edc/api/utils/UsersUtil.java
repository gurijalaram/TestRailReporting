package com.apriori.edc.api.utils;

import com.apriori.edc.api.enums.EDCAPIEnum;
import com.apriori.edc.api.models.response.users.Users;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;

import org.apache.http.HttpStatus;

public class UsersUtil extends TestUtil {

    /**
     * GET the current user
     *
     * @return users object
     */
    public Users getCurrentUser() {
        RequestEntity requestEntity =
            RequestEntityUtil_Old.init(EDCAPIEnum.CURRENT_USER, Users.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Users> getUserResponse = HTTPRequest.build(requestEntity).get();

        return getUserResponse.getResponseEntity();
    }
}
