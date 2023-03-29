package tests.acs;

import com.apriori.acs.entity.response.acs.genericclasses.GenericExtendedPropertyInfoItem;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoAdditiveManufacturingResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoBarAndTubeResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoSheetMetalResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class MaterialsInfoTests {

    @Test
    @TestRail(testCaseId = "22695")
    @Description("Test Get Materials Info endpoint for Additive Manufacturing")
    public void testGetMaterialsInfoAdditiveManufacturing() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoAdditiveManufacturingResponse materialsInfoAdditiveManufacturingResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup(),
                MaterialsInfoAdditiveManufacturingResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoAdditiveManufacturingResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem manufacturer = materialsInfoAdditiveManufacturingResponse.getPropertyInfoMap().getManufacturer();

        softAssertions.assertThat(manufacturer.getName()).isNotNull();
        softAssertions.assertThat(manufacturer.getSupportedSerializedType()).isEqualTo("STRING");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "22691")
    @Description("Test Get Materials Info endpoint for Bar & Tube Fab")
    public void testGetMaterialsInfoBarAndTubeFab() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoBarAndTubeResponse materialsInfoBarAndTubeResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup(),
                MaterialsInfoBarAndTubeResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoBarAndTubeResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoBarAndTubeResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "22701")
    @Description("Test Get Materials Info endpoint for Sheet Metal")
    public void testGetMaterialsInfoSheetMetal() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoSheetMetalResponse materialsInfoSheetMetalResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.SHEET_METAL.getProcessGroup(),
                MaterialsInfoSheetMetalResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoSheetMetalResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoSheetMetalResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }
}
