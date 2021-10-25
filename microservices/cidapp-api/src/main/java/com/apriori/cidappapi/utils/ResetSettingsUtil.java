package com.apriori.cidappapi.utils;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.response.preferences.PreferenceItemsResponse;
import com.apriori.cidappapi.entity.response.preferences.PreferenceResponse;
import com.apriori.utils.enums.ColourEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.DecimalPlaceEnum;
import com.apriori.utils.enums.LengthEnum;
import com.apriori.utils.enums.MassEnum;
import com.apriori.utils.enums.TimeEnum;
import com.apriori.utils.enums.UnitsEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
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

        ResponseWrapper<PreferenceItemsResponse> preferencesResponse = HTTPRequest.build(responseEntity).get();

        List<PreferenceResponse> preferencesItems = preferencesResponse.getResponseEntity().getItems();

        Map<String, String> mappedResponse = new HashMap<>();

        preferencesItems.forEach(x -> mappedResponse.put(x.getName(), x.getIdentity()));

        String areaIdentity = mappedResponse.get("display.areaUnits");
        String currencyIdentity = mappedResponse.get("display.currency");
        String unitIdentity = mappedResponse.get("display.unitsGroup");
        String lengthIdentity = mappedResponse.get("display.lengthUnits");
        String massIdentity = mappedResponse.get("display.massUnits");
        String timeIdentity = mappedResponse.get("display.timeUnits");
        String decimalIdentity = mappedResponse.get("display.decimalPlaces");
        String languageIdentity = mappedResponse.get("display.language");
        String colourIdentity = mappedResponse.get("display.selectionColor");
        String scenarioIdentity = mappedResponse.get("production.defaultScenarioName");
        String proGroupIdentity = mappedResponse.get("production.defaultProcessGroup");
        String digFacIdentity = mappedResponse.get("production.defaultDigitalFactory");
        String matCatalogIdentity = mappedResponse.get("production.defaultMaterialCatalogName");
        String annVolIdentity = mappedResponse.get("production.defaultAnnualVolume");
        String prodLifeIdentity = mappedResponse.get("production.defaultProductionLife");
        String batchIdentity = mappedResponse.get("production.defaultBatchSize");
        String tolModeIdentity = mappedResponse.get("tolerance.toleranceMode");
        String cadThresholdIdentity = mappedResponse.get("tolerance.useCadToleranceThreshold");

        RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.PATCH_PREFERENCES, null)
            .token(token)
            .customBody("{\"userPreferences\": {"
                + "\"" + areaIdentity + "\":\"mm2\","
                + "\"" + currencyIdentity + "\":\"" + CurrencyEnum.USD.getCurrency() + "\","
                + "\"" + unitIdentity + "\":\"" + UnitsEnum.MMKS.getUnits() + "\","
                + "\"" + lengthIdentity + "\":\"" + LengthEnum.MILLIMETER.getLength() + "\","
                + "\"" + massIdentity + "\":\"" + MassEnum.KILOGRAM.getMass() + "\","
                + "\"" + timeIdentity + "\":\"" + TimeEnum.SECOND.getTime() + "\","
                + "\"" + decimalIdentity + "\":\"" + DecimalPlaceEnum.TWO.getDecimalPlaces() + "\","
                + "\"" + languageIdentity + "\":\"en\","
                + "\"" + colourIdentity + "\":\"" + ColourEnum.YELLOW.getColour() + "\","
                + "\"" + scenarioIdentity + "\":\"Initial\","
                + "\"" + proGroupIdentity + "\":\"" + null + "\","
                + "\"" + digFacIdentity + "\":\"" + null + "\","
                + "\"" + matCatalogIdentity + "\":\"" + null + "\","
                + "\"" + annVolIdentity + "\":\"" + null + "\","
                + "\"" + prodLifeIdentity + "\":\"" + null + "\","
                + "\"" + batchIdentity + "\":\"" + null + "\","
                + "\"" + tolModeIdentity + "\":\"SYSTEMDEFAULT\","
                + "\"" + cadThresholdIdentity + "\":\"" + false + "\""
                + "}}");

        return HTTPRequest.build(requestEntity).patch();
    }
}
