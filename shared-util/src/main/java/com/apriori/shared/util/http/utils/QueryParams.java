package com.apriori.shared.util.http.utils;

import org.apache.hc.core5.http.ContentType;

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

    /**
     * Accepts key, value of mime type
     *
     * @param key   - the key
     * @param value - the value
     * @return current object
     */
    public QueryParams use(String key, ContentType value) {
        this.put(key, value.getMimeType());
        return this;
    }
}
