package com.apriori.http.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author kpatel
 */
public class URLParams extends HashMap<String, Object> {

    public static URLParams params() {
        return new URLParams();
    }

    public URLParams use(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public URLParams useIf(String key, Object value, Predicate<Object> pred) {
        if (pred.test(value)) {
            this.put(key, value);
        }
        return this;
    }

    public URLParams useAll(URLParams params) {
        this.putAll(params);
        return this;
    }

    public URLParams useAll(Map params) {
        this.putAll(params);
        return this;
    }
}
