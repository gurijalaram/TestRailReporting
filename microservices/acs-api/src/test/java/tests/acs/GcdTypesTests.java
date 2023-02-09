package tests.acs;

import com.apriori.acs.entity.response.acs.GCDTypes.GcdTypesResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GcdTypesTests {
    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14814")
    @Description("Get available GCDs for Sheet Metal")
    public void testGetGCDTypesSheetMetal() {
        AcsResources acsResources = new AcsResources();

        GcdTypesResponse response = acsResources.getGCDTypes(
            ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getStraightBend().get(0).getName()).isEqualTo("name");
        softAssertions.assertThat(response.getStraightBend().get(4).getDisplayName()).isEqualTo("Bend Angle");
        softAssertions.assertThat(response.getEdge().get(0).getStorageType()).isEqualTo("DOUBLE");
        softAssertions.assertThat(response.getCurvedWall().get(3).getUnitType()).isEqualTo("Length");
        softAssertions.assertThat(response.getCurvedSurface().get(2).getEditable()).isEqualTo(true);
        softAssertions.assertAll();
    }
}
