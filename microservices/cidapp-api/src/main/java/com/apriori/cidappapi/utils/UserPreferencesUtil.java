package com.apriori.cidappapi.utils;

import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.response.preferences.PreferenceItemsResponse;
import com.apriori.cidappapi.entity.response.preferences.PreferenceResponse;
import com.apriori.utils.enums.PreferencesEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import java.util.List;
import java.util.stream.Collectors;

public class UserPreferencesUtil {

    /**
     * Patch/update a preference
     *
     * @param userCredentials - the user credentials
     * @param preference      - the preference
     * @param value           - the value
     * @return response object
     */
    public ResponseWrapper<String> patchPreference(UserCredentials userCredentials, PreferencesEnum preference, String value) {
        RequestEntity responseEntity = RequestEntityUtil.init(CidAppAPIEnum.GET_PREFERENCES, PreferenceItemsResponse.class)
            .token(userCredentials.getToken());

        ResponseWrapper<PreferenceItemsResponse> preferencesResponse = HTTPRequest.build(responseEntity).get();

        List<PreferenceResponse> preferencesItems = preferencesResponse.getResponseEntity().getItems();

        PreferenceResponse prefItem = preferencesItems.stream().filter(x -> x.getName().equals(preference.getPreference())).collect(Collectors.toList()).get(0);

        RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.PATCH_PREFERENCES, null)
            .token(userCredentials.getToken())
            .customBody("{\"userPreferences\": {"
                + "\"" + prefItem.getIdentity() + "\":\"" + value + "\""
                + "}}");

        return HTTPRequest.build(requestEntity).patch();
    }
}
