package tests.acs;

import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesAdditiveManufacturingResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesAssemblyResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesBarAndTubeFabResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesCastingDieResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesCastingInvestmentResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesCastingSandResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesForgingResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesPlasticMoldingResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesRapidPrototypingResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesRotoAndBlowMoldingResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesSheetMetalHydroformingResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesSheetMetalRollFormingResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesSheetMetalTransferDieResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesSheetPlasticResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesStockMachiningResponse;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesTwoModelMachiningResponse;
import com.apriori.acs.entity.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GcdTypesTests {

    private final SoftAssertions softAssertions = new SoftAssertions();
    private final  AcsResources acsResources = new AcsResources();

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "17181")
    @Description("Get available GCDs for Sheet Metal")
    public void testGetGcdTypesSheetMetal() {

        GcdTypesResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.SHEET_METAL.getProcessGroup(), GcdTypesResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getStraightBend().get(0).getName()).isEqualTo("name");
        softAssertions.assertThat(response.getStraightBend().get(4).getDisplayName()).isEqualTo("Bend Angle");
        softAssertions.assertThat(response.getEdge().get(0).getStorageType()).isEqualTo("DOUBLE");
        softAssertions.assertThat(response.getCurvedWall().get(3).getUnitType()).isEqualTo("Length");
        softAssertions.assertThat(response.getCurvedSurface().get(2).getEditable()).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17169")
    @Description("Run API for Assembly Process Group")
    public void testGetGcdTypesAssembly() {

        GcdTypesAssemblyResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.ASSEMBLY.getProcessGroup(), GcdTypesAssemblyResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getWeld().get(3).getName()).isEqualTo("isRobotic");
        softAssertions.assertThat(response.getWeld().get(3).getDisplayName()).isEqualTo("Method");
        softAssertions.assertThat(response.getAssemblyComponentGcd().get(0).getStorageType()).isEqualTo("OBJECT");
        softAssertions.assertThat(response.getAssemblyComponentGcd().get(1).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getContactGroup().get(0).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17170")
    @Description("Run API for 2-Model Machining Process Group")
    public void testGetGcdTypesTwoModelMachining() {

        GcdTypesTwoModelMachiningResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup(), GcdTypesTwoModelMachiningResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getCurvedWall().get(1).getName()).isEqualTo("time");
        softAssertions.assertThat(response.getCurvedWall().get(1).getDisplayName()).isEqualTo("Time");
        softAssertions.assertThat(response.getAxiGroove().get(1).getStorageType()).isEqualTo("DOUBLE");
        softAssertions.assertThat(response.getAxiGroove().get(1).getUnitType()).isEqualTo("Angle");
        softAssertions.assertThat(response.getAxiGroove().get(2).getEditable()).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17171")
    @Description("Run API for Additive Manufacturing Process Group")
    public void testGetGcdTypesAdditiveManufacturing() {

        GcdTypesAdditiveManufacturingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup(), GcdTypesAdditiveManufacturingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getSupportStructure().get(1).getName()).isEqualTo("status");
        softAssertions.assertThat(response.getSupportStructure().get(2).getDisplayName()).isEqualTo("Max Overhang Angle");
        softAssertions.assertThat(response.getSupportStructure().get(1).getStorageType()).isEqualTo("OBJECT");
        softAssertions.assertThat(response.getMultiStepHole().get(3).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getMultiStepHole().get(3).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17172")
    @Description("Run API for Bar & Tube Fab Process Group")
    public void testGetGcdTypesBarAndTubeFab() {

        GcdTypesBarAndTubeFabResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup(), GcdTypesBarAndTubeFabResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getComplexHole().get(1).getName()).isEqualTo("numCurvedEdges");
        softAssertions.assertThat(response.getComplexHole().get(2).getDisplayName()).isEqualTo("Num Sharp Corners");
        softAssertions.assertThat(response.getForm().get(0).getStorageType()).isEqualTo("STRING");
        softAssertions.assertThat(response.getBend().get(2).getUnitType()).isEqualTo("Angle");
        softAssertions.assertThat(response.getBend().get(3).getEditable()).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17173")
    @Description("Run API for Casting - Die Process Group")
    public void testGetGcdTypesCastingDie() {

        GcdTypesCastingDieResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.CASTING_DIE.getProcessGroup(), GcdTypesCastingDieResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getSimpleHole().get(1).getName()).isEqualTo("time");
        softAssertions.assertThat(response.getSimpleHole().get(2).getDisplayName()).isEqualTo("Hole Type");
        softAssertions.assertThat(response.getComboVoid().get(0).getStorageType()).isEqualTo("STRING");
        softAssertions.assertThat(response.getSlideBundle().get(1).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getSlideBundle().get(1).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17174")
    @Description("Run API for Casting - Sand Process Group")
    public void testGetGcdTypesCastingSand() {

        GcdTypesCastingSandResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.CASTING_SAND.getProcessGroup(), GcdTypesCastingSandResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getPlanarFace().get(1).getName()).isEqualTo("time");
        softAssertions.assertThat(response.getPlanarFace().get(2).getDisplayName()).isEqualTo("Flatness");
        softAssertions.assertThat(response.getMultiStepHole().get(0).getStorageType()).isEqualTo("STRING");
        softAssertions.assertThat(response.getComboVoid().get(3).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getAxiGroove().get(1).getEditable()).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17175")
    @Description("Run API for Casting - Investment Process Group")
    public void testGetGcdTypesCastingInvestment() {

        GcdTypesCastingInvestmentResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.CASTING_INVESTMENT.getProcessGroup(), GcdTypesCastingInvestmentResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getCoreBundle().get(1).getName()).isEqualTo("time");
        softAssertions.assertThat(response.getSimpleHole().get(1).getDisplayName()).isEqualTo("Time");
        softAssertions.assertThat(response.getComboVoid().get(0).getStorageType()).isEqualTo("STRING");
        softAssertions.assertThat(response.getSlideBundle().get(3).getUnitType()).isEqualTo("Length");
        softAssertions.assertThat(response.getSlideBundle().get(1).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17176")
    @Description("Run API for Forging Process Group")
    public void testGetGcdTypesForging() {

        GcdTypesForgingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.FORGING.getProcessGroup(), GcdTypesForgingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getCurvedWall().get(0).getName()).isEqualTo("name");
        softAssertions.assertThat(response.getRingedHole().get(1).getDisplayName()).isEqualTo("Time");
        softAssertions.assertThat(response.getVoids().get(0).getStorageType()).isEqualTo("STRING");
        softAssertions.assertThat(response.getComponent().get(6).getUnitType()).isEqualTo("Area");
        softAssertions.assertThat(response.getComponent().get(6).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17177")
    @Description("Run API for Plastic Molding Process Group")
    public void testGetGcdTypesPlasticMolding() {

        GcdTypesPlasticMoldingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup(), GcdTypesPlasticMoldingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getPartingLine().get(0).getName()).isEqualTo("name");
        softAssertions.assertThat(response.getPartingLine().get(1).getDisplayName()).isEqualTo("Draw Direction");
        softAssertions.assertThat(response.getRib().get(0).getStorageType()).isEqualTo("STRING");
        softAssertions.assertThat(response.getMultiStepHole().get(3).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getComboVoid().get(0).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17178")
    @Description("Run API for Powder Metal Process Group")
    public void testGetGcdTypesPowderMetal() {

        GcdTypesForgingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.POWDER_METAL.getProcessGroup(), GcdTypesForgingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getAxiGroove().get(0).getName()).isEqualTo("name");
        softAssertions.assertThat(response.getKeyway().get(2).getDisplayName()).isEqualTo("Tolerance (coordinate)");
        softAssertions.assertThat(response.getRing().get(0).getStorageType()).isEqualTo("STRING");
        softAssertions.assertThat(response.getCurvedWall().get(1).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getRingedHole().get(0).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17179")
    @Description("Run API for Rapid Prototyping Process Group")
    public void testGetGcdTypesRapidPrototyping() {

        GcdTypesRapidPrototypingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup(), GcdTypesRapidPrototypingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getCurvedWall().get(0).getName()).isEqualTo("name");
        softAssertions.assertThat(response.getRingedHole().get(2).getDisplayName()).isEqualTo("Status");
        softAssertions.assertThat(response.getPlanarFace().get(0).getStorageType()).isEqualTo("STRING");
        softAssertions.assertThat(response.getPlanarFace().get(1).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getSimpleHole().get(0).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17180")
    @Description("Run API for Roto & Blow Molding Process Group")
    public void testGetGcdTypesRotoAndBlowMolding() {

        GcdTypesRotoAndBlowMoldingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup(), GcdTypesRotoAndBlowMoldingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getPerimeter().get(0).getName()).isEqualTo("name");
        softAssertions.assertThat(response.getComplexHole().get(2).getDisplayName()).isEqualTo("Num Curved Edges");
        softAssertions.assertThat(response.getAxiGroove().get(0).getStorageType()).isEqualTo("STRING");
        softAssertions.assertThat(response.getPartingLine().get(4).getUnitType()).isEqualTo("Area");
        softAssertions.assertThat(response.getSpout().get(0).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17182")
    @Description("Run API for Sheet Metal - Transfer Die Process Group")
    public void testGetGcdTypesSheetMetalTransferDie() {

        GcdTypesSheetMetalTransferDieResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup(), GcdTypesSheetMetalTransferDieResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getBlank().get(0).getName()).isEqualTo("name");
        softAssertions.assertThat(response.getComplexHole().get(1).getDisplayName()).isEqualTo("Number of Coining Chains");
        softAssertions.assertThat(response.getForm().get(1).getStorageType()).isEqualTo("OBJECT");
        softAssertions.assertThat(response.getEdgeSegment().get(3).getUnitType()).isEqualTo("Length");
        softAssertions.assertThat(response.getLance().get(0).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17183")
    @Description("Run API for Sheet Metal - Hydroforming Process Group")
    public void testGetGcdTypesSheetMetalHydroforming() {

        GcdTypesSheetMetalHydroformingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.SHEET_METAL_HYDROFORMING.getProcessGroup(), GcdTypesSheetMetalHydroformingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getStraightBend().get(0).getName()).isEqualTo("name");
        softAssertions.assertThat(response.getBlank().get(1).getDisplayName()).isEqualTo("Number of Coining Chains");
        softAssertions.assertThat(response.getEdgeSegment().get(1).getStorageType()).isEqualTo("OBJECT");
        softAssertions.assertThat(response.getMultiBend().get(2).getUnitType()).isEqualTo("Angle");
        softAssertions.assertThat(response.getCurvedSurface().get(0).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17184")
    @Description("Run API for Sheet Metal - Roll Forming Process Group")
    public void testGetGcdTypesSheetMetalRollForming() {

        GcdTypesSheetMetalRollFormingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.SHEET_METAL_ROLLFORMING.getProcessGroup(), GcdTypesSheetMetalRollFormingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getStraightBend().get(0).getName()).isEqualTo("name");
        softAssertions.assertThat(response.getForm().get(1).getDisplayName()).isEqualTo("Sub Type");
        softAssertions.assertThat(response.getBend().get(0).getStorageType()).isEqualTo("STRING");
        softAssertions.assertThat(response.getLance().get(2).getUnitType()).isEqualTo("Length");
        softAssertions.assertThat(response.getPlanarFace().get(0).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17185")
    @Description("Run API for Sheet Metal - Stretch Forming Process Group")
    public void testGetGcdTypesSheetMetalStretchForming() {

        GcdTypesSheetMetalTransferDieResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING.getProcessGroup(), GcdTypesSheetMetalTransferDieResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getBlank().get(0).getName()).isEqualTo("name");
        softAssertions.assertThat(response.getComplexHole().get(1).getDisplayName()).isEqualTo("Number of Coining Chains");
        softAssertions.assertThat(response.getForm().get(1).getStorageType()).isEqualTo("OBJECT");
        softAssertions.assertThat(response.getEdgeSegment().get(3).getUnitType()).isEqualTo("Length");
        softAssertions.assertThat(response.getLance().get(0).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17186")
    @Description("Run API for Sheet Plastic Process Group")
    public void testGetGcdTypesSheetPlastic() {

        GcdTypesSheetPlasticResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup(), GcdTypesSheetPlasticResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getComboVoid().get(0).getName()).isEqualTo("name");
        softAssertions.assertThat(response.getComplexHole().get(2).getDisplayName()).isEqualTo("Num Curved Edges");
        softAssertions.assertThat(response.getAxiGroove().get(1).getStorageType()).isEqualTo("DOUBLE");
        softAssertions.assertThat(response.getCoreBundle().get(1).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getCurvedWall().get(2).getEditable()).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17187")
    @Description("Run API for Stock Machining Process Group")
    public void testGetGcdTypesStockMachining() {

        GcdTypesStockMachiningResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.STOCK_MACHINING.getProcessGroup(), GcdTypesStockMachiningResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getPerimeter().get(0).getName()).isEqualTo("name");
        softAssertions.assertThat(response.getAxiGroove().get(1).getDisplayName()).isEqualTo("Helix Angle");
        softAssertions.assertThat(response.getRing().get(1).getStorageType()).isEqualTo("DOUBLE");
        softAssertions.assertThat(response.getPlanarFace().get(1).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getSimpleHole().get(0).getEditable()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17188")
    @Description("Run API for a Process group that does not exist")
    public void testGetGcdTypesForProcessGroupThatDoesNotExist() {
        GenericErrorResponse errorResponse = acsResources.getGcdTypes(
            ProcessGroupEnum.INVALID_PG.getProcessGroup(), GenericErrorResponse.class).getResponseEntity();

        softAssertions.assertThat(errorResponse.getErrorCode()).isEqualTo(404);
        softAssertions.assertThat(errorResponse.getErrorMessage()).isEqualTo("Unknown process group: " + ProcessGroupEnum.INVALID_PG.getProcessGroup());
        softAssertions.assertAll();
    }
}
