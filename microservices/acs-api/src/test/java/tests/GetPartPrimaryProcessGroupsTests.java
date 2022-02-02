package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.getpartprimaryprocessgroups.GetPartPrimaryProcessGroupsResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetPartPrimaryProcessGroupsTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10881")
    @Description("Validate Get Part Primary Process Groups Endpoint")
    public void testGetPartPrimaryProcessGroupsEndpoint() {
        AcsResources acsResources = new AcsResources();
        GetPartPrimaryProcessGroupsResponse getPartPrimaryProcessGroupsResponse = acsResources.getPartPrimaryProcessGroups();

        assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().size(), is(equalTo(21)));

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(0).equals(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(1).equals(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(2).equals(ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(3).equals(ProcessGroupEnum.CASTING.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(4).equals(ProcessGroupEnum.CASTING_DIE.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(5).equals(ProcessGroupEnum.CASTING_INVESTMENT.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(6).equals(ProcessGroupEnum.CASTING_SAND.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(7).equals(ProcessGroupEnum.COMPOSITES.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(8).equals(ProcessGroupEnum.FORGING.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(9).equals(ProcessGroupEnum.MULTI_SPINDLE_MACHINING.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(10).equals(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(11).equals(ProcessGroupEnum.POWDER_METAL.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(12).equals(ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(13).equals(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(14).equals(ProcessGroupEnum.SHEET_METAL.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(15).equals(ProcessGroupEnum.SHEET_METAL_HYDROFORMING.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(16).equals(ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(17).equals(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(18).equals(ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(19).equals(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup()));
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.getProcessGroups().get(20).equals(ProcessGroupEnum.USER_GUIDED.getProcessGroup()));
        softAssertions.assertAll();
    }
}
