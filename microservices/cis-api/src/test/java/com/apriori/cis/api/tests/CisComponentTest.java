package com.apriori.cis.api.tests;

import static com.apriori.css.api.enums.CssSearch.SCENARIO_CREATED_AT_GT;

import com.apriori.cis.api.controller.CisBidPackageProjectResources;
import com.apriori.cis.api.controller.CisComponentResources;
import com.apriori.cis.api.controller.CisProjectResources;
import com.apriori.cis.api.models.request.bidpackage.AssignedComponentRequest;
import com.apriori.cis.api.models.request.bidpackage.BidPackageItemParameters;
import com.apriori.cis.api.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.cis.api.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.cis.api.models.response.bidpackage.BidPackageResponse;
import com.apriori.cis.api.models.response.component.AssignedComponentsResponse;
import com.apriori.cis.api.models.response.component.ComponentParameters;
import com.apriori.css.api.enums.CssSearch;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.serialization.util.DateFormattingUtils;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.DateUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class CisComponentTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static ScenarioItem scenarioItem;
    private static UserCredentials currentUser;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static String projectName;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser, SCENARIO_CREATED_AT_GT.getKey() + DateUtil.getDateDaysBefore(90, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ), CssSearch.COMPONENT_TYPE_EQ.getKey() + "PART").get(0);
    }

    @Test
    @TestRail(id = {22906})
    @Description("Get Already Assigned Components for specific user")
    public void testGetAlreadyAssignedComponents() {
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        BidPackageProjectResponse bppResponse = CisProjectResources.createProject(new HashMap<>(),
            itemsList,
            usersList,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getUsers().size()).isEqualTo(1);

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

        CisBidPackageProjectResources.deleteBidPackageProject(bppResponse.getBidPackageIdentity(), bppResponse.getIdentity(),
            HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @AfterEach
    public void testCleanup() {
        softAssertions.assertAll();
    }
}
