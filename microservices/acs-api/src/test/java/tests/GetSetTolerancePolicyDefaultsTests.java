package tests;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.getsettolerancepolicydefaults.GetTolerancePolicyDefaultsResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.utils.TestRail;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetSetTolerancePolicyDefaultsTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10473")
    @Description("Test Get Tolerance Policy Defaults")
    public void testGetTolerancePolicyDefaults() {
        AcsResources acsResources = new AcsResources();
        GetTolerancePolicyDefaultsResponse response = acsResources.getTolerancePolicyDefaults();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getPropertyValueMap().getTotalRunoutOverride() == 1.4);
        softAssertions.assertThat(response.getPropertyValueMap().getPerpendicularityOverride() == 1.1);
        softAssertions.assertThat(response.getPropertyValueMap().getSymmetryOverride() == 1.6);
        softAssertions.assertThat(response.getPropertyValueMap().getRoughnessOverride() == 1.2);
        softAssertions.assertThat(response.getPropertyValueMap().getCircularityOverride() == 0.6);
        softAssertions.assertThat(response.getPropertyValueMap().getMinCadToleranceThreshhold() == 0.2);
        softAssertions.assertThat(response.getPropertyValueMap().getToleranceMode().equals("SYSTEMDEFAULT"));
        softAssertions.assertThat(response.getPropertyValueMap().getBendAngleToleranceOverride() == 0.5);
        softAssertions.assertThat(response.getPropertyValueMap().getRunoutOverride() == 1.3);
        softAssertions.assertThat(response.getPropertyValueMap().getFlatnessOverride() == 0.0);
        softAssertions.assertThat(response.getPropertyValueMap().getParallelismOverride() == 1.0);
        softAssertions.assertThat(!response.getPropertyValueMap().isUseCadToleranceThreshhold());
        softAssertions.assertThat(response.getPropertyValueMap().getCadToleranceReplacement() == 0.35);
        softAssertions.assertThat(response.getPropertyValueMap().getStraightnessOverride() == 1.5);
        softAssertions.assertThat(response.getPropertyValueMap().getPositionToleranceOverride() == 0.4);
        softAssertions.assertThat(response.getPropertyValueMap().getProfileOfSurfaceOverride() == 1.2);
        softAssertions.assertThat(response.getPropertyValueMap().getRoughnessRzOverride() == 0.2);
        softAssertions.assertThat(response.getPropertyValueMap().getToleranceOverride() == 0.0);
        softAssertions.assertThat(response.getPropertyValueMap().getDiamToleranceOverride() == 0.3);
        softAssertions.assertThat(response.getPropertyValueMap().getConcentricityOverride() == 0.7);
        softAssertions.assertThat(response.getPropertyValueMap().getCylindricityOverride() == 0.9);

        softAssertions.assertAll();
    }
}
