package com.apriori.apibase.utils;

import java.io.File;
import java.util.HashMap;

/**
 * @author kpatel
 */
public class MultiPartFiles extends HashMap<String, File> {

    public static MultiPartFiles params() {
        return new MultiPartFiles();
    }

    public MultiPartFiles use(String key, File value) {
        this.put(key, value);
        return this;
    }
}