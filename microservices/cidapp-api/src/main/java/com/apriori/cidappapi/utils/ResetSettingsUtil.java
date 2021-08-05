package com.apriori.cidappapi.utils;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.response.preferences.PreferenceItemsResponse;
import com.apriori.cidappapi.entity.response.preferences.PreferenceResponse;
import com.apriori.utils.enums.ColourEnum;
import com.apriori.utils.enums.UnitsEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.utils.users.UserCredentials;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResetSettingsUtil {

    /**
     * Resets all settings in Cidapp
     *
     * @param userCredentials - the user credentials
     * @return response object
     */
    public ResponseWrapper<String> resetSettings(UserCredentials userCredentials) {
        String token = userCredentials == null ? new JwtTokenUtil().retrieveJwtToken() : new JwtTokenUtil(userCredentials).retrieveJwtToken();

        RequestEntity responseEntity = RequestEntityUtil.init(CidAppAPIEnum.GET_PREFERENCES, PreferenceItemsResponse.class)
            .token(token);

        ResponseWrapper<PreferenceItemsResponse> preferencesResponse = HTTP2Request.build(responseEntity).get();

        List<PreferenceResponse> preferencesItems = preferencesResponse.getResponseEntity().getItems();

        Map<String, String> mappedResponse = new HashMap<>();

        preferencesItems.forEach(x -> mappedResponse.put(x.getName(), x.getIdentity()));

        String unitIdentity = mappedResponse.get("display.unitsGroup");
        String colourIdentity = mappedResponse.get("display.selectionColor");
        String scenarioIdentity = mappedResponse.get("production.defaultScenarioName");
        String proGroupIdentity = mappedResponse.get("production.defaultProcessGroup");
        String digFacIdentity = mappedResponse.get("production.defaultDigitalFactory");
        String matCatalogIdentity = mappedResponse.get("production.defaultMaterialCatalogName");
        String annVolIdentity = mappedResponse.get("production.defaultAnnualVolume");
        String prodLifeIdentity = mappedResponse.get("production.defaultProductionLife");
        String batchIdentity = mappedResponse.get("production.defaultBatchSize");
        String tolModeIdentity = mappedResponse.get("tolerance.toleranceMode");

        RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.PATCH_PREFERENCES, null)
            .token(token)
            .customBody("{\"userPreferences\": {"
                + "\"" + unitIdentity + "\":\"" + UnitsEnum.MMKS.getUnits() + "\","
                + "\"" + colourIdentity + "\":\"" + ColourEnum.YELLOW.getColour() + "\","
                + "\"" + scenarioIdentity + "\":\"Initial\","
                + "\"" + proGroupIdentity + "\":\"" + null + "\","
                + "\"" + digFacIdentity + "\":\"" + null + "\","
                + "\"" + matCatalogIdentity + "\":\"" + null + "\","
                + "\"" + annVolIdentity + "\":\"" + null + "\","
                + "\"" + prodLifeIdentity + "\":\"" + null + "\","
                + "\"" + batchIdentity + "\":\"" + null + "\","
                + "\"" + tolModeIdentity + "\":\"SYSTEMDEFAULT\""
                + "}}");

        return HTTP2Request.build(requestEntity).patch();
    }
}
