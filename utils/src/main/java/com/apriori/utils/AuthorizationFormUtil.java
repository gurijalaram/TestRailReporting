package com.apriori.utils;

import java.util.HashMap;
import java.util.Map;

public class AuthorizationFormUtil {

    public static Map<String, String> getTokenAuthorizationForm(final String token) {
        return new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
                put("apriori.tenantGroup", "default");
                put("apriori.tenant", "default");
            }};
    }

    public static Map<String, String> getDefaultAuthorizationForm(final String username, final String password) {
        return new HashMap<String, String>() {{
                put("grant_type", "password");
                put("client_id", "apriori-web-cost");
                put("client_secret", "donotusethiskey");
                put("scope", "tenantGroup%3Ddefault%20tenant%3Ddefault");
                put("username", username);
                put("password", password);
            }};
    }
}
