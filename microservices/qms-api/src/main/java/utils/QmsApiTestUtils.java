package utils;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.entity.enums.CssSearch;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.entity.response.bidpackage.ComponentResponse;
import com.apriori.qms.entity.response.bidpackage.ScenariosResponse;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.utils.CssComponent;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;

import java.io.File;
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
        UserCredentials otherCustomerUser;
        switch (PropertiesContext.get("customer")) {
            case "ap-int":
                return new UserCredentials().setEmail("testUser1@widgets.aprioritest.com");
            default:
                return new UserCredentials().setEmail("qa-automation-01@apriori.com");
        }
    }
}

