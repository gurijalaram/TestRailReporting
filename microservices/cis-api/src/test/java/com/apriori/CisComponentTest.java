package com.apriori;

import com.apriori.cisapi.controller.CisBidPackageItemResources;
import com.apriori.cisapi.controller.CisBidPackageResources;
import com.apriori.cisapi.controller.CisComponentResources;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageResponse;
import com.apriori.cisapi.entity.response.component.AssignComponentsResponse;
import com.apriori.entity.response.ScenarioItem;
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

public class CisComponentTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static ScenarioItem scenarioItem;
    private static final UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        String bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        bidPackageResponse = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);
        CisBidPackageItemResources.createBidPackageItem(
            CisBidPackageItemResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);
    }

    @Test
    @TestRail(id = {22906})
    @Description("Get Already Assigned Components for specific user")
    public void testGetAlreadyAssignedComponents() {
        AssignComponentsResponse assignedComponentsResponse = CisComponentResources.getAssignedComponents(currentUser, AssignComponentsResponse.class, HttpStatus.SC_OK);
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
        CisBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }
}
