package com.apriori.bcs.utils;

import com.apriori.utils.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Constants {

    public static final int MAX_NUMBER_OF_PARTS = 100;
    public static final int MULTIPART_POLLING_INTERVALS = 25;
    public static final int MULTIPART_POLLING_WAIT = 10000;
    public static final int MULTIPART_THREAD_COUNT = 100;
    public static final int POLLING_TIMEOUT = 25;
    public static final String SECRET_KEY = PropertiesContext.get("${env}.secret_key");
}

