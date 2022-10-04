package com.apriori.utils.authorization;

import com.apriori.utils.enums.OldTokenEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class OldAuthorizationUtil {

    @Singular
    private List<Map<String, ?>> xwwwwFormUrlEncodeds = new ArrayList<>();
    private static final HashMap<String, String> headers = new HashMap<>();

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
        headers.put("Accept", "*/*");

        List<Map<String, ?>> requestData = new ArrayList<>();
        Map<String, String> requestData2 = new HashMap<>();
        requestData2.put("grant_type", "password");
        requestData2.put("username", user.getEmail());
        // TODO: 27/09/2022 cn - removed this substring, i don't see why its needed
        //requestData2.put("password", user.getEmail().substring(0, 16));
        requestData2.put("password", user.getUsername());
        requestData2.put("client_id", "apriori-web-cost");
        requestData2.put("client_secret", "donotusethiskey");
        requestData2.put("scope", "tenantGroup=default tenant=apriori-staging");
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
