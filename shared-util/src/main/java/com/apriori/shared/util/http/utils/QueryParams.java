package com.apriori.shared.util.http.utils;

import java.util.HashMap;
import java.util.Map;

public class QueryParams extends HashMap<String, String> {

    public QueryParams use(String key, String value) {
        this.put(key, value);
        return this;
    }

    public QueryParams use(Map<String, String> paramMap) {
        this.putAll(paramMap);
        return this;
    }
}
