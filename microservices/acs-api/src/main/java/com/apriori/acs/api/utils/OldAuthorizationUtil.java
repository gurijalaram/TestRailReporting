package com.apriori.acs.api.utils;

import com.apriori.acs.api.OldTokenRequest;
import com.apriori.acs.api.enums.OldTokenEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class OldAuthorizationUtil {

    private static final HashMap<String, String> headers = new HashMap<>();

    public OldAuthorizationUtil() {
    }

    /**
     * POST to get a JWT token
     *
     * @return string
     */
    private OldTokenRequest getToken(UserCredentials user) {
        log.info("Getting Token from old CID API...");

        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept", "application/json");

        List<Map<String, ?>> requestData = new ArrayList<>();
        Map<String, String> requestData2 = new HashMap<>();
        requestData2.put("grant_type", "password");
        requestData2.put("username", user.getEmail());
        requestData2.put("password", user.getEmail().split("@")[0]);
        requestData2.put("client_id", "apriori-web-cost");
        requestData2.put("client_secret", "donotusethiskey");
        requestData2.put("scope", "tenantGroup=default tenant=default");
        requestData.add(requestData2);

        RequestEntityUtil requestEntityUtil = new RequestEntityUtil(user);
        RequestEntity requestEntity = requestEntityUtil.init(OldTokenEnum.POST_TOKEN, OldTokenRequest.class)
            .headers(headers)
            .urlParams(requestData);

        return (OldTokenRequest) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    public String getTokenAsString(UserCredentials user) {
        return getToken(user).getAccessToken();
    }
}