package com.apriori;

import com.apriori.acs.models.request.workorders.NewPartRequest;
import com.apriori.acs.models.response.acs.availableroutings.AvailableRoutingsFirstLevel;
import com.apriori.acs.models.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.fms.models.response.FileResponse;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;
import com.apriori.workorders.WorkorderAPITests;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AvailableRoutingsTests {

    @Test
    @TestRail(id = 14814)
    @Description("Get available routings after Cost")
    public void testGetAvailableRoutingsCosted() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14812)
    @Description("Get available routings before Cost")
    public void testGetAvailableRoutingsUncosted() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();

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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
                fileUploadOutputs.getScenarioIterationKey(),
                "aPriori USA",
                ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isEqualTo("UNCOSTED");
        softAssertions.assertAll();

    }

    @Test
    @TestRail(id = 14823)
    @Description("Get available routings after Cost for Additive Manufacturing scenario")
    public void testGetAvailableRoutingsAdditiveManufacturing() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
                costOutputs.getScenarioIterationKey(),
                "aPriori USA",
                ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14824)
    @Description("Get available routings after Cost for Bar & Tube Fab scenario")
    public void testGetAvailableRoutingsBarandTube() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14825)
    @Description("Get available routings after Cost for Casting - Die scenario")
    public void testGetAvailableRoutingsCastingDie() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.CASTING_DIE.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14826)
    @Description("Get available routings after Cost for Casting - Investment scenario")
    public void testGetAvailableRoutingsCastingInvestment() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.CASTING_INVESTMENT.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14827)
    @Description("Get available routings after Cost for Casting - Sand scenario")
    public void testGetAvailableRoutingsCastingSand() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.CASTING_SAND.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14828)
    @Description("Get available routings after Cost for Forging scenario")
    public void testGetAvailableRoutingsForging() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.FORGING.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14829)
    @Description("Get available routings after Cost for Plastic Molding scenario")
    public void testGetAvailableRoutingsPlasticMolding() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14830)
    @Description("Get available routings after Cost for Powder Metal scenario")
    public void testGetAvailableRoutingsPowderMetal() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.POWDER_METAL.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14834)
    @Description("Get available routings after Cost for Rapid Prototyping scenario")
    public void testGetAvailableRoutingsRapidPrototyping() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14835)
    @Description("Get available routings after Cost for Roto & Blow Molding scenario")
    public void testGetAvailableRoutingsRotoandBlowMolding() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14836)
    @Description("Get available routings after Cost for Sheet Metal scenario")
    public void testGetAvailableRoutingsSheetMetal() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14837)
    @Description("Get available routings after Cost for Sheet Metal - Hydroforming scenario")
    public void testGetAvailableRoutingsSheetMetalHydroforming() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.SHEET_METAL_HYDROFORMING.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14838)
    @Description("Get available routings after Cost for Sheet Metal - Roll Forming scenario")
    public void testGetAvailableRoutingsSheetMetalRollForming() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.SHEET_METAL_ROLLFORMING.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14839)
    @Description("Get available routings after Cost for Sheet Metal - Stretch Forming scenario")
    public void testGetAvailableRoutingsSheetMetalStretchForming() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 148340)
    @Description("Get available routings after Cost for Sheet Metal - Transfer Die scenario")
    public void testGetAvailableRoutingsSheetMetalTransferDie() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14841)
    @Description("Get available routings after Cost for Sheet Plastic scenario")
    public void testGetAvailableRoutingsSheetPlastic() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isNotNull();
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 14842)
    @Description("Get available routings after Cost for Stock Machining scenario")
    public void testGetAvailableRoutingsStockMachining() {
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

        AvailableRoutingsFirstLevel response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.STOCK_MACHINING.getProcessGroup()
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getName()).isNotNull();
        softAssertions.assertThat(response.getDisplayName()).isNotNull();
        softAssertions.assertThat(response.getPlantName()).isNotNull();
        softAssertions.assertThat(response.getProcessGroupName()).isEqualTo("Stock Machining");
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }
}