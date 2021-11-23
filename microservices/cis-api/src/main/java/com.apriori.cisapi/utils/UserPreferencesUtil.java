package com.apriori.cisapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cisapi.entity.enums.CISAPIEnum;
import com.apriori.cisapi.entity.response.user.preferences.UserPreferencesItemsResponse;
import com.apriori.cisapi.entity.response.user.preferences.UserPreferencesResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

import java.util.List;

public class UserPreferencesUtil extends TestUtil {

    /**
     * Get all User Preferences
     *
     * @return response object
     */
    protected static List<UserPreferencesResponse> getUserPreferences() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CISAPIEnum.GET_USERS_CURRENT_PREFERENCES, UserPreferencesItemsResponse.class);

        ResponseWrapper<UserPreferencesItemsResponse> getAllResponses = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, getAllResponses.getStatusCode());

        return getAllResponses.getResponseEntity().getItems();
    }
}