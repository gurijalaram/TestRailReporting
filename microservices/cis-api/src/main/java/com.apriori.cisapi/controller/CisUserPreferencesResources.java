package com.apriori.cisapi.controller;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cisapi.entity.enums.CisAPIEnum;
import com.apriori.cisapi.entity.request.userpreferences.CurrentUserPreferenceParameters;
import com.apriori.cisapi.entity.request.userpreferences.CurrentUserPreferenceRequest;
import com.apriori.cisapi.entity.response.userpreferences.CurrentExtendedUserPreferencesResponse;
import com.apriori.cisapi.entity.response.userpreferences.ExtendedUserPreferencesResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

public class CisUserPreferencesResources extends TestUtil {

    /**
     * Get extended User Preferences
     *
     * @return response object
     */
    protected static CurrentExtendedUserPreferencesResponse getCurrentExtendedUserPreferences() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CisAPIEnum.USERS_CURRENT_EXTENDED_PREFERENCES, CurrentExtendedUserPreferencesResponse.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<CurrentExtendedUserPreferencesResponse> getAllResponses = HTTPRequest.build(requestEntity)
            .get();

        return getAllResponses.getResponseEntity();
    }

    /**
     * Get extended User Preferences
     *
     * @return response object
     */
    protected static ExtendedUserPreferencesResponse getLoggedExtendedUserPreferences() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CisAPIEnum.USERS_EXTENDED_PREFERENCES, ExtendedUserPreferencesResponse.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ExtendedUserPreferencesResponse> getAllResponses = HTTPRequest.build(requestEntity).get();

        return getAllResponses.getResponseEntity();
    }

    /**
     * Update extended User Preferences
     *
     * @return response object
     */
    protected static CurrentExtendedUserPreferencesResponse updateCurrentExtendedUserPreferences(String avatarColor) {
        CurrentUserPreferenceRequest currentUserPreferenceRequestBuilder = CurrentUserPreferenceRequest.builder()
            .userPreferences(CurrentUserPreferenceParameters.builder().avatarColor(avatarColor).build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.USERS_CURRENT_EXTENDED_PREFERENCES, CurrentExtendedUserPreferencesResponse.class)
            .body(currentUserPreferenceRequestBuilder)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<CurrentExtendedUserPreferencesResponse> getAllResponses = HTTPRequest.build(requestEntity)
            .patch();

        return getAllResponses.getResponseEntity();
    }
}
