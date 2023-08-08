package com.apriori;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.models.request.workorders.NewPartRequest;
import com.apriori.acs.models.response.acs.genericclasses.GenericResourceCreatedIdResponse;
import com.apriori.acs.models.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.fms.models.response.FileResponse;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.testrail.TestRail;
import com.apriori.workorders.WorkorderAPITests;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

public class RoutingSelectionTests extends TestUtil {

    @Test
    @TestRail(id = 14843)
    @Description("Save Routing Selection after Cost")
    public void testSaveRoutingSelection() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14854)
    @Description("Save Routing Selection after Cost for Additive Manufacturing")
    public void testSaveRoutingSelectionAdditiveManufacturing() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14855)
    @Description("Save Routing Selection after Cost for Bar & Tube")
    public void testSaveRoutingSelectionBarandTube() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14856)
    @Description("Save Routing Selection after Cost for Casting - Die")
    public void testSaveRoutingSelectionCastingDie() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14857)
    @Description("Save Routing Selection after Cost for Casting - Investment")
    public void testSaveRoutingSelectionCastingInvestment() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14858)
    @Description("Save Routing Selection after Cost for Casting - Sand")
    public void testSaveRoutingSelectionCastingSand() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14859)
    @Description("Save Routing Selection after Cost for Forging")
    public void testSaveRoutingSelectionForging() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14860)
    @Description("Save Routing Selection after Cost for Plastic Molding")
    public void testSaveRoutingSelectionPlasticMolding() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14861)
    @Description("Save Routing Selection after Cost for Powder Metal")
    public void testSaveRoutingSelectionPowderMetal() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14862)
    @Description("Save Routing Selection after Cost for Rapid Prototyping")
    public void testSaveRoutingSelectionRapidPrototyping() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14863)
    @Description("Save Routing Selection after Cost for Roto & Blow Molding")
    public void testSaveRoutingSelectionRotoandBlowMolding() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14864)
    @Description("Save Routing Selection after Cost for Sheet Metal")
    public void testSaveRoutingSelectionSheetMetal() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14865)
    @Description("Save Routing Selection after Cost for Sheet Metal - Hydroforming")
    public void testSaveRoutingSelectionSheetMetalHydroforming() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14866)
    @Description("Save Routing Selection after Cost for Sheet Metal - Roll Forming")
    public void testSaveRoutingSelectionSheetMetalRollForming() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14867)
    @Description("Save Routing Selection after Cost for Sheet Metal - Stretch Forming")
    public void testSaveRoutingSelectionSheetMetalStretchForming() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14868)
    @Description("Save Routing Selection after Cost for Sheet Metal - Transfer Die")
    public void testSaveRoutingSelectionSheetMetalTransferDie() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14869)
    @Description("Save Routing Selection after Cost for Sheet Plastic")
    public void testSaveRoutingSelectionSheetPlastic() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(id = 14870)
    @Description("Save Routing Selection after Cost for Stock Machining")
    public void testSaveRoutingSelectionStockMachining() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

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

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }
}
