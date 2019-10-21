package com.apriori.utils.constants;

import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.logging.Level;


public class Constants {

    private static final ConstantsInit constantsInit = ConfigFactory.create(ConstantsInit.class);

    public static final Level consoleLogLevel = Level.parse(constantsInit.consoleLogLevelData());
    public static final String url = constantsInit.url();
    public static final String schemaBasePath = constantsInit.schemaBasePath();
    public static final String internalApiURL = constantsInit.internalApiURL();
    public static final String GRID_SERVER_URL = constantsInit.gridServerUrl();
    public static final String cidURL = constantsInit.cidURL();
    public static final String cirURL = constantsInit.cirURL();

    //    public static final String defaultCidURL = "https://cid-te.awsdev.apriori.com/";
//    public static final String EdcQaURL = "http://edc-api.qa.awsdev.apriori.com/";
//    public static final String schemaBasePath = "schemas/";
//    public static final String localhostdURL = "https://localhost/apriori/cost/session/login?";
//    public static final String cidURL = (StringUtils.isEmpty(System.getProperty("url"))) ? defaultCidURL + "apriori/cost/" : System.getProperty("url");
//    public static final Level consoleLogLevel = (StringUtils.isEmpty(System.getProperty("consoleloglevel"))) ? Level.OFF : Level.parse(System.getProperty("consoleloglevel"));
//    public static final String internalApiURL = Constants.defaultCidURL + "ws";
//    public static final String GRID_SERVER_URL = "http://localhost:4444/wd/hub";
//    public static final String cirURL = (StringUtils.isEmpty(System.getProperty("url"))) ? defaultCidURL + "jasperserver-pro" : System.getProperty("url");
}