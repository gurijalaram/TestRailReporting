package com.apriori.shared.util.http.utils;

import org.apache.hc.core5.http.ContentType;

import java.util.HashMap;

public class ContentParams extends HashMap<String, String> {

    /**
     * Puts the mime type
     *
     * @param key   - the key
     * @param value - the value
     * @return current object
     */
    public ContentParams use(String key, ContentType value) {
        this.put(key, value.getMimeType());
        return this;
    }

    /**
     * Puts the mime type
     *
     * @param key   - the key
     * @param value - the value
     * @return current object
     */
    public ContentParams use(String key, String value) {
        this.put(key, value);
        return this;
    }
}
