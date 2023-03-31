package tests.acs;

import com.apriori.acs.entity.response.acs.genericclasses.GenericExtendedPropertyInfoItem;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoAdditiveManufacturingResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoBarAndTubeResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoCastingDieResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoCastingInvestmentResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoCastingSandResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoSheetMetalResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
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
    @Ignore
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

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoBarAndTubeResponse.getPropertyInfoMap().getTBeamCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "22692")
    @Description("Test Get Materials Info endpoint for Casting - Die")
    public void testGetMaterialsInfoCastingDie() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoCastingDieResponse materialsInfoCastingDieResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.CASTING_DIE.getProcessGroup(),
                MaterialsInfoCastingDieResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoCastingDieResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoCastingDieResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "22693")
    @Description("Test Get Materials Info endpoint for Casting - Sand")
    public void testGetMaterialsInfoCastingSand() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoCastingSandResponse materialsInfoCastingSandResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.CASTING_SAND.getProcessGroup(),
                MaterialsInfoCastingSandResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoCastingSandResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoCastingSandResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "22694")
    @Description("Test Get Materials Info endpoint for Casting - Investment")
    public void testGetMaterialsInfoCastingInvestment() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoCastingInvestmentResponse materialsInfoCastingInvestmentResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.CASTING_INVESTMENT.getProcessGroup(),
                MaterialsInfoCastingInvestmentResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoCastingInvestmentResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoCastingInvestmentResponse.getPropertyInfoMap().getCostPerKG();

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
