package com.apriori.utils.http.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kpatel
 */
public class MultiPartFiles {
    private HashMap<String, File> multiPartsFileData = new HashMap<>();
    private Map<String, List<File>> multiPartsFilesData = new HashMap<>();
    private HashMap<String, String> multiPartsTextData = new HashMap<>();

    public MultiPartFiles use(String key, File value) {
        multiPartsFileData.put(key, value);
        return this;
    }

    public MultiPartFiles use(String key, String value) {
        multiPartsTextData.put(key, value);
        return this;
    }

    public MultiPartFiles use(String key, List<File> values) {
        values.forEach(value -> multiPartsFilesData.computeIfAbsent(key,  k -> new ArrayList<>()).add(value));
        return this;
    }

    public HashMap<String, File> getMultiPartsFileData() {
        return multiPartsFileData;
    }

    public HashMap<String, String> getMultiPartsTextData() {
        return multiPartsTextData;
    }

    public Map<String, List<File>> getMultiPartsFilesData() {
        return multiPartsFilesData;
    }
}