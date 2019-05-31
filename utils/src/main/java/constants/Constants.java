package main.java.constants;

import org.apache.commons.lang3.StringUtils;

import java.util.logging.Level;

public class Constants {

    public static final String defaultCidURL = "https://cid-te.awsdev.apriori.com/apriori/cost/";
    public static final String cidURL = (StringUtils.isEmpty(System.getProperty("url"))) ? defaultCidURL : System.getProperty("url");
    public static final Level consoleLogLevel = (StringUtils.isEmpty(System.getProperty("consoleloglevel"))) ? Level.OFF : Level.parse(System.getProperty("consoleloglevel"));
    public static final String internalApiURL = Constants.cidURL.endsWith("/") ? Constants.defaultCidURL + "ch" : Constants.cidURL + "/ws";
    public static final String GRID_SERVER_URL = "http://localhost:4444/wd/hub";
}
