package com.apriori.utils.http.utils;

import java.util.HashMap;
import java.util.Map;

public class FormParams extends HashMap<String, String> {

    public FormParams use(String key, String value) {
        this.put(key, value);
        return this;
    }

    public FormParams use(Map<String, String> paramMap) {
        this.putAll(paramMap);
        return this;
    }
}
