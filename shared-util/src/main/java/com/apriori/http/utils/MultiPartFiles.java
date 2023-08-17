package com.apriori.http.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * @author kpatel
 */
public class MultiPartFiles {
    private HashMap<String, File> multiPartsFileData = new HashMap<>();
    private Multimap<String, File> multiPartsFilesData = ArrayListMultimap.create();
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
        values.forEach(value -> multiPartsFilesData.put(key, value));
        return this;
    }

    public HashMap<String, File> getMultiPartsFileData() {
        return multiPartsFileData;
    }

    public HashMap<String, String> getMultiPartsTextData() {
        return multiPartsTextData;
    }

    public Multimap<String, File> getMultiPartsFilesData() {
        return multiPartsFilesData;
    }
}