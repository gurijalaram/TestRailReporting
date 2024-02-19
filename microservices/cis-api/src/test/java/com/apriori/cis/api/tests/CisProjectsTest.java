package com.apriori.cis.api.tests;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cis.api.controller.CisBidPackageProjectResources;
import com.apriori.cis.api.controller.CisProjectResources;
import com.apriori.cis.api.models.request.bidpackage.BidPackageItemParameters;
import com.apriori.cis.api.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.cis.api.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.cis.api.util.CISTestUtil;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
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
import java.util.HashMap;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class CisProjectsTest extends CISTestUtil {

    private static SoftAssertions softAssertions;
    private static UserCredentials currentUser;
    private static ScenarioItem scenarioItem;
    private static ComponentInfoBuilder componentInfoBuilder;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        componentInfoBuilder = new ScenariosUtil().uploadAndPublishComponent(new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING, currentUser));
        scenarioItem = new CssComponent().getWaitBaseCssComponents(componentInfoBuilder.getUser(), COMPONENT_NAME_EQ.getKey() + componentInfoBuilder.getComponentName(),
            SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName()).get(0);
    }

    @Test
    @TestRail(id = {22898})
    @Description("Create and Delete Project")
    public void testCreateAndDeleteProject() {
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

        CisBidPackageProjectResources.deleteBidPackageProject(bppResponse.getBidPackageIdentity(), bppResponse.getIdentity(),
            HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @AfterEach
    public void testCleanup() {
        new ScenariosUtil().deleteScenario(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
