package com.apriori;


import static com.apriori.enums.CssSearch.SCENARIO_CREATED_AT_GT;

import com.apriori.cis.controller.CisComponentResources;
import com.apriori.cis.models.request.bidpackage.AssignedComponentRequest;
import com.apriori.cis.models.response.component.AssignedComponentsResponse;
import com.apriori.cis.models.response.component.ComponentParameters;
import com.apriori.http.utils.DateUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.models.response.ScenarioItem;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class CisComponentTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static ScenarioItem scenarioItem;
    private static UserCredentials currentUser;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser, SCENARIO_CREATED_AT_GT.getKey() + DateUtil.getDateDaysBefore(90, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ)).get(0);
    }

    @Test
    @TestRail(id = {22906})
    @Description("Get Already Assigned Components for specific user")
    public void testGetAlreadyAssignedComponents() {
        ComponentParameters componentParameters = ComponentParameters.builder()
            .componentIdentity(scenarioItem.getComponentIdentity())
            .scenarioIdentity(scenarioItem.getScenarioIdentity())
            .iterationIdentity(scenarioItem.getIterationIdentity())
            .build();

        AssignedComponentRequest componentModel = AssignedComponentRequest.builder().components(Collections.singletonList(componentParameters)).build();
        AssignedComponentsResponse assignedComponentsResponse = CisComponentResources.postToGetAssignedComponents(currentUser, componentModel, AssignedComponentsResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(assignedComponentsResponse.size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(assignedComponentsResponse.stream()
                .anyMatch(bp -> bp.getComponentIdentity().equals(scenarioItem.getComponentIdentity()) &&
                    bp.getScenarioIdentity().equals(scenarioItem.getScenarioIdentity()) &&
                    bp.getIterationIdentity().equals(scenarioItem.getIterationIdentity()))
            ).isTrue();
        }
    }

    @AfterEach
    public void testCleanup() {
        softAssertions.assertAll();
    }
}
