package com.apriori.trr.api.testrail.http.utils;

import java.util.HashMap;
import java.util.Map;

public class QueryParams extends HashMap<String, String> {

    /**
     * Accepts key, value
     *
     * @param key   - the key
     * @param value - the value
     * @return current object
     */
    public QueryParams use(String key, String value) {
        this.put(key, value);
        return this;
    }

    /**
     * Accepts map of key, value
     *
     * @param paramMap - the parameter map
     * @return current object
     */
    public QueryParams use(Map<String, String> paramMap) {
        this.putAll(paramMap);
        return this;
    }
}
