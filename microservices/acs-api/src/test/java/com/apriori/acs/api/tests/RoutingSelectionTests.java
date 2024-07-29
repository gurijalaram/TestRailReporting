package com.apriori.acs.api.tests;

import com.apriori.acs.api.models.request.workorders.NewPartRequest;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericResourceCreatedIdResponse;
import com.apriori.acs.api.models.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.api.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.api.tests.workorders.WorkorderApiUtils;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.acs.api.utils.workorders.FileUploadResources;
import com.apriori.fms.api.models.response.FileResponse;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class RoutingSelectionTests extends TestUtil {
    private FileUploadResources fileUploadResources;
    private NewPartRequest productionInfoInputs;
    private SoftAssertions softAssertions;
    private AcsResources acsResources;
    private String testScenarioName;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        acsResources = new AcsResources(requestEntityUtil);
        fileUploadResources = new FileUploadResources(requestEntityUtil);
        productionInfoInputs = new WorkorderApiUtils(requestEntityUtil).setupProductionInfoInputs();
        testScenarioName = new GenerateStringUtil().generateStringForAutomation("Scenario");
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = 14843)
    @Description("Save Routing Selection after Cost")
    public void testSaveRoutingSelection() {
        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
                productionInfoInputs,
                fileUploadOutputs,
                processGroup,
                false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Sheet Metal",
            "aPriori USA",
            "Sheet Metal"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14854)
    @Description("Save Routing Selection after Cost for Additive Manufacturing")
    public void testSaveRoutingSelectionAdditiveManufacturing() {
        String processGroup = ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "BasicScenario_Additive.prt.1",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Additive Manufacturing",
            "aPriori India",
            "Additive Manufacturing"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14855)
    @Description("Save Routing Selection after Cost for Bar & Tube")
    public void testSaveRoutingSelectionBarandTube() {
        String processGroup = ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "B&T-LOW-001.SLDPRT",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Bar & Tube Fab",
            "aPriori India",
            "Bar & Tube Fab"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14856)
    @Description("Save Routing Selection after Cost for Casting - Die")
    public void testSaveRoutingSelectionCastingDie() {
        String processGroup = ProcessGroupEnum.CASTING_DIE.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "Casting-Die.stp",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Casting - Die",
            "aPriori India",
            "Casting - Die"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14857)
    @Description("Save Routing Selection after Cost for Casting - Investment")
    public void testSaveRoutingSelectionCastingInvestment() {
        String processGroup = ProcessGroupEnum.CASTING_INVESTMENT.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "piston_model1.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Casting - Investment",
            "aPriori India",
            "Casting - Investment"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14858)
    @Description("Save Routing Selection after Cost for Casting - Sand")
    public void testSaveRoutingSelectionCastingSand() {
        String processGroup = ProcessGroupEnum.CASTING_SAND.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "SandCastIssues.SLDPRT",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Casting - Sand",
            "aPriori India",
            "Casting - Sand"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14859)
    @Description("Save Routing Selection after Cost for Forging")
    public void testSaveRoutingSelectionForging() {
        String processGroup = ProcessGroupEnum.FORGING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "BasicScenario_Forging.stp",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Forging",
            "aPriori India",
            "Forging"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14860)
    @Description("Save Routing Selection after Cost for Plastic Molding")
    public void testSaveRoutingSelectionPlasticMolding() {
        String processGroup = ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "PlasticMoulding.CATPart",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Plastic Molding",
            "aPriori India",
            "Plastic Molding"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14861)
    @Description("Save Routing Selection after Cost for Powder Metal")
    public void testSaveRoutingSelectionPowderMetal() {
        String processGroup = ProcessGroupEnum.POWDER_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "case_31_test_part_6_small.prt.2",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Powder Metal",
            "aPriori India",
            "Powder Metal"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14862)
    @Description("Save Routing Selection after Cost for Rapid Prototyping")
    public void testSaveRoutingSelectionRapidPrototyping() {
        String processGroup = ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "blow_mold_duct_1.prt.1",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Rapid Prototyping",
            "aPriori India",
            "Rapid Prototyping"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14863)
    @Description("Save Routing Selection after Cost for Roto & Blow Molding")
    public void testSaveRoutingSelectionRotoandBlowMolding() {
        String processGroup = ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "blow_mold_duct_1.prt.1",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Roto & Blow Molding",
            "aPriori India",
            "Roto & Blow Molding"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14864)
    @Description("Save Routing Selection after Cost for Sheet Metal")
    public void testSaveRoutingSelectionSheetMetal() {
        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "78828.ipt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Sheet Metal",
            "aPriori India",
            "Sheet Metal"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14865)
    @Description("Save Routing Selection after Cost for Sheet Metal - Hydroforming")
    public void testSaveRoutingSelectionSheetMetalHydroforming() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_HYDROFORMING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "FlangedRound.SLDPRT",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Sheet Metal - Hydroforming",
            "aPriori India",
            "Sheet Metal - Hydroforming"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14866)
    @Description("Save Routing Selection after Cost for Sheet Metal - Roll Forming")
    public void testSaveRoutingSelectionSheetMetalRollForming() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_ROLLFORMING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "gullwing.prt.8",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Sheet Metal - Roll Forming",
            "aPriori India",
            "Sheet Metal - Roll Forming"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14867)
    @Description("Save Routing Selection after Cost for Sheet Metal - Stretch Forming")
    public void testSaveRoutingSelectionSheetMetalStretchForming() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "Hydroforming.stp",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Sheet Metal - Stretch Forming",
            "aPriori India",
            "Sheet Metal - Stretch Forming"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14868)
    @Description("Save Routing Selection after Cost for Sheet Metal - Transfer Die")
    public void testSaveRoutingSelectionSheetMetalTransferDie() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "SheetMetal.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Sheet Metal - Transfer Die",
            "aPriori India",
            "Sheet Metal - Transfer Die"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14869)
    @Description("Save Routing Selection after Cost for Sheet Plastic")
    public void testSaveRoutingSelectionSheetPlastic() {
        String processGroup = ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "r151294.prt.1",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Sheet Plastic",
            "aPriori India",
            "Sheet Plastic"
        );

        performRoutingSelectionAssertions(response);
    }

    @Test
    @TestRail(id = 14870)
    @Description("Save Routing Selection after Cost for Stock Machining")
    public void testSaveRoutingSelectionStockMachining() {
        String processGroup = ProcessGroupEnum.STOCK_MACHINING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "testpart-4.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Stock Machining",
            "aPriori India",
            "Stock Machining"
        );

        performRoutingSelectionAssertions(response);
    }

    private void performRoutingSelectionAssertions(GenericResourceCreatedIdResponse response) {
        softAssertions.assertThat(response.getId()).isNotNull();
        softAssertions.assertThat(response.getResourceCreated()).isEqualTo("true");
        softAssertions.assertAll();
    }
}
