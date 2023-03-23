package utils;

import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;

import java.util.HashMap;
import java.util.Map;

public class QmsApiTestUtils {

    /**
     * setup header information for DDS API Authorization
     *
     * @return Map
     */
    public static Map<String, String> setUpHeader(String cloudContext) {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Content-Type", "application/json");
        header.put("ap-cloud-context", cloudContext);
        return header;
    }

    public static UserCredentials getCustomerUser() {
        switch (PropertiesContext.get("customer")) {
            case "ap-int":
                return new UserCredentials().setEmail("testUser1@widgets.aprioritest.com");
            default:
                return new UserCredentials().setEmail("qa-automation-01@apriori.com");
        }
    }
}

