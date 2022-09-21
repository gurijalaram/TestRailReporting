package com.apriori.edcapi.utils;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.json.utils.JsonManager;

public class BodyInformationUtil {

    /**
     * Common method to input the body info
     *
     * @return response object
     */
    protected static Object postBodyInformation(String filename, Class klass) {
        return JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile(filename).getPath(), klass);
    }
}
