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
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import testsuites.categories.AcsTest;

public class GcdTypesTests {

    private final SoftAssertions softAssertions = new SoftAssertions();
    private final AcsResources acsResources = new AcsResources();

    @Test
    @Category(AcsTest.class)
    @TestRail(id = 17181)
    @Description("Get available GCDs for Sheet Metal")
    public void testGetGcdTypesSheetMetal() {

        GcdTypesResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.SHEET_METAL.getProcessGroup(), GcdTypesResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getStraightBend().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("name"));
        softAssertions.assertThat(response.getStraightBend().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Bend Angle"));
        softAssertions.assertThat(response.getEdge().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("DOUBLE"));
        softAssertions.assertThat(response.getCurvedWall().get(2).getUnitType()).isEqualTo("Length");
        softAssertions.assertThat(response.getCurvedSurface().stream()).anyMatch(x -> x.getEditable().equals(true));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17169)
    @Description("Run API for Assembly Process Group")
    public void testGetGcdTypesAssembly() {

        GcdTypesAssemblyResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.ASSEMBLY.getProcessGroup(), GcdTypesAssemblyResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getWeld().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("isRobotic"));
        softAssertions.assertThat(response.getWeld().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Method"));
        softAssertions.assertThat(response.getAssemblyComponentGcd().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("OBJECT"));
        softAssertions.assertThat(response.getAssemblyComponentGcd().get(1).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getContactGroup().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17170)
    @Description("Run API for 2-Model Machining Process Group")
    public void testGetGcdTypesTwoModelMachining() {

        GcdTypesTwoModelMachiningResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup(), GcdTypesTwoModelMachiningResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getCurvedWall().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("time"));
        softAssertions.assertThat(response.getCurvedWall().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Time"));
        softAssertions.assertThat(response.getAxiGroove().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("DOUBLE"));
        softAssertions.assertThat(response.getAxiGroove().get(1).getUnitType()).isEqualTo("Angle");
        softAssertions.assertThat(response.getAxiGroove().stream()).anyMatch(x -> x.getEditable().equals(true));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17171)
    @Description("Run API for Additive Manufacturing Process Group")
    public void testGetGcdTypesAdditiveManufacturing() {

        GcdTypesAdditiveManufacturingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup(), GcdTypesAdditiveManufacturingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getSupportStructure().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("status"));
        softAssertions.assertThat(response.getSupportStructure().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Max Overhang Angle"));
        softAssertions.assertThat(response.getSupportStructure().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("OBJECT"));
        softAssertions.assertThat(response.getMultiStepHole().get(3).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getMultiStepHole().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17172)
    @Description("Run API for Bar & Tube Fab Process Group")
    public void testGetGcdTypesBarAndTubeFab() {

        GcdTypesBarAndTubeFabResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup(), GcdTypesBarAndTubeFabResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getComplexHole().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("numCurvedEdges"));
        softAssertions.assertThat(response.getComplexHole().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Num Sharp Corners"));
        softAssertions.assertThat(response.getForm().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("STRING"));
        softAssertions.assertThat(response.getBend().get(2).getUnitType()).isEqualTo("Angle");
        softAssertions.assertThat(response.getBend().stream()).anyMatch(x -> x.getEditable().equals(true));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17173)
    @Description("Run API for Casting - Die Process Group")
    public void testGetGcdTypesCastingDie() {

        GcdTypesCastingDieResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.CASTING_DIE.getProcessGroup(), GcdTypesCastingDieResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getSimpleHole().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("time"));
        softAssertions.assertThat(response.getSimpleHole().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Hole Type"));
        softAssertions.assertThat(response.getComboVoid().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("STRING"));
        softAssertions.assertThat(response.getSlideBundle().get(1).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getSlideBundle().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17174)
    @Description("Run API for Casting - Sand Process Group")
    public void testGetGcdTypesCastingSand() {

        GcdTypesCastingSandResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.CASTING_SAND.getProcessGroup(), GcdTypesCastingSandResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getPlanarFace().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("time"));
        softAssertions.assertThat(response.getPlanarFace().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Flatness"));
        softAssertions.assertThat(response.getMultiStepHole().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("STRING"));
        softAssertions.assertThat(response.getComboVoid().get(3).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getAxiGroove().stream()).anyMatch(x -> x.getEditable().equals(true));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17175)
    @Description("Run API for Casting - Investment Process Group")
    public void testGetGcdTypesCastingInvestment() {

        GcdTypesCastingInvestmentResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.CASTING_INVESTMENT.getProcessGroup(), GcdTypesCastingInvestmentResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getCoreBundle().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("time"));
        softAssertions.assertThat(response.getSimpleHole().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Time"));
        softAssertions.assertThat(response.getComboVoid().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("STRING"));
        softAssertions.assertThat(response.getSlideBundle().get(3).getUnitType()).isEqualTo("Length");
        softAssertions.assertThat(response.getSlideBundle().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17176)
    @Description("Run API for Forging Process Group")
    public void testGetGcdTypesForging() {

        GcdTypesForgingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.FORGING.getProcessGroup(), GcdTypesForgingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getCurvedWall().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("name"));
        softAssertions.assertThat(response.getRingedHole().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Time"));
        softAssertions.assertThat(response.getVoids().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("STRING"));
        softAssertions.assertThat(response.getComponent().get(6).getUnitType()).isEqualTo("Area");
        softAssertions.assertThat(response.getComponent().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17177)
    @Description("Run API for Plastic Molding Process Group")
    public void testGetGcdTypesPlasticMolding() {

        GcdTypesPlasticMoldingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup(), GcdTypesPlasticMoldingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getPartingLine().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("name"));
        softAssertions.assertThat(response.getPartingLine().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Draw Direction"));
        softAssertions.assertThat(response.getRib().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("STRING"));
        softAssertions.assertThat(response.getMultiStepHole().get(3).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getComboVoid().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17178)
    @Description("Run API for Powder Metal Process Group")
    public void testGetGcdTypesPowderMetal() {

        GcdTypesForgingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.POWDER_METAL.getProcessGroup(), GcdTypesForgingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getAxiGroove().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("name"));
        softAssertions.assertThat(response.getKeyway().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Tolerance (coordinate)"));
        softAssertions.assertThat(response.getRing().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("STRING"));
        softAssertions.assertThat(response.getCurvedWall().get(1).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getRingedHole().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17179)
    @Description("Run API for Rapid Prototyping Process Group")
    public void testGetGcdTypesRapidPrototyping() {

        GcdTypesRapidPrototypingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup(), GcdTypesRapidPrototypingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getCurvedWall().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("name"));
        softAssertions.assertThat(response.getRingedHole().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Status"));
        softAssertions.assertThat(response.getPlanarFace().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("STRING"));
        softAssertions.assertThat(response.getPlanarFace().get(1).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getSimpleHole().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17180)
    @Description("Run API for Roto & Blow Molding Process Group")
    public void testGetGcdTypesRotoAndBlowMolding() {

        GcdTypesRotoAndBlowMoldingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup(), GcdTypesRotoAndBlowMoldingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getPerimeter().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("name"));
        softAssertions.assertThat(response.getComplexHole().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Num Curved Edges"));
        softAssertions.assertThat(response.getAxiGroove().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("STRING"));
        softAssertions.assertThat(response.getPartingLine().get(4).getUnitType()).isEqualTo("Area");
        softAssertions.assertThat(response.getSpout().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17182)
    @Description("Run API for Sheet Metal - Transfer Die Process Group")
    public void testGetGcdTypesSheetMetalTransferDie() {

        GcdTypesSheetMetalTransferDieResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup(), GcdTypesSheetMetalTransferDieResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getBlank().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("name"));
        softAssertions.assertThat(response.getComplexHole().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Number of Coining Chains"));
        softAssertions.assertThat(response.getForm().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("OBJECT"));
        softAssertions.assertThat(response.getEdgeSegment().get(3).getUnitType()).isEqualTo("Length");
        softAssertions.assertThat(response.getLance().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17183)
    @Description("Run API for Sheet Metal - Hydroforming Process Group")
    public void testGetGcdTypesSheetMetalHydroforming() {

        GcdTypesSheetMetalHydroformingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.SHEET_METAL_HYDROFORMING.getProcessGroup(), GcdTypesSheetMetalHydroformingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getStraightBend().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("name"));
        softAssertions.assertThat(response.getBlank().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Number of Coining Chains"));
        softAssertions.assertThat(response.getEdgeSegment().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("OBJECT"));
        softAssertions.assertThat(response.getForm().get(4).getUnitType()).isEqualTo("Area");
        softAssertions.assertThat(response.getCurvedSurface().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17184)
    @Description("Run API for Sheet Metal - Roll Forming Process Group")
    public void testGetGcdTypesSheetMetalRollForming() {

        GcdTypesSheetMetalRollFormingResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.SHEET_METAL_ROLLFORMING.getProcessGroup(), GcdTypesSheetMetalRollFormingResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getStraightBend().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("name"));
        softAssertions.assertThat(response.getForm().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Sub Type"));
        softAssertions.assertThat(response.getBend().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("STRING"));
        softAssertions.assertThat(response.getLance().get(2).getUnitType()).isEqualTo("Length");
        softAssertions.assertThat(response.getPlanarFace().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17185)
    @Description("Run API for Sheet Metal - Stretch Forming Process Group")
    public void testGetGcdTypesSheetMetalStretchForming() {

        GcdTypesSheetMetalTransferDieResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING.getProcessGroup(), GcdTypesSheetMetalTransferDieResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getBlank().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("name"));
        softAssertions.assertThat(response.getComplexHole().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Number of Coining Chains"));
        softAssertions.assertThat(response.getForm().stream()).anyMatch(x -> x.getStorageType().equals("OBJECT"));
        softAssertions.assertThat(response.getEdgeSegment().get(3).getUnitType()).isEqualTo("Length");
        softAssertions.assertThat(response.getLance().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17186)
    @Description("Run API for Sheet Plastic Process Group")
    public void testGetGcdTypesSheetPlastic() {

        GcdTypesSheetPlasticResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup(), GcdTypesSheetPlasticResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getPerimeter().stream()).anyMatch(x -> x.getName().equalsIgnoreCase("name"));
        softAssertions.assertThat(response.getComplexHole().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Num Curved Edges"));
        softAssertions.assertThat(response.getAxiGroove().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("DOUBLE"));
        softAssertions.assertThat(response.getCoreBundle().get(1).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getCurvedWall().stream()).anyMatch(x -> x.getEditable().equals(true));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17187)
    @Description("Run API for Stock Machining Process Group")
    public void testGetGcdTypesStockMachining() {

        GcdTypesStockMachiningResponse response = acsResources.getGcdTypes(
            ProcessGroupEnum.STOCK_MACHINING.getProcessGroup(), GcdTypesStockMachiningResponse.class).getResponseEntity();

        softAssertions.assertThat(response.getPerimeter().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("name"));
        softAssertions.assertThat(response.getAxiGroove().stream()).anyMatch(x -> x.getDisplayName().equalsIgnoreCase("Helix Angle"));
        softAssertions.assertThat(response.getMultiStepHole().stream()).anyMatch(x -> x.getStorageType().equalsIgnoreCase("DOUBLE"));
        softAssertions.assertThat(response.getPlanarFace().get(1).getUnitType()).isEqualTo("Time");
        softAssertions.assertThat(response.getSimpleHole().stream()).anyMatch(x -> x.getEditable().equals(false));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17188)
    @Description("Run API for a Process group that does not exist")
    public void testGetGcdTypesForProcessGroupThatDoesNotExist() {
        GenericErrorResponse errorResponse = acsResources.getGcdTypes(
            ProcessGroupEnum.INVALID_PG.getProcessGroup(), GenericErrorResponse.class).getResponseEntity();

        softAssertions.assertThat(errorResponse.getErrorCode()).isEqualTo(404);
        softAssertions.assertThat(errorResponse.getErrorMessage()).isEqualTo("Unknown process group: " + ProcessGroupEnum.INVALID_PG.getProcessGroup());
        softAssertions.assertAll();
    }
}
