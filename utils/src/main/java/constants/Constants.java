package main.java.constants;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Constants {

    public static final String defaultCidURL = "https://cid-te.awsdev.apriori.com/";
    public static final String schemaBasePath = "schemas/";
    public static final String cidURL = (StringUtils.isEmpty(System.getProperty("url"))) ? defaultCidURL + "apriori/cost/" : System.getProperty("url");
    public static final Level consoleLogLevel = (StringUtils.isEmpty(System.getProperty("consoleloglevel"))) ? Level.OFF : Level.parse(System.getProperty("consoleloglevel"));
    public static final String internalApiURL = Constants.defaultCidURL + "ws";
    public static final String GRID_SERVER_URL = "http://localhost:4444/wd/hub";

    //TODO z: discuss, is it a good idea, to use it, by this way
    // it is easy to change common authorization process which require FORM creds
    // in plans to add common request data, for Session authorization if we need it
    public static final Map<String, String> DEFAULT_AUTHORIZATION_FORM_WITHOUT_USERCREDS = new HashMap<String, String>() {{
        put("grant_type", "password");
        put("client_id", "apriori-web-cost");
        put("client_secret", "donotusethiskey");
        put("scope", "tenantGroup%3Ddefault%20tenant%3Ddefault");
    }};


}
