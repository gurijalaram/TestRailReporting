package com.apriori.utils.constants;

import org.apache.commons.lang3.StringUtils;

import java.util.logging.Level;

public class Constants {

    public static final String defaultCidURL = "https://cid-te.awsdev.apriori.com/";
    public static final String EdcQaURL = "http://edc-api.qa.awsdev.apriori.com/";
    public static final String schemaBasePath = "schemas/";
    public static final String localhostdURL = "https://localhost/apriori/cost/session/login?";
    public static final String cidURL = (StringUtils.isEmpty(System.getProperty("url"))) ? defaultCidURL + "apriori/cost/" : System.getProperty("url");
    public static final Level consoleLogLevel = (StringUtils.isEmpty(System.getProperty("consoleloglevel"))) ? Level.OFF : Level.parse(System.getProperty("consoleloglevel"));
    public static final String internalApiURL = Constants.defaultCidURL + "ws";
    public static final String GRID_SERVER_URL = "http://localhost:4444/wd/hub";
}