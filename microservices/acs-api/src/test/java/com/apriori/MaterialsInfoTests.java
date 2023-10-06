package com.apriori;

import com.apriori.acs.models.response.acs.genericclasses.GenericExtendedPropertyInfoItem;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoAdditiveManufacturingResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoBarAndTubeResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoCastingDieResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoCastingInvestmentResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoCastingSandResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoForgingResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoPlasticMoldingResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoPowderMetalResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoRapidPrototypingResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoRotoBlowMoldingResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoSheetMetalHydroformingResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoSheetMetalResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoSheetMetalRollFormingResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoSheetMetalStretchFormingResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoSheetMetalTransferDieResponse;
import com.apriori.acs.models.response.acs.materialsinfo.MaterialsInfoStockMachiningResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class MaterialsInfoTests {

    @Test
    @TestRail(id = 22695)
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
    @TestRail(id = 22691)
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

        GenericExtendedPropertyInfoItem tBeamCostPerKG = materialsInfoBarAndTubeResponse.getPropertyInfoMap().getTeeBeamCostPerKG();

        softAssertions.assertThat(tBeamCostPerKG.getName()).isEqualTo("tBeamCostPerKG");
        softAssertions.assertThat(tBeamCostPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(tBeamCostPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 22692)
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
    @TestRail(id = 22693)
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
    @TestRail(id = 22694)
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
    @TestRail(id = 22696)
    @Description("Test Get Materials Info endpoint for Forging")
    public void testGetMaterialsInfoForging() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoForgingResponse materialsInfoForgingResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.FORGING.getProcessGroup(),
                MaterialsInfoForgingResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoForgingResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem roundBarCostPerKG = materialsInfoForgingResponse.getPropertyInfoMap().getRoundBarCostPerKG();

        softAssertions.assertThat(roundBarCostPerKG.getName()).isEqualTo("roundBarCostPerKG");
        softAssertions.assertThat(roundBarCostPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(roundBarCostPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 22697)
    @Description("Test Get Materials Info endpoint for Plastic Molding")
    public void testGetMaterialsInfoPlasticMolding() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoPlasticMoldingResponse materialsInfoPlasticMoldingResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup(),
                MaterialsInfoPlasticMoldingResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoPlasticMoldingResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoPlasticMoldingResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 22698)
    @Description("Test Get Materials Info endpoint for Powder Metal")
    public void testGetMaterialsInfoPowderMetal() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoPowderMetalResponse materialsInfoPowderMetalResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.POWDER_METAL.getProcessGroup(),
                MaterialsInfoPowderMetalResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoPowderMetalResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoPowderMetalResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 22699)
    @Description("Test Get Materials Info endpoint for Rapid Prototyping")
    public void testGetMaterialsInfoRapidPrototyping() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoRapidPrototypingResponse materialsInfoRapidPrototypingResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup(),
                MaterialsInfoRapidPrototypingResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoRapidPrototypingResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoRapidPrototypingResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 22700)
    @Description("Test Get Materials Info endpoint for Roto & Blow Molding")
    public void testGetMaterialsInfoRotoBlowMolding() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoRotoBlowMoldingResponse materialsInfoRotoBlowMoldingResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup(),
                MaterialsInfoRotoBlowMoldingResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoRotoBlowMoldingResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = materialsInfoRotoBlowMoldingResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 22701)
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
    @TestRail(id = 22702)
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
    @TestRail(id = 22703)
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
    @TestRail(id = 22704)
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
    @TestRail(id = 22705)
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

    @Test
    @TestRail(id = 22706)
    @Description("Test Get Materials Info endpoint for Stock Machining")
    public void testGetMaterialsInfoStockMachining() {
        AcsResources acsResources = new AcsResources();
        MaterialsInfoStockMachiningResponse materialsInfoStockMachiningResponse = acsResources
            .getMaterialsInfo(
                "aPriori USA",
                ProcessGroupEnum.STOCK_MACHINING.getProcessGroup(),
                MaterialsInfoStockMachiningResponse.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(materialsInfoStockMachiningResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem plateCostPerKG = materialsInfoStockMachiningResponse.getPropertyInfoMap().getPlateCostPerKG();

        softAssertions.assertThat(plateCostPerKG.getName()).isEqualTo("plateCostPerKG");
        softAssertions.assertThat(plateCostPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(plateCostPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }
}
