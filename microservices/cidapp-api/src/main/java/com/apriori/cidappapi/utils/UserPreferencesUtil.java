package com.apriori.cidappapi.utils;

import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.response.preferences.PreferenceItemsResponse;
import com.apriori.cidappapi.entity.response.preferences.PreferenceResponse;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.enums.ColourEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.DecimalPlaceEnum;
import com.apriori.utils.enums.LengthEnum;
import com.apriori.utils.enums.MassEnum;
import com.apriori.utils.enums.PreferencesEnum;
import com.apriori.utils.enums.TimeEnum;
import com.apriori.utils.enums.UnitsEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        RequestEntity responseEntity = RequestEntityUtil.init(CidAppAPIEnum.PREFERENCES_PAGE_SIZE, PreferenceItemsResponse.class)
            .token(userCredentials.getToken());

        ResponseWrapper<PreferenceItemsResponse> preferencesResponse = HTTPRequest.build(responseEntity).get();

        List<PreferenceResponse> preferencesItems = preferencesResponse.getResponseEntity().getItems();

        PreferenceResponse preferenceResponse = preferencesItems.stream().filter(x -> x.getName().equals(preference.getPreference())).collect(Collectors.toList()).get(0);

        RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.PREFERENCES, null)
            .token(userCredentials.getToken())
            .customBody("{\"userPreferences\": {"
                + "\"" + preferenceResponse.getIdentity() + "\":\"" + value + "\""
                + "}}");

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Put/update preferences
     *
     * @param userCredentials - the user credentials
     * @param preferences      - the preferences to be updated
     *
     * @return response object
     */
    public ResponseWrapper<String> updatePreferences(UserCredentials userCredentials, Map<PreferencesEnum, String> preferences) {
        StringBuilder updatePreferences = new StringBuilder();
        PreferenceResponse preference;

        for (Map.Entry<PreferencesEnum, String> update : preferences.entrySet()) {

            preference = getPreference(userCredentials, update.getKey());

            updatePreferences
                .append(updatePreferences.length() > 0 ? "," : "")
                .append("{")
                .append("\"name\":\"").append(preference.getName()).append("\",")
                .append("\"type\":\"").append(preference.getType()).append("\",")
                .append("\"value\":") .append(preference.getType().equals("STRING") ? "\"" : "")
                .append(update.getValue()).append(preference.getType().equals("STRING") ? "\"" : "")
                .append(",\"updatedBy\":\"").append(preference.getUpdatedBy())
                .append("\"}");
        }

        RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.PREFERENCES, null)
            .token(userCredentials.getToken())
            .customBody("{\"userPreferences\": [ " + updatePreferences + " ]}");

        return HTTPRequest.build(requestEntity).put();
    }

    /**
     * Get the list of current preferences
     *
     * @param userCredentials - the user credentials
     *
     * @return List of preferences
     */
    public List<PreferenceResponse> getPreferences(UserCredentials userCredentials) {
        RequestEntity responseEntity = RequestEntityUtil.init(CidAppAPIEnum.PREFERENCES_PAGE_SIZE, PreferenceItemsResponse.class)
            .token(userCredentials.getToken());

        ResponseWrapper<PreferenceItemsResponse> preferencesResponse = HTTPRequest.build(responseEntity).get();

        return preferencesResponse.getResponseEntity().getItems();
    }

    /**
     * Get the list of current preferences
     *
     * @param userCredentials - the user credentials
     *
     * @return List of preferences
     */
    public PreferenceResponse getPreference(UserCredentials userCredentials, PreferencesEnum preference) {
        List<PreferenceResponse> preferencesItems = getPreferences(userCredentials);

//        return preferencesItems.stream().filter(x -> x.getName().equals(preference.getPreference())).collect(Collectors.toList()).get(0);
        return preferencesItems.stream()
            .filter(x -> x.getName().equals(preference.getPreference()))
            .findFirst()
            .orElse(null);
    }

    /**
     * Resets all settings in Cidapp
     *
     * @param userCredentials - the user credentials
     * @return response object
     */
    public ResponseWrapper<String> resetSettings(UserCredentials userCredentials) {
        String token = new AuthorizationUtil(userCredentials).getTokenAsString();

        RequestEntity responseEntity = RequestEntityUtil.init(CidAppAPIEnum.PREFERENCES_PAGE_SIZE, PreferenceItemsResponse.class)
            .token(token);

        ResponseWrapper<PreferenceItemsResponse> preferencesResponse = HTTPRequest.build(responseEntity).get();

        List<PreferenceResponse> preferencesItems = preferencesResponse.getResponseEntity().getItems();

        Map<String, String> mappedResponse = new HashMap<>();

        preferencesItems.forEach(x -> mappedResponse.put(x.getName(), x.getIdentity()));

        String asmStrategyIdentity = mappedResponse.get(PreferencesEnum.ASSEMBLY_STRATEGY.getPreference());
        String areaIdentity = mappedResponse.get(PreferencesEnum.AREA_UNITS.getPreference());
        String currencyIdentity = mappedResponse.get(PreferencesEnum.CURRENCY.getPreference());
        String unitIdentity = mappedResponse.get(PreferencesEnum.UNITS_GROUP.getPreference());
        String lengthIdentity = mappedResponse.get(PreferencesEnum.LENGTH_UNITS.getPreference());
        String massIdentity = mappedResponse.get(PreferencesEnum.MASS_UNITS.getPreference());
        String timeIdentity = mappedResponse.get(PreferencesEnum.TIME_UNITS.getPreference());
        String decimalIdentity = mappedResponse.get(PreferencesEnum.DECIMAL_PLACES.getPreference());
        String languageIdentity = mappedResponse.get(PreferencesEnum.LANGUAGE.getPreference());
        String colourIdentity = mappedResponse.get(PreferencesEnum.SELECTION_COLOUR.getPreference());
        String scenarioIdentity = mappedResponse.get(PreferencesEnum.DEFAULT_SCENARIO_NAME.getPreference());
        String proGroupIdentity = mappedResponse.get(PreferencesEnum.DEFAULT_PROCESS_GROUP.getPreference());
        String digFacIdentity = mappedResponse.get(PreferencesEnum.DEFAULT_DIGITAL_FACTORY.getPreference());
        String matCatalogIdentity = mappedResponse.get(PreferencesEnum.DEFAULT_MATERIAL_CATALOG_NAME.getPreference());
        String cadThresholdIdentity = mappedResponse.get(PreferencesEnum.CAD_TOLERANCE_THRESHOLD.getPreference());
        String materialIdentity = mappedResponse.get(PreferencesEnum.DEFAULT_MATERIAL_NAME.getPreference());
        String annVolIdentity = mappedResponse.get(PreferencesEnum.DEFAULT_ANNUAL_VOLUME.getPreference());
        String prodLifeIdentity = mappedResponse.get(PreferencesEnum.DEFAULT_PRODUCTION_LIFE.getPreference());
        String batchIdentity = mappedResponse.get(PreferencesEnum.DEFAULT_BATCH_SIZE.getPreference());
        String tolModeIdentity = mappedResponse.get(PreferencesEnum.TOLERANCE_MODE.getPreference());

        RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.PREFERENCES, null)
            .token(token)
            .customBody("{\"userPreferences\": {"
                + "\"" + asmStrategyIdentity + "\":\"\","
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
                + "\"" + materialIdentity + "\":\"Use Default\","
                + "\"" + annVolIdentity + "\":\"" + 5500 + "\","
                + "\"" + prodLifeIdentity + "\":\"" + 5 + "\","
                + "\"" + batchIdentity + "\":\"" + 458 + "\","
                + "\"" + cadThresholdIdentity + "\":\"" + false + "\","
                + "\"" + tolModeIdentity + "\":\"SYSTEMDEFAULT\""
                + "}}");

        return HTTPRequest.build(requestEntity).patch();
    }
}
