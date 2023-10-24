package com.apriori.acs.utils;

import com.apriori.acs.OldTokenRequest;
import com.apriori.acs.enums.OldTokenEnum;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class OldAuthorizationUtil {

    private static final HashMap<String, String> headers = new HashMap<>();
    @Singular
    private List<Map<String, ?>> xwwwwFormUrlEncodeds = new ArrayList<>();

    public OldAuthorizationUtil() {
    }

    /**
     * POST to get a JWT token
     *
     * @return string
     */
    private OldTokenRequest getToken() {
        log.info("Getting Token from old CID API...");

        UserCredentials user = UserUtil.getUser();

        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept", "application/json");

        List<Map<String, ?>> requestData = new ArrayList<>();
        Map<String, String> requestData2 = new HashMap<>();
        requestData2.put("grant_type", "password");
        requestData2.put("username", user.getEmail());
        requestData2.put("password", user.getUsername());
        requestData2.put("client_id", "apriori-web-cost");
        requestData2.put("client_secret", "donotusethiskey");
        requestData2.put("scope", "tenantGroup=default tenant=default");
        requestData.add(requestData2);

        RequestEntity requestEntity = RequestEntityUtil.init(OldTokenEnum.POST_TOKEN, OldTokenRequest.class)
            .headers(headers)
            .xwwwwFormUrlEncodeds(null)
            .urlParams(requestData);

        return (OldTokenRequest) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    public String getTokenAsString() {
        return getToken().getAccessToken();
    }
}