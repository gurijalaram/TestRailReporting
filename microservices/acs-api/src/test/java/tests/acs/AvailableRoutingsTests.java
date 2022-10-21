package tests.acs;

import com.apriori.acs.entity.request.workorders.NewPartRequest;
import com.apriori.acs.entity.response.acs.availableroutings.AvailableRoutingsFirstLevel;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import tests.workorders.WorkorderAPITests;
import testsuites.categories.AcsTest;

public class AvailableRoutingsTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14814")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14812")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14823")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14824")
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
        softAssertions.assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14825")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14826")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14827")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14828")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14829")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14830")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14834")
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
            "Plastic moulded cap DFM.CATPart",
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14835")
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
            "Rapid Prototyping.stp",
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14836")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14837")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14838")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14839")
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
            "G05752358-107 --.CATPart",
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "148340")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14841")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14842")
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