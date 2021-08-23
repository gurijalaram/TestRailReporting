package com.apriori.utils;

import com.apriori.utils.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Constants {

    /**
     * Get default url
     *
     * @return string
     */
    public static String getApiUrl() {
        return PropertiesContext.get("${env}.cas.api_url");
    }
}