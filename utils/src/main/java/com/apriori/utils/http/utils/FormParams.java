package com.apriori.utils.http.utils;

import java.util.HashMap;

public class FormParams extends HashMap<String, String> {

    public FormParams use(String key, String value) {
        this.put(key, value);
        return this;
    }
}
