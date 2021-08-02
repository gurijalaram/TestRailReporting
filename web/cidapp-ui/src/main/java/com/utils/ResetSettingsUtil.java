package com.utils;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.response.preferences.PreferenceItemsResponse;
import com.apriori.cidappapi.entity.response.preferences.PreferenceResponse;
import com.apriori.utils.enums.UnitsEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResetSettingsUtil {

    UserCredentials userCredentials = UserUtil.getUser();
    String token = userCredentials == null ? new JwtTokenUtil().retrieveJwtToken() : new JwtTokenUtil(userCredentials).retrieveJwtToken();
    private String unitIdentity;
    private String colourIdentity;
    private String proGroupIdentity;
    private String digFacIdentity;
    private String matCatalogIdentity;
    private String annVolIdentity;
    private String prodLifeIdentity;
    private String batchIdentity;
    private String tolModeIdentity;
    private String scenarioIdentity;

    public ResponseWrapper<Object> resetSettings() {
        RequestEntity responseEntity = RequestEntityUtil.init(CidAppAPIEnum.GET_PREFERENCES, PreferenceItemsResponse.class)
            .token(token);

        ResponseWrapper<PreferenceItemsResponse> preferencesResponse = HTTP2Request.build(responseEntity).get();

        Stream<PreferenceResponse> stream = preferencesResponse.getResponseEntity().getItems().stream();

        unitIdentity = stream.filter(x -> x.getName().equals("display.unitsGroup")).collect(Collectors.toList()).get(0).getIdentity();
        colourIdentity = stream.filter(x -> x.getName().equals("display.selectionColor")).collect(Collectors.toList()).get(0).getIdentity();
        scenarioIdentity = stream.filter(x -> x.getName().equals("production.defaultScenarioName")).collect(Collectors.toList()).get(0).getIdentity();
        proGroupIdentity = stream.filter(x -> x.getName().equals("production.defaultProcessGroup")).collect(Collectors.toList()).get(0).getIdentity();
        digFacIdentity = stream.filter(x -> x.getName().equals("production.defaultDigitalFactory")).collect(Collectors.toList()).get(0).getIdentity();
        matCatalogIdentity = stream.filter(x -> x.getName().equals("production.defaultMaterialCatalogName")).collect(Collectors.toList()).get(0).getIdentity();
        annVolIdentity = stream.filter(x -> x.getName().equals("production.defaultAnnualVolume")).collect(Collectors.toList()).get(0).getIdentity();
        prodLifeIdentity = stream.filter(x -> x.getName().equals("production.defaultProductionLife")).collect(Collectors.toList()).get(0).getIdentity();
        batchIdentity = stream.filter(x -> x.getName().equals("production.defaultBatchSize")).collect(Collectors.toList()).get(0).getIdentity();
        tolModeIdentity = stream.filter(x -> x.getName().equals("tolerance.toleranceMode")).collect(Collectors.toList()).get(0).getIdentity();

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
                + "\"" + tolModeIdentity + "\":\" SYSTEMDEFAULT \""
                + "}}");

        return HTTP2Request.build(requestEntity).patch();
    }
}
