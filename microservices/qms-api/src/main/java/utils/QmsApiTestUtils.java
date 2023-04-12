package utils;

import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.entity.request.scenariodiscussion.Attributes;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionParameters;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionRequest;
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
        if (PropertiesContext.get("customer").startsWith("ap-int")) {
            return new UserCredentials().setEmail("testUser1@widgets.aprioritest.com");
        }
        return new UserCredentials().setEmail("qa-automation-01@apriori.com");
    }

    public static ScenarioDiscussionRequest getScenarioDiscussionRequest(UserCredentials assignedUser, ScenarioItem scenarioItem, String description) {
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .status("ACTIVE")
                .type("SCENARIO")
                .assigneeEmail(assignedUser.getEmail())
                .description(description)
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .attributes(Attributes.builder()
                    .attribute("materialName")
                    .subject("4056-23423-003")
                    .build())
                .build())
            .build();
        return scenarioDiscussionRequest;
    }
}

