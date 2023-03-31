package tests.acs;

import com.apriori.acs.entity.response.acs.genericclasses.GenericExtendedPropertyInfoItem;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoAdditiveManufacturingResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoBarAndTubeResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoCastingDieResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoCastingInvestmentResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoCastingSandResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoSheetMetalHydroformingResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoSheetMetalResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoSheetMetalRollFormingResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoSheetMetalStretchFormingResponse;
import com.apriori.acs.entity.response.acs.materialsinfo.MaterialsInfoSheetMetalTransferDieResponse;
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

    @Test
    @TestRail(testCaseId = "22702")
    @Description("Test Get Materials Info endpoint for Sheet Metal - Transfer Die")
    public void testGetMaterialsInfoSheetMetalTransferDie() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoSheetMetalTransferDieResponse materialsInfoSheetMetalTransferDieResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup(),
                MaterialsInfoSheetMetalTransferDieResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoSheetMetalTransferDieResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoSheetMetalTransferDieResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "22703")
    @Description("Test Get Materials Info endpoint for Sheet Metal - Hydroforming")
    public void testGetMaterialsInfoSheetMetalHydroforming() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoSheetMetalHydroformingResponse materialsInfoSheetMetalHydroformingResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.SHEET_METAL_HYDROFORMING.getProcessGroup(),
                MaterialsInfoSheetMetalHydroformingResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoSheetMetalHydroformingResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoSheetMetalHydroformingResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "22704")
    @Description("Test Get Materials Info endpoint for Sheet Metal - Roll Forming")
    public void testGetMaterialsInfoSheetMetalRollForming() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoSheetMetalRollFormingResponse materialsInfoSheetMetalRollFormingResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.SHEET_METAL_ROLLFORMING.getProcessGroup(),
                MaterialsInfoSheetMetalRollFormingResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoSheetMetalRollFormingResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoSheetMetalRollFormingResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "22705")
    @Description("Test Get Materials Info endpoint for Sheet Metal - Stretch Forming")
    public void testGetMaterialsInfoSheetMetalStretchForming() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoSheetMetalStretchFormingResponse materialsInfoSheetMetalStretchFormingResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING.getProcessGroup(),
                MaterialsInfoSheetMetalStretchFormingResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoSheetMetalStretchFormingResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoSheetMetalStretchFormingResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }
}
