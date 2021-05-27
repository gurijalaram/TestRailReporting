package utils;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Constants {

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "cidapp-int";
    public static final List<String> INPUT_VALUES = Arrays.asList("annual volume", "batch size", "material", "production life", "component name", "description", "notes",
        "scenario name", "tolerance count", "fully burdened cost", "material cost", "piece part cost", "total capital investment",
        "cycle time", "finish mass", "process routing", "utilization");
    public static final List<String> TOGGLE_VALUES = Arrays.asList("cad connected", "locked", "published");
    public static final List<String> DATE_VALUES = Arrays.asList("created at", "last updated at");
    public static final List<String> TYPE_INPUT_VALUES = Arrays.asList("process group", "vpe", "assignee", "component type", "cost maturity", "created by", "last updated by",
        "state", "status", "dfm");
    private static final Logger logger = LoggerFactory.getLogger(Constants.class);
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    public static String environment;
    private static String baseUrl;
    private static String secretKey;
    private static String cidServiceHost;
    private static String cidTokenUsername;
    private static String cidTokenEmail;
    private static String cidTokenIssuer;
    private static String cidTokenSubject;
    private static String cidApiUrl;
    private static String cssApiUrl;

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(environment.concat(".properties"));

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
            String properties = PROPERTIES.stringPropertyNames().stream()
                .map(key -> key + "=" + PROPERTIES.getProperty(key) + "\n")
                .collect(Collectors.joining());
            logger.info(String.format("Listing properties for '%s' " + "\n" + "%s", environment, properties));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get default url
     *
     * @return string
     */
    public static String getDefaultUrl() {
        baseUrl = System.getProperty(DEFAULT_BASE_URL_KEY) == null ? PROPERTIES.getProperty("url.default") : System.getProperty(DEFAULT_BASE_URL_KEY);
        System.setProperty("baseUrl", baseUrl);

        return baseUrl;
    }

    /**
     * Get default url
     *
     * @return string
     */
    public static String getApiUrl() {
        return cidApiUrl = System.getProperty("cidApiUrl") == null ? PROPERTIES.getProperty("cid.api.url").concat("%s") : System.getProperty("cidApiUrl");
    }


    /**
     * Get secret key
     *
     * @return string
     */
    public static String getSecretKey() {
        return secretKey = System.getProperty("cidSecretKey") == null ? PROPERTIES.getProperty("cid.secret.key") : System.getProperty("cidSecretKey");
    }

    /**
     * Get service host
     *
     * @return string
     */
    public static String getCidServiceHost() {
        return cidServiceHost = System.getProperty("cidServiceHost") == null ? PROPERTIES.getProperty("cid.service.host") : System.getProperty("cidServiceHost");
    }

    /**
     * Get token username
     *
     * @return string
     */
    public static String getCidTokenUsername() {
        return cidTokenUsername = System.getProperty("cidTokenUsername") == null ? PROPERTIES.getProperty("cid.token.username") : System.getProperty("cidTokenUsername");
    }

    /**
     * Get token email
     *
     * @return string
     */
    public static String getCidTokenEmail() {
        return cidTokenEmail = System.getProperty("cidTokenEmail") == null ? PROPERTIES.getProperty("cid.token.email") : System.getProperty("cidTokenEmail");
    }

    /**
     * Get token issuer
     *
     * @return string
     */
    public static String getCidTokenIssuer() {
        return cidTokenIssuer = System.getProperty("cidTokenIssuer") == null ? PROPERTIES.getProperty("cid.token.issuer") : System.getProperty("cidTokenIssuer");
    }

    /**
     * Get token subject
     *
     * @return string
     */
    public static String getCidTokenSubject() {
        return cidTokenSubject = System.getProperty("cidTokenSubject") == null ? PROPERTIES.getProperty("cid.token.subject") : System.getProperty("cidTokenSubject");
    }

    /**
     * Get css url
     *
     * @return string
     */
    public static String getCssApiUrl() {
        return cssApiUrl = System.getProperty("cssApiUrl") == null ? PROPERTIES.getProperty("css.api.url").concat("%s") : System.getProperty("cssApiUrl");
    }
}
