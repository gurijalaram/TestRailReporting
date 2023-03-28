package tests.acs;

import com.apriori.acs.entity.response.acs.genericclasses.GenericExtendedPropertyInfoItem;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class MaterialsInfoTests {

    @Test
    @TestRail(testCaseId = "16829")
    @Description("Test Get Materials Info endpoint for Sheet Metal")
    public void testGetMaterialsInfoSheetMetal() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoResponse materialsInfoResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.SHEET_METAL.getProcessGroup()
            );

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }
}
